/*
Per sviluppare questa classe, applichiamo i concetti architetturali dei sistemi distribuiti basati su oggetti distribuiti.
Questa classe rappresenta l'oggetto remoto effettivo (lato server). I client, attraverso il Registry RMI, otterranno uno Stub (Proxy) che si occuperà
di effettuare il marshalling delle richieste, trasmetterle via rete e attendere le risposte, nascondendo la complessità della comunicazione.

Sul lato server, dato che verrà istanziato un solo oggetto GestoreAutenticazioniImpl, che verrà condiviso tra tutti i thread client concorrenti, l'oggetto dovrà
essere thread-safe. Questo risultato è garantito trasformando questa classe in un Monitor, utilizzando il modificatore synchronized sui metodi, per garantire la mutua
esclusione nell'accesso alle risorse critiche (il database e la struttura dati delle sessioni di accesso).
*/


/* 1. STRUTTURA BASE
Definiamo le variabili di istanza per la connessione al database e la struttura dati per gestire le sessioni attive alla piattaforma.
Utilizziamo HashSet per tracciare gli utenti attualmente loggati.
*/

package remote_services;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.models.Utente;
import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.exceptions.InvalidUsernameException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class GestoreAutenticazioniImpl extends UnicastRemoteObject implements GestoreAutenticazioni{
    private final String url_db = "jdbc:postgresql://localhost:5432/theknife";
    private final String utente_db = "postgres";
    private final String password_db = "carmine!";
    
    // Struttura dati per impedire accessi multipli contemporanei
    private final Set<String> sessioniAttive; // contiene una serie (lista) di stringhe (es. ID di sessione) e non può contenere duplicati, ovvero due volte la stessa stringa
    
    public GestoreAutenticazioniImpl() throws RemoteException{
        super(); // richiama il costruttore di UnicastRemoteObject per esportare l'oggetto
        this.sessioniAttive = new HashSet<>(); // implememtazione di Set basata su tabelle di hash, che introduce operazioni add(), contains(), remove() che, mediamente, hanno complessità O(1)
        /* NON SERVE
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.err.println("Driver JDBC PostgreSQL non trovato");
            e.printStackTrace();
        }
        */
    }
    
    /* 2. IMPLEMENTAZIONE METODI SINCRONIZZATI
    Applichiamo il costrutto try-with-resources per garantire che Connection, PreparedStatement e ResultSet vengano chiusi automaticamente al termine del blocco try
    prevenendo memory leak e l'esaurimento delle connessioni al db.
    */
    
    /**
     * Il metodo verifica che username ed email non esistano già, in modo tale da rispettare i vincoli UNIQUE del database e lanciare eccezioni correttamente. 
     * Verifica che i dati utilizzati durante la registrazione non siano già utilizzati da un altro utente presente nel database.
     * @param utente l'utente da registrare
     * @throws RemoteException
     * @throws InvalidUsernameException
     * @throws InvalidEMailException 
     */
    @Override
    public synchronized void registraUtente(Utente utente) throws RemoteException, InvalidUsernameException, InvalidEMailException{
        String checkQuery = "SELECT username, email FROM Utenti WHERE username = ? OR email = ?";
        String insertQuery = "INSERT INTO Utenti (nome, cognome, username, email, password_hash, data_nascita, citta, nazione, ruolo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement checkSt = connection.prepareStatement(checkQuery)){
            
            checkSt.setString(1, utente.getUsername());
            checkSt.setString(2, utente.getEMail());
            
            try(ResultSet rs = checkSt.executeQuery()){ // esegue checkQuery
                while(rs.next()){
                    if(rs.getString("username").equals(utente.getUsername())){
                        throw new InvalidUsernameException(); // Username già in uso
                    }
                    if(rs.getString("email").equals(utente.getEMail())){
                        throw new InvalidEMailException(); // Indirizzo email già registrato
                    }
                }
            }
            
            // Se controlli sui dati di registrazione passano, inserimento utente nel db
            try(PreparedStatement insertSt = connection.prepareStatement(insertQuery)){
                insertSt.setString(1, utente.getNome());
                insertSt.setString(2, utente.getCognome());
                insertSt.setString(3, utente.getUsername());
                insertSt.setString(4, utente.getEMail());
                insertSt.setString(5, utente.getPassword());
                
                if(utente.getDataNascita() != null){
                    insertSt.setDate(6, java.sql.Date.valueOf(utente.getDataNascita()));
                }else{
                    insertSt.setNull(6, Types.DATE);
                }
                
                insertSt.setString(7, utente.getCitta());
                insertSt.setString(8, utente.getNazione());
                insertSt.setString(9, utente.getRuolo());
                
                insertSt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DB durante la registrazione: " + e.getMessage());
            throw new RemoteException("Errore di comunicazione con il database", e);
        }
    }

    // prima verifico la memoria locale (Set) per bloccare login multipli, poi al database per validare le credenziali
    @Override
    public synchronized void login(Utente utente) throws RemoteException, InvalidSessionRequestException{
        // 1. Controllo mutua esclusione sulle sessioni
        if(sessioniAttive.contains(utente.getUsername())){
            throw new InvalidSessionRequestException(); // L'utente utente.getUsername() ha già una sessione attiva nel sistema
        }
        
        // 2. Verifica sul db
        String query = "SELECT password_hash FROM Utenti WHERE username = ?";
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement st = connection.prepareStatement(query)){
            
            st.setString(1, utente.getUsername());
            
            try(ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    String dbHash = rs.getString("password_hash");
                    if(dbHash.equals(utente.getPassword())){
                        // aggiunge l'utente che ha fatto il login a Set (memoria server)
                        sessioniAttive.add(utente.getUsername());
                    }else{
                        throw new InvalidSessionRequestException(); // Password errata
                    }
                }else{
                    throw new InvalidSessionRequestException(); // Username inesistente
                } 
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DB durante il login: " + e.getMessage());
            throw new RemoteException("Errore interno del server", e);
        }
    }

    // basta agire sulla memoria interna del server, ovvero su Set
    @Override
    public synchronized void logout(Utente utente) throws RemoteException, InvalidSessionRequestException{
        if(!sessioniAttive.contains(utente.getUsername())){
            throw new InvalidSessionRequestException(); // Impossibile effettuare il logout: l'utente non è attualmente loggato
        } 
        sessioniAttive.remove(utente.getUsername());
    }

    @Override
    public synchronized void eliminaAccount(Utente utente) throws RemoteException{
        String query = "DELETE FROM Utenti WHERE username = ?";
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement st = connection.prepareStatement(query)){
            
            st.setString(1, utente.getUsername());
            int removedRows = st.executeUpdate();
            
            /* 
            Misura di sicurezza: non ha senso rimuovere un utente dalla lista delle sessioni attive
            se non siamo riusciti a cancellarlo dal db. 
            Se removedRows è > 0, l'utente è stato trovato ed eliminato
            */
            if(removedRows > 0){ 
                sessioniAttive.remove(utente.getUsername());
            }else{
                // Se removedRows è 0, l'utente che si voleva eliminare non esisteva nel DB
                throw new RemoteException("Utente non trovato per l'eliminazione");
            }
        } catch (SQLException e) {
            System.err.println("Errore DB durante l'eliminazione dell'account: " + e.getMessage());
            throw new RemoteException("Errore del server durante l'eliminazione", e);
        }
    }
}

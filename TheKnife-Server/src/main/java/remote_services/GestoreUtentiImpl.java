package remote_services;

import a_a.theknife.common.exceptions.InvalidCoordinatesException;
import a_a.theknife.common.interfaces.GestoreUtenti;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;
import a_a.theknife.common.models.GestorePassword;
import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidUsernameException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class GestoreUtentiImpl extends UnicastRemoteObject implements GestoreUtenti{
    private final String url_db = "jdbc:postgresql://localhost:5432/theknife";
    private final String utente_db = "postgres";
    private final String password_db = "carmine!";
    
    public GestoreUtentiImpl() throws RemoteException{
        super();
        try{
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e) {
            System.err.println("Driver JDBC PostgreSQL non trovato");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void modificaUsername(Utente utente, String nuovoUsername) throws RemoteException, InvalidUsernameException{
        String checkQuery = "SELECT id FROM Utenti WHERE username = ?"; // query per verificare se il nuovo username che l'utente vuole utilizzare è già utilizzato da qualche altro utente nel db
        // utilizzo l'id dell'utente (pk) per essere sicuro di aggiornare l'username corretto
        String updateQuery = "UPDATE Utenti SET username = ? WHERE id = ?";
        
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement checkSt = connection.prepareStatement(checkQuery)){
            
            checkSt.setString(1, nuovoUsername); // username = nuovoUsername
            try(ResultSet rs = checkSt.executeQuery()){ // esegue checkQuery
                if(rs.next()){
                    throw new InvalidUsernameException(); // Il nuovo username è già utilizzato
                }
            }
            
            // Se il controllo passa, ovvero se il nuovoUsername non è già presente nel database, allora si passa alla modifica del vecchio username
            try(PreparedStatement updateSt = connection.prepareStatement(updateQuery)){
                updateSt.setString(1, nuovoUsername);
                updateSt.setInt(2, utente.getId());
                
                int updatedRows = updateSt.executeUpdate();
                if(updatedRows == 0){
                    throw new RemoteException("Errore: utente non trovato nel database per l'aggiornamento del relativo username");
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore DB durante la modifica username: " + e.getMessage());
            throw new RemoteException("Errore di comunicazione con il database", e);
        }
    }

    @Override
    public synchronized void modificaPassword(Utente utente, String nuovaPassword) throws RemoteException, InvalidPasswordException{
        if(!GestorePassword.validate(nuovaPassword)){ // Se la nuovaPassword non rispetta i requisiti necessari per una password valida
            throw new InvalidPasswordException();
        }
        
        String hashPassword = GestorePassword.hashPassword(nuovaPassword);
        String updateQuery = "UPDATE Utenti SET password_hash = ? WHERE id = ?";
        
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement updateSt = connection.prepareStatement(updateQuery)){
            
            updateSt.setString(1, hashPassword);
            updateSt.setInt(2, utente.getId());
            updateSt.executeUpdate();
            
            utente.setPassword(nuovaPassword); // dopo l'hashing salvato sul db, salvo la nuovaPassword in chiaro all'istanza Utente
        } catch (SQLException e) {
            System.err.println("Errore DB durate la modifica della password: " + e.getMessage());
            throw new RemoteException("Errore di comunicazione con il database", e);
        }
    }

    @Override
    public synchronized ArrayList<Ristorante> visualizzaPreferiti(Utente utente) throws RemoteException{
        ArrayList<Ristorante> preferiti = new ArrayList<>();
        
        /*
        Query principale: trova i ristoranti preferiti dell'utente con id = ?
        JOIN basata sulla struttura: Preferiti.cliente punta a Utenti.id e Preferiti.ristorante punta a Ristoranti.id
        */
        String queryRistoranti = "SELECT r.* " 
                               + "FROM Ristoranti r JOIN Preferiti p ON r.id = p.ristorante " 
                               + "WHERE p.cliente = ?";
        
        /*
        Query secondarie: servono per popolare gli ArrayList richiesti dal costruttore di Ristorante.java
        - queryServizi: seleziona i servizi offerti dal ristorante con id = ?
        - queryCucine: seleziona i tipi di cucina offerti dal ristorante con id = ?
        */
        String queryServizi = "SELECT servizio FROM ServiziOfferti WHERE ristorante = ?";
        String queryCucine = "SELECT tipo_cucina FROM ProposteCulinarie WHERE ristorante = ?";
        
        
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement stRistoranti = connection.prepareStatement(queryRistoranti);
            PreparedStatement stServizi = connection.prepareStatement(queryServizi);
            PreparedStatement stCucine = connection.prepareStatement(queryCucine)){
            
            stRistoranti.setInt(1, utente.getId()); // p.cliente = utente.getId()
            
            try(ResultSet rsRistoranti = stRistoranti.executeQuery()){ // esegue queryRistoranti: trova i preferiti dell'utente con id utente.getId()
                // scorriamo ogni ristorante preferito trovato
                while(rsRistoranti.next()){ // rsRistoranti corrisponde ad un oggetto Ristorante nella lista dei preferiti dell'utente
                    int idRistorante = rsRistoranti.getInt("id"); // id del ristorante preferito dell'utente
                    
                    ArrayList<String> servizi = new ArrayList<>();
                    stServizi.setInt(1, idRistorante); // ristorante = idRistorante
                    try(ResultSet rsServizi = stServizi.executeQuery()){ // esegue queryServizi
                        while(rsServizi.next()){
                            servizi.add(rsServizi.getString("servizio"));
                        }
                    }
                    
                    ArrayList<String> tipiCucina = new ArrayList<>();
                    stCucine.setInt(1, idRistorante); // ristorante = idRistorante
                    try(ResultSet rsCucine = stCucine.executeQuery()){
                        while(rsCucine.next()){
                            tipiCucina.add(rsCucine.getString("tipo_cucina"));
                        }
                    }
                    
                    try{
                        Ristorante r = new Ristorante(
                            idRistorante,
                            rsRistoranti.getInt("proprietario"),
                            rsRistoranti.getString("nome"),
                            rsRistoranti.getString("indirizzo"),
                            rsRistoranti.getString("citta"),
                            rsRistoranti.getString("nazione"),
                            rsRistoranti.getString("fascia_prezzo"),
                            rsRistoranti.getDouble("latitudine"),
                            rsRistoranti.getDouble("longitudine"),
                            rsRistoranti.getString("recapito_telefonico"),
                            rsRistoranti.getString("pagina_michelin"),
                            rsRistoranti.getString("pagina_web"),
                            rsRistoranti.getString("riconoscimento"),
                            rsRistoranti.getBoolean("stella_green"),
                            rsRistoranti.getString("descrizione"),
                            rsRistoranti.getBoolean("prenotazione_online"),
                            rsRistoranti.getBoolean("delivery"),
                            servizi,
                            tipiCucina
                        );
                        // se la creazione ha successo, lo aggiungiamo alla lista dei ristoranti da restituire al client
                        preferiti.add(r);
                        
                    } catch (InvalidCoordinatesException e) {
                        // Se il DB contiene coordinate sbagliate (lat e long) per un ristorante, 
                        // saltiamo il ristorante, invece di far crashare l'intera richiesta dell'utente
                        throw new RemoteException("Attenzione: Il ristorante ID " + idRistorante + " ha coordinate non valide nel DB e sarà ignorato");
                        // System.err.println("Attenzione: Il ristorante ID " + idRistorante + " ha coordinate non valide nel DB e sarà ignorato");
                    }
                }
            }  
        } catch (SQLException e) {
            System.err.println("Errore DB durante la lettura dei preferiti: " + e.getMessage());
            throw new RemoteException("Errore di comunicazione con il database", e);
        }
        return preferiti;
    }
    
    @Override
    public synchronized void aggiungiPreferito(Utente utente, Ristorante ristorante) throws RemoteException{
        /*
        Il cliente utente.getId() aggiunge alla sua lista dei preferiti un ristorante ristorante.getId()
        */
        String insertQuery = "INSERT INTO Preferiti (cliente, ristorante) VALUES (?, ?)";
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement st = connection.prepareStatement(insertQuery)){
            
            st.setInt(1, utente.getId()); // cliente = utente.getId()
            st.setInt(2, ristorante.getId()); // ristorante = ristorante.getId()
            st.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Errore DB in aggiunta del ristorante preferito: " + e.getMessage());
        }
    }

    @Override
    public synchronized void rimuoviPreferito(Utente utente, Ristorante ristorante) throws RemoteException{
        /*
        Il cliente utente.getId() rimuove dalla sua lista dei preferiti un ristorante ristorante.getId()
        */
        String deleteQuery = "DELETE FROM Preferiti WHERE cliente = ? AND ristorante = ?";
        try(Connection connection = DriverManager.getConnection(url_db, utente_db, password_db);
            PreparedStatement st = connection.prepareStatement(deleteQuery)){
            
            st.setInt(1, utente.getId()); // cliente = utente.getId()
            st.setInt(2, ristorante.getId()); // ristorante = ristorante.getId()
            st.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Errore DB durante la rimozione di un preferito: " + e.getMessage());
            throw new RemoteException("Errore del server durante l'eliminazione", e);
        }
    }
}

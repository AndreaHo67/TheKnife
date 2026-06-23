package a_a.theknife.server.remote_services;

import a_a.theknife.server.db_info.CredenzialiDatabase;

import a_a.theknife.common.interfaces.GestoreRecensioni;

import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.sql.*;

import java.util.ArrayList;

import java.time.LocalDate;

public class GestoreRecensioniImpl extends UnicastRemoteObject implements GestoreRecensioni{
    private static final String DB_URL = CredenzialiDatabase.getDbUrl();
    private static final String DB_USER = CredenzialiDatabase.getDbUser();
    private static final String DB_PASSWORD = CredenzialiDatabase.getDbPassword();
    
    public GestoreRecensioniImpl() throws RemoteException{
    }

    @Override // -- FUNZIONA --
    public ArrayList<Recensione> visualizzaRecensioni(Utente utente) throws RemoteException{
        String query = "SELECT re.id AS id_recensione, u1.username, re.valutazione, re.testo AS testo_recensione, re.ultima_modifica AS ultima_modifica_recensione, ri.id AS id_risposta, ri.autore AS autore_risposta, ri.testo AS testo_risposta, ri.ultima_modifica AS ultima_modifica_risposta "
                     + "FROM Utenti u1 JOIN Recensioni re ON u1.id = re.autore LEFT JOIN Risposte ri ON re.id = ri.recensione JOIN Utenti u2 ON u2.id = ri.autore "
                     + "WHERE re.autore = ?";
        
        ArrayList<Recensione> recensioni = new ArrayList<>();
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, utente.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int idRecensione = resultSet.getInt("id_recensione");
                    String autoreRecensione = resultSet.getString("username");
                    int valutazione = resultSet.getInt("valutazione");
                    String testoRecensione = resultSet.getString("testo_recensione");
                    LocalDate ultimaModificaRecensione = resultSet.getDate("ultima_modifica_recensione").toLocalDate();
                    
                    Risposta risposta = null;
                    int idRisposta = resultSet.getInt("id_risposta");
                    if(idRisposta != 0){ // nel db non ci sono id = 0, e sono tutti auto_increment. Se è 0 -> la left join non ha trovato una risposta associata
                        String autoreRisposta = resultSet.getString("autore_risposta");
                        String testoRisposta = resultSet.getString("testo_risposta");
                        LocalDate ultimaModificaRisposta = resultSet.getDate("ultima_modifica_risposta").toLocalDate();
                        try{
                            risposta = new Risposta(idRisposta, autoreRisposta, testoRisposta, ultimaModificaRisposta);
                        }
                        catch(TextTooLongException e){
                            // TMCH = To Make Compiler Happy, questa eccezione qui NON può verificarsi.
                        }
                    }
                    try{
                        recensioni.add(new Recensione(idRecensione, autoreRecensione, valutazione, testoRecensione, ultimaModificaRecensione, risposta));
                    }
                    catch(InvalidRatingException | TextTooLongException e){
                        // TMCH = To Make Compiler Happy, queste eccezioni qui NON possono verificarsi.
                    }
                }
            }
        }
        catch(SQLException e){
            throw(new RemoteException("Comunicazione con il database fallita", e));
        }
        return(recensioni);
    }

    @Override // -- FUNZIONA --
    public void scriviRecensione(Recensione recensione, Ristorante ristorante) throws RemoteException{
        String queryGet = "SELECT u.id "
                        + "FROM Utenti u "
                        + "WHERE u.username = ?";
        String queryInsert = "INSERT INTO Recensioni(autore, valutazione, testo, ultima_modifica, ristorante) "
                           + "VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement getPreparedStatement = connection.prepareStatement(queryGet);
            PreparedStatement insertPreparedStatement = connection.prepareStatement(queryInsert)){
            getPreparedStatement.setString(1, recensione.getAutore());
            
            int autore;
            try(ResultSet resultSet = getPreparedStatement.executeQuery()){
                if(resultSet.next()){
                    autore = resultSet.getInt("id");
                }
                else{
                    throw(new SQLException("Utente non trovato: " + recensione.getAutore()));
                }
            }
            catch(SQLException f){
                f.printStackTrace();
                throw(f);
            }
            
            insertPreparedStatement.setInt(1, autore);
            insertPreparedStatement.setInt(2, recensione.getValutazione());
            insertPreparedStatement.setString(3, recensione.getTesto());
            insertPreparedStatement.setDate(4, Date.valueOf(recensione.getUltimaModifica()));
            insertPreparedStatement.setInt(5, ristorante.getId());
            
            insertPreparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw(new RemoteException("Errore DB", e));
        }
    }

    @Override // -- FUNZIONA --
    public void eliminaRecensione(Recensione recensione) throws RemoteException{
        String query = "DELETE FROM Recensioni "
                     + "WHERE id = ?";
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, recensione.getId());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            throw(new RemoteException("Comunicazione con il database fallita", e));
        }
    }

    @Override // -- FUNZIONA --
    public void scriviRispostaRecensione(Recensione recensione, Risposta risposta) throws RemoteException, ReviewNotExistsException, AnswerAlreadyExistsException{
        String checkQuery1 = "SELECT COUNT(*) AS conta_recensioni "
                           + "FROM Recensioni "
                           + "WHERE id = ?";
        String checkQuery2 = "SELECT COUNT(*) AS conta_risposte "
                           + "FROM Risposte "
                           + "WHERE recensione = ?";
        String getQuery = "SELECT id "
                        + "FROM Utenti "
                        + "WHERE username = ?";
        String insertQuery = "INSERT INTO Risposte(recensione, autore, testo, ultima_modifica) "
                           + "VALUES (?, ?, ?, ?)";
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement check1PreparedStatement = connection.prepareStatement(checkQuery1);
            PreparedStatement check2PreparedStatement = connection.prepareStatement(checkQuery2);
            PreparedStatement getPreparedStatement = connection.prepareStatement(getQuery);
            PreparedStatement insertPreparedStatement = connection.prepareStatement(insertQuery)){
            
            check1PreparedStatement.setInt(1, recensione.getId());
            try(ResultSet check1ResultSet = check1PreparedStatement.executeQuery()){
                if(check1ResultSet.next()){
                    if(check1ResultSet.getInt("conta_recensioni") == 0){
                        throw(new ReviewNotExistsException());
                    }
                }
                else{
                    throw(new RemoteException("Comunicazione con il database fallita"));
                }
            }
            
            check2PreparedStatement.setInt(1, recensione.getId());
            try(ResultSet check2ResultSet = check2PreparedStatement.executeQuery()){
                if(check2ResultSet.next()){
                    if(check2ResultSet.getInt("conta_risposte") != 0){
                        throw(new AnswerAlreadyExistsException());
                    }
                }
                
            }
            
            getPreparedStatement.setString(1, risposta.getAutore());
            
            int autore;
            try(ResultSet getResultSet = getPreparedStatement.executeQuery()){
                if(getResultSet.next()){
                    autore = getResultSet.getInt("id");
                }
                else{
                    throw(new RemoteException("Utente non trovato"));
                }
            }
            
            insertPreparedStatement.setInt(1, recensione.getId());
            insertPreparedStatement.setInt(2, autore);
            insertPreparedStatement.setString(3, risposta.getTesto());
            insertPreparedStatement.setDate(4, Date.valueOf(risposta.getData()));
            insertPreparedStatement.executeUpdate();
            
        }
        catch(SQLException e){
            throw(new RemoteException("Comunicazione con il database fallita", e));
        }
    }
}
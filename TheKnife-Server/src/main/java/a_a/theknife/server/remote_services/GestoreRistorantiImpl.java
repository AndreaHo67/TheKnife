package a_a.theknife.server.remote_services;

import a_a.theknife.server.db_info.CredenzialiDatabase;

import a_a.theknife.common.interfaces.GestoreRistoranti;

import a_a.theknife.common.models.Filtro;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidCoordinatesException;
import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.NoReviewsException;
import a_a.theknife.common.exceptions.TextTooLongException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.sql.*;

import java.time.LocalDate;

import java.util.ArrayList;

public class GestoreRistorantiImpl extends UnicastRemoteObject implements GestoreRistoranti{
    private static final String DB_URL = CredenzialiDatabase.getDbUrl();
    private static final String DB_USER = CredenzialiDatabase.getDbUser();
    private static final String DB_PASSWORD = CredenzialiDatabase.getDbPassword();
    
    public GestoreRistorantiImpl() throws RemoteException{
    }
    
    @Override // -- FUNZIONA --
    public int numeroRistorantiRegistrati() throws RemoteException{
        String query = "SELECT COUNT(r.id) AS contaRistoranti "
                     + "FROM Ristoranti r";
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return(resultSet.getInt("contaRistoranti"));
                }
                return(0);
        }
        catch(SQLException e){
            throw(new RemoteException());
        }
    }

    @Override // -- FUNZIONA --
    public double calcolaValutazioneMedia(Ristorante ristorante) throws RemoteException, NoReviewsException{
            String query = "SELECT AVG(r.valutazione) AS valutazioneMedia "
                         + "FROM Recensioni r "
                         + "WHERE r.ristorante = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, ristorante.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return(resultSet.getDouble("valutazioneMedia"));
                }
                else{
                    throw(new NoReviewsException());
                }
            }
            catch(SQLException e){
                throw(new RemoteException());
            }
        }
        catch(SQLException e){
            throw(new RemoteException());
        }
    }

    @Override // -- FUNZIONA --
    public ArrayList<Ristorante> ricercaRistorantiPerNome(String nomeRistorante) throws RemoteException{
        String query = "SELECT r.id, r.proprietario, r.nome, r.indirizzo, r.citta, r.nazione, r.fascia_prezzo, r.latitudine, r.longitudine, r.recapito_telefonico, r.pagina_michelin, r.pagina_web, r.riconoscimento, r.stella_green, r.descrizione, r.prenotazione_online, r.delivery, s.servizio, p.tipo_cucina "
                     + "FROM Ristoranti r JOIN ProposteCulinarie p ON r.id = p.ristorante JOIN ServiziOfferti s ON r.id = s.ristorante "
                     + "WHERE r.nome LIKE(?) "
                     + "ORDER BY r.id";
        
        ArrayList<Ristorante> ristoranti = new ArrayList<>();
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, "%" + nomeRistorante + "%");
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                int idCorrente = -1;
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    
                    if(id == idCorrente){
                        String servizio = resultSet.getString("servizio");
                        if(!ristoranti.getLast().getServizi().contains(servizio)){
                            ristoranti.getLast().getServizi().add(servizio);
                        }
                        String tipoCucina = resultSet.getString("tipo_cucina");
                        if(!ristoranti.getLast().getTipiCucina().contains(tipoCucina)){
                            ristoranti.getLast().getTipiCucina().add(tipoCucina);
                        }
                    }
                    else{
                        int proprietario = resultSet.getInt("proprietario");
                        String nome = resultSet.getString("nome");
                        String indirizzo = resultSet.getString("indirizzo");
                        String citta = resultSet.getString("citta");
                        String nazione = resultSet.getString("nazione");
                        String fasciaPrezzo = resultSet.getString("fascia_prezzo");
                        double latitudine = resultSet.getDouble("latitudine");
                        double longitudine = resultSet.getDouble("longitudine");
                        String recapitoTelefonico = resultSet.getString("recapito_telefonico");
                        String paginaMichelin = resultSet.getString("pagina_michelin");
                        String paginaWeb = resultSet.getString("pagina_web");
                        String riconoscimento = resultSet.getString("riconoscimento");
                        boolean stellaGreen = resultSet.getBoolean("stella_green");
                        String descrizione = resultSet.getString("descrizione");
                        boolean prenotazioneOnline = resultSet.getBoolean("prenotazione_online");
                        boolean delivery = resultSet.getBoolean("delivery");
                        ArrayList<String> servizi = new ArrayList<>();
                        ArrayList<String> tipiCucina = new ArrayList<>();
                        try{
                            ristoranti.add(new Ristorante(id, proprietario, nome, indirizzo, citta, nazione, fasciaPrezzo, latitudine, longitudine, recapitoTelefonico, paginaMichelin, paginaWeb, riconoscimento, stellaGreen, descrizione, prenotazioneOnline, delivery, servizi, tipiCucina));
                        }
                        catch(InvalidCoordinatesException e){
                            throw(new RemoteException());
                        }
                        ristoranti.getLast().getServizi().add(resultSet.getString("servizio"));
                        ristoranti.getLast().getTipiCucina().add(resultSet.getString("tipo_cucina"));
                        idCorrente = id;
                    }
                }
            }
            catch(SQLException e){
                throw(new RemoteException());
            }
        }
        catch(SQLException e){
            throw(new RemoteException());
        }
        return(ristoranti);
    }

    @Override // -- FUNZIONA --
    public ArrayList<Ristorante> ricercaRistorantiPerLocalita(String _citta, String _nazione) throws RemoteException{
        String query = "SELECT r.id, r.proprietario, r.nome, r.indirizzo, r.citta, r.nazione, r.fascia_prezzo, r.latitudine, r.longitudine, r.recapito_telefonico, r.pagina_michelin, r.pagina_web, r.riconoscimento, r.stella_green, r.descrizione, r.prenotazione_online, r.delivery, s.servizio, p.tipo_cucina "
                     + "FROM Ristoranti r JOIN ProposteCulinarie p ON r.id = p.ristorante JOIN ServiziOfferti s ON r.id = s.ristorante "
                     + "WHERE r.citta = ? AND r.nazione = ? "
                     + "ORDER BY r.id";
        
        ArrayList<Ristorante> ristoranti = new ArrayList<>();
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, _citta);
            preparedStatement.setString(2, _nazione);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                int idCorrente = 0;
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    
                    if(id == idCorrente){
                        String servizio = resultSet.getString("servizio");
                        if(!ristoranti.getLast().getServizi().contains(servizio)){
                            ristoranti.getLast().getServizi().add(servizio);
                        }
                        String tipoCucina = resultSet.getString("tipo_cucina");
                        if(!ristoranti.getLast().getTipiCucina().contains(tipoCucina)){
                            ristoranti.getLast().getTipiCucina().add(tipoCucina);
                        }
                    }
                    else{
                        int proprietario = resultSet.getInt("proprietario");
                        String nome = resultSet.getString("nome");
                        String indirizzo = resultSet.getString("indirizzo");
                        String citta = resultSet.getString("citta");
                        String nazione = resultSet.getString("nazione");
                        String fasciaPrezzo = resultSet.getString("fascia_prezzo");
                        double latitudine = resultSet.getDouble("latitudine");
                        double longitudine = resultSet.getDouble("longitudine");
                        String recapitoTelefonico = resultSet.getString("recapito_telefonico");
                        String paginaMichelin = resultSet.getString("pagina_michelin");
                        String paginaWeb = resultSet.getString("pagina_web");
                        String riconoscimento = resultSet.getString("riconoscimento");
                        boolean stellaGreen = resultSet.getBoolean("stella_green");
                        String descrizione = resultSet.getString("descrizione");
                        boolean prenotazioneOnline = resultSet.getBoolean("prenotazione_online");
                        boolean delivery = resultSet.getBoolean("delivery");
                        ArrayList<String> servizi = new ArrayList<>();
                        ArrayList<String> tipiCucina = new ArrayList<>();
                        try{
                            ristoranti.add(new Ristorante(id, proprietario, nome, indirizzo, citta, nazione, fasciaPrezzo, latitudine, longitudine, recapitoTelefonico, paginaMichelin, paginaWeb, riconoscimento, stellaGreen, descrizione, prenotazioneOnline, delivery, servizi, tipiCucina));
                        }
                        catch(InvalidCoordinatesException e){
                            throw(new RemoteException());
                        }
                        ristoranti.getLast().getServizi().add(resultSet.getString("servizio"));
                        ristoranti.getLast().getTipiCucina().add(resultSet.getString("tipo_cucina"));
                        idCorrente = id;
                    }
                }
            }
            catch(SQLException e){
                throw(new RemoteException());
            }
        }
        catch(SQLException e){
            throw(new RemoteException());
        }   
        return(ristoranti);
    }

    @Override // -- FUNZIONA --
    public ArrayList<Ristorante> ricercaRistorantiPerFiltri(Filtro[] filtri) throws RemoteException{
        String query = "SELECT r.id, r.proprietario, r.nome, r.indirizzo, r.citta, r.nazione, r.fascia_prezzo, r.latitudine, r.longitudine, r.recapito_telefonico, r.pagina_michelin, r.pagina_web, r.riconoscimento, r.stella_green, r.descrizione, r.prenotazione_online, r.delivery, s.servizio, p.tipo_cucina "
                     + "FROM Ristoranti r JOIN ProposteCulinarie p ON r.id = p.ristorante JOIN ServiziOfferti s ON r.id = s.ristorante "
                     + "WHERE r.id IN (SELECT DISTINCT r2.id "
                     +                "FROM Ristoranti r2 "
                     +                "WHERE r2.nome LIKE(?) "
                     +                "AND r2.citta LIKE(?) "
                     +                "AND r2.nazione LIKE(?) "
                     +                "AND r2.riconoscimento LIKE(?) ";
        
        String filtroValutazioneMedia = "AND (SELECT AVG(re.valutazione) "
                                      +      "FROM Recensioni re "
                                      +      "WHERE re.ristorante = r2.id) >= ? ";
        
        String filtroFasciaPrezzo = "AND r2.fascia_prezzo = ? ";
        
        String filtroStellaGreen = "AND r2.stella_green = ? ";
        
        String filtroPrenotazioneOnline = "AND r2.prenotazione_online = ? ";
        
        String filtroServizioDelivery = "AND r2.delivery = ? ";
        
        String filtroTipoCucina = "AND EXISTS (SELECT 1 "
                                +             "FROM ProposteCulinarie p2 "
                                +             "WHERE p2.tipo_cucina LIKE(?) AND p2.ristorante = r2.id) ";
        
        String filtroServizio = "AND EXISTS (SELECT 1 "
                              +             "FROM ServiziOfferti s2 "
                              +             "WHERE s2.servizio LIKE(?) AND s2.ristorante = r2.id) ";
        
        String chiusuraQuery = ") ORDER BY r.id";
        
        ArrayList<Object> parametri = new ArrayList<>();
        parametri.add("%" + filtri[0].getValore().toString().trim() + "%"); // filtri[0].getValore è il filtro per il nome del ristorante, che non è mai null
        parametri.add("%" + filtri[1].getValore().toString().trim() + "%"); // filtri[1].getValore è il filtro per il nome della città del ristorante, che non è mai null
        parametri.add("%" + filtri[2].getValore().toString().trim() + "%"); // filtri[2].getValore è il filtro per il nome della nazione del ristorante, che non è mai null
        parametri.add("%" + filtri[3].getValore().toString().trim() + "%"); // filtri[3].getValore è il filtro per il riconoscimento del ristorante, che non è mai null
        
        if(filtri[4].getValore() != null && filtri[4].getValore() instanceof Double){ // filtri[4].getValore è il filtro per la valutazione media del ristorante
            query += filtroValutazioneMedia;
            parametri.add((Double)filtri[4].getValore());
        }
        
        if(filtri[5].getValore() != null && filtri[5].getValore() instanceof String){ // filtri[5].getValore è il filtro per la fascia di prezzo del ristorante
            query += filtroFasciaPrezzo;
            parametri.add(filtri[5].getValore().toString().trim());
        }
        
        if(Boolean.TRUE.equals(filtri[6].getValore())){ // filtri[6].getValore è il filtro per la stella green del ristorante
            query += filtroStellaGreen;
            parametri.add((Boolean)filtri[6].getValore());
        }
        
        if(Boolean.TRUE.equals(filtri[7].getValore())){ // filtri[7].getValore è il filtro per il servizio prenotazione online del ristorante
            query += filtroPrenotazioneOnline;
            parametri.add((Boolean)filtri[7].getValore());
        }
        
        if(Boolean.TRUE.equals(filtri[8].getValore())){ // filtri[8].getValore è il filtro per il servizio delivery del ristorante
            query += filtroServizioDelivery;
            parametri.add((Boolean)filtri[8].getValore());
        }
        
        if(filtri[9].getValore() != null && filtri[9].getValore() instanceof ArrayList<?>){ // filtri[9].getValore è il filtro per i tipi di cucina del ristorante
            ArrayList<String> tipiCucina = (ArrayList)filtri[9].getValore();
            for(int i = 0; i < tipiCucina.size(); i++){
                query += filtroTipoCucina;
                parametri.add("%" + tipiCucina.get(i).trim() + "%");
            }
        }
        
        if(filtri[10].getValore() != null && filtri[10].getValore() instanceof ArrayList<?>){ // filtri[10].getValore è il filtro per i servizi del ristorante
            ArrayList<String> servizi = (ArrayList)filtri[10].getValore();
            for(int i = 0; i < servizi.size(); i++){
                query += filtroServizio;
                parametri.add("%" + servizi.get(i).trim() + "%");
            }
        }
        
        query += chiusuraQuery;
        
        ArrayList<Ristorante> ristoranti = new ArrayList<>();
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i < parametri.size(); i++){
                if(parametri.get(i) instanceof String){
                    preparedStatement.setString(i + 1, (String)parametri.get(i));
                }
                else if(parametri.get(i) instanceof Boolean){
                    preparedStatement.setBoolean(i + 1, (Boolean)parametri.get(i));
                }
                else if(parametri.get(i) instanceof Double){
                    preparedStatement.setDouble(i + 1, (Double)parametri.get(i));
                }
            }
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                int idCorrente = -1;
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    
                    if(id == idCorrente){
                        String servizio = resultSet.getString("servizio");
                        if(!ristoranti.getLast().getServizi().contains(servizio)){
                            ristoranti.getLast().getServizi().add(servizio);
                        }
                        String tipoCucina = resultSet.getString("tipo_cucina");
                        if(!ristoranti.getLast().getTipiCucina().contains(tipoCucina)){
                            ristoranti.getLast().getTipiCucina().add(tipoCucina);
                        }
                    }
                    else{
                        int proprietario = resultSet.getInt("proprietario");
                        String nome = resultSet.getString("nome");
                        String indirizzo = resultSet.getString("indirizzo");
                        String citta = resultSet.getString("citta");
                        String nazione = resultSet.getString("nazione");
                        String fasciaPrezzo = resultSet.getString("fascia_prezzo");
                        double latitudine = resultSet.getDouble("latitudine");
                        double longitudine = resultSet.getDouble("longitudine");
                        String recapitoTelefonico = resultSet.getString("recapito_telefonico");
                        String paginaMichelin = resultSet.getString("pagina_michelin");
                        String paginaWeb = resultSet.getString("pagina_web");
                        String riconoscimento = resultSet.getString("riconoscimento");
                        boolean stellaGreen = resultSet.getBoolean("stella_green");
                        String descrizione = resultSet.getString("descrizione");
                        boolean prenotazioneOnline = resultSet.getBoolean("prenotazione_online");
                        boolean delivery = resultSet.getBoolean("delivery");
                        ArrayList<String> servizi = new ArrayList<>();
                        ArrayList<String> tipiCucina = new ArrayList<>();
                        try{
                            ristoranti.add(new Ristorante(id, proprietario, nome, indirizzo, citta, nazione, fasciaPrezzo, latitudine, longitudine, recapitoTelefonico, paginaMichelin, paginaWeb, riconoscimento, stellaGreen, descrizione, prenotazioneOnline, delivery, servizi, tipiCucina));
                        }
                        catch(InvalidCoordinatesException e){
                            throw(new RemoteException());
                        }
                        ristoranti.getLast().getServizi().add(resultSet.getString("servizio"));
                        ristoranti.getLast().getTipiCucina().add(resultSet.getString("tipo_cucina"));
                        idCorrente = id;
                    }
                }
            }
        }
        catch(SQLException e){
            throw(new RemoteException("Comunicazione con il database fallita"));
        }
        
        return(ristoranti);
    }

    @Override // -- FUNZIONA --
    public ArrayList<Ristorante> visualizzaRistoranti(Utente utente) throws RemoteException{
        String query = "SELECT r.id, r.proprietario, r.nome, r.indirizzo, r.citta, r.nazione, r.fascia_prezzo, r.latitudine, r.longitudine, r.recapito_telefonico, r.pagina_michelin, r.pagina_web, r.riconoscimento, r.stella_green, r.descrizione, r.prenotazione_online, r.delivery, s.servizio, p.tipo_cucina "
                     + "FROM Ristoranti r JOIN ProposteCulinarie p ON r.id = p.ristorante JOIN ServiziOfferti s ON r.id = s.ristorante "
                     + "WHERE r.proprietario = ? "
                     + "ORDER BY r.id";
        
        ArrayList<Ristorante> ristoranti = new ArrayList<>();
        
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, utente.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                int idCorrente = 0;
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    
                    if(id == idCorrente){
                        String servizio = resultSet.getString("servizio");
                        if(!ristoranti.getLast().getServizi().contains(servizio)){
                            ristoranti.getLast().getServizi().add(servizio);
                        }
                        String tipoCucina = resultSet.getString("tipo_cucina");
                        if(!ristoranti.getLast().getTipiCucina().contains(tipoCucina)){
                            ristoranti.getLast().getTipiCucina().add(tipoCucina);
                        }
                    }
                    else{
                        int proprietario = resultSet.getInt("proprietario");
                        String nome = resultSet.getString("nome");
                        String indirizzo = resultSet.getString("indirizzo");
                        String citta = resultSet.getString("citta");
                        String nazione = resultSet.getString("nazione");
                        String fasciaPrezzo = resultSet.getString("fascia_prezzo");
                        double latitudine = resultSet.getDouble("latitudine");
                        double longitudine = resultSet.getDouble("longitudine");
                        String recapitoTelefonico = resultSet.getString("recapito_telefonico");
                        String paginaMichelin = resultSet.getString("pagina_michelin");
                        String paginaWeb = resultSet.getString("pagina_web");
                        String riconoscimento = resultSet.getString("riconoscimento");
                        boolean stellaGreen = resultSet.getBoolean("stella_green");
                        String descrizione = resultSet.getString("descrizione");
                        boolean prenotazioneOnline = resultSet.getBoolean("prenotazione_online");
                        boolean delivery = resultSet.getBoolean("delivery");
                        ArrayList<String> servizi = new ArrayList<>();
                        ArrayList<String> tipiCucina = new ArrayList<>();
                        try{
                            ristoranti.add(new Ristorante(id, proprietario, nome, indirizzo, citta, nazione, fasciaPrezzo, latitudine, longitudine, recapitoTelefonico, paginaMichelin, paginaWeb, riconoscimento, stellaGreen, descrizione, prenotazioneOnline, delivery, servizi, tipiCucina));
                        }
                        catch(InvalidCoordinatesException e){
                            throw(new RemoteException());
                        }
                        ristoranti.getLast().getServizi().add(resultSet.getString("servizio"));
                        ristoranti.getLast().getTipiCucina().add(resultSet.getString("tipo_cucina"));
                        idCorrente = id;
                    }
                }
            }
            
        }
        catch(SQLException e){
            throw(new RemoteException("Errore"));
        }
        return(ristoranti);
    }

    @Override
    public ArrayList<Recensione> visualizzaRecensioniPerRistorante(Ristorante ristorante) throws RemoteException{
        String query = "SELECT re.id AS id_recensione, u.username AS autore_recensione, re.valutazione, re.testo AS testo_recensione, re.ultima_modifica AS ultima_modifica_recensione, ri.id AS id_risposta, ri.autore AS autore_risposta, ri.testo AS testo_risposta, ri.ultima_modifica AS ultima_modifica_risposta " +
                       "FROM Recensioni re JOIN Utenti u ON re.autore = u.id LEFT JOIN Risposte ri ON re.id = ri.recensione " +
                       "WHERE re.ristorante = ? " +
                       "ORDER BY re.ultima_modifica DESC";

        ArrayList<Recensione> recensioni = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, ristorante.getId());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){

                    int idRecensione = resultSet.getInt("id_recensione");
                    String autore = resultSet.getString("autore_recensione"); // ora è username ✔️
                    int valutazione = resultSet.getInt("valutazione");
                    String testoRecensione = resultSet.getString("testo_recensione");
                    LocalDate ultimaModificaRecensione = resultSet.getDate("ultima_modifica_recensione").toLocalDate();

                    Risposta risposta = null;
                    int idRisposta = resultSet.getInt("id_risposta");

                    if(idRisposta != 0){
                        String autoreRisposta = resultSet.getString("autore_risposta");
                        String testoRisposta = resultSet.getString("testo_risposta");
                        LocalDate ultimaModificaRisposta = resultSet.getDate("ultima_modifica_risposta").toLocalDate();
                        
                        try{
                            risposta = new Risposta(idRisposta, autoreRisposta, testoRisposta, ultimaModificaRisposta);
                        }
                        catch(TextTooLongException e){
                            // TMCH
                        }
                    }

                    try{
                        recensioni.add(new Recensione(idRecensione, autore, valutazione, testoRecensione, ultimaModificaRecensione, risposta));
                    }
                    catch(InvalidRatingException | TextTooLongException e){
                        // TMCH
                    }
                }
            }
        }
        catch(SQLException e){
            throw(new RemoteException("Comunicazione col DB fallita"));
        }

        return(recensioni);
    }

    @Override // -- FUNZIONA --
    public void apriRistorante(Ristorante ristorante) throws RemoteException, InvalidCoordinatesException{
        String ristoranteQuery = "INSERT INTO Ristoranti(proprietario, nome, indirizzo, citta, nazione, fascia_prezzo, "
                               + "latitudine, longitudine, recapito_telefonico, pagina_michelin, pagina_web, riconoscimento, "
                               + "stella_green, descrizione, prenotazione_online, delivery) "
                               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String serviziQuery = "INSERT INTO ServiziOfferti (ristorante, servizio) VALUES (?, ?)";

        String proposteCulinarieQuery = "INSERT INTO ProposteCulinarie(ristorante, tipo_cucina) VALUES (?, ?)";

        Connection connection = null;
        try{
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);

            PreparedStatement ristorantePS = connection.prepareStatement(ristoranteQuery, Statement.RETURN_GENERATED_KEYS);

            PreparedStatement serviziPS = connection.prepareStatement(serviziQuery);

            PreparedStatement cucinePS = connection.prepareStatement(proposteCulinarieQuery);

            // --- INSERT RISTORANTE ---
            ristorantePS.setInt(1, ristorante.getProprietario());
            ristorantePS.setString(2, ristorante.getNome());
            ristorantePS.setString(3, ristorante.getIndirizzo());
            ristorantePS.setString(4, ristorante.getCitta());
            ristorantePS.setString(5, ristorante.getNazione());
            ristorantePS.setString(6, ristorante.getFasciaPrezzo());
            ristorantePS.setDouble(7, ristorante.getLatitudine());
            ristorantePS.setDouble(8, ristorante.getLongitudine());
            ristorantePS.setString(9, ristorante.getRecapitoTelefonico());
            ristorantePS.setString(10, ristorante.getPaginaMichelin());
            ristorantePS.setString(11, ristorante.getPaginaWeb());
            ristorantePS.setString(12, ristorante.getRiconoscimento());
            ristorantePS.setBoolean(13, ristorante.getStellaGreen());
            ristorantePS.setString(14, ristorante.getDescrizione());
            ristorantePS.setBoolean(15, ristorante.getPrenotazioneOnline());
            ristorantePS.setBoolean(16, ristorante.getDelivery());

            int affectedRows = ristorantePS.executeUpdate();

            if(affectedRows == 0){
                throw(new SQLException("Inserimento ristorante fallito"));
            }

            ResultSet keys = ristorantePS.getGeneratedKeys();
            int idRistorante;

            if(keys.next()){
                idRistorante = keys.getInt(1);
            }
            else{
                throw(new SQLException("ID ristorante non generato"));
            }

            // --- SERVIZI ---
            for(String s : ristorante.getServizi()){
                serviziPS.setInt(1, idRistorante);
                serviziPS.setString(2, s);
                serviziPS.addBatch();
            }
            serviziPS.executeBatch();

            // --- CUCINE ---
            for(String c : ristorante.getTipiCucina()){
                cucinePS.setInt(1, idRistorante);
                cucinePS.setString(2, c);
                cucinePS.addBatch();
            }
            cucinePS.executeBatch();

            connection.commit();

            ristorantePS.close();
            serviziPS.close();
            cucinePS.close();

        }
        catch(Exception e){
            if(connection != null){
                try{
                    connection.rollback();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw(new RemoteException("Errore apertura del ristorante"));
        }
        finally{
            if(connection != null){
                try{
                    connection.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // -- FUNZIONA --
    public String ristoranteInfo(Ristorante ristorante) throws RemoteException{
        return(ristorante.toString());
    }
}
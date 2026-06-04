package a_a.theknife.common.interfaces;

import java.rmi.*;

import java.util.ArrayList;

// import a_a.theknife.common.models.Descrittore;
import a_a.theknife.common.models.Utente;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Ristorante;

import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
import a_a.theknife.common.exceptions.AnswerNotExistsException;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidPasswordException;

public interface GestoreUtentiInterface extends Remote{
    // -- METODI IN COMUNE A CLIENTI E RISTORATORI --
    public void registraUtente(Utente utente) throws RemoteException, InvalidUsernameException, InvalidEMailException, InvalidPasswordException;
    
    public void login(Utente utente) throws RemoteException;
    
    public void logout() throws RemoteException;
    
    public void modificaUsername(String username) throws RemoteException, InvalidUsernameException;
    
    public void modificaPassword(String password) throws RemoteException, InvalidPasswordException;
    
    public void eliminaAccount() throws RemoteException;
    
    // -- METODI SOLO DEI CLIENTI --
    
    // per le recensioni
    public ArrayList<Recensione> visualizzaRecensioni() throws RemoteException;
    
    public void scriviRecensione() throws RemoteException;
    
    public void modificaRecensione() throws RemoteException;
    
    public void eliminaRecensione() throws RemoteException;
    
    // per i preferiti (ristoranti)
    public ArrayList<Ristorante> visualizzaPreferiti() throws RemoteException;
    
    public void aggiungiPreferito() throws RemoteException;
    
    public void rimuoviPreferito() throws RemoteException;
    
    public ArrayList<Ristorante> ricercaRistorantiPerNome(String nomeRistorante) throws RemoteException;
    
    public ArrayList<Ristorante> ricercaRistorantiPerLocalita(String citta) throws RemoteException;
    
    public ArrayList<Ristorante> ricercaRistorantiPerFiltri(ArrayList<String> filtri) throws RemoteException;
    
    // -- METODI SOLO DEI RISTORATORI --
    public ArrayList<Ristorante> visualizzaRistoranti() throws RemoteException;
    
    public ArrayList<Recensione> visualizzaRecensioniPerRistorante() throws RemoteException;
    
    public void scriviRisposta() throws RemoteException, AnswerAlreadyExistsException;
    
    public void modificaRisposta() throws RemoteException, AnswerNotExistsException;
}
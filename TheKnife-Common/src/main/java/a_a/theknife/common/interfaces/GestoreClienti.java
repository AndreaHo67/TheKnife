package a_a.theknife.common.interfaces;

import java.rmi.RemoteException;

import java.util.ArrayList;

import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;


public interface GestoreClienti{
    /**
     * Il metodo ritorna un elenco dei ristoranti preferiti per l'utente specificato.
     * @param utente l'utente per cui si cercano i preferiti
     * @return un elenco dei ristoranti preferiti per l'utente ricevuto come parametro
     * @throws RemoteException 
     */
    public ArrayList<Ristorante> visualizzaPreferiti(Utente utente) throws RemoteException;
    
    /**
     * Il metodo aggiunge un ristorante all'elenco dei ristoranti preferiti per
     * l'utente specificato.
     * @param utente l'utente a cui aggiungere un preferito
     * @param ristorante il ristorante da aggiungere ai preferiti
     * @throws RemoteException 
     */
    public void aggiungiPreferito(Utente utente, Ristorante ristorante) throws RemoteException;
    
    /**
     * Il metodo rimuove il ristorante ricevuto in input dalla lista dei preferiti
     * dell'utente specificato
     * @param utente l'utente a cui togliere un preferito
     * @param ristorante il ristorante da rimuovere dai preferiti
     * @throws RemoteException 
     */
    public void rimuoviPreferito(Utente utente, Ristorante ristorante) throws RemoteException;
}
package a_a.theknife.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import a_a.theknife.common.models.Utente;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidPasswordException;

public interface GestoreUtenti extends Remote{
    /**
     * Il metodo modifica il campo username dell'utente ricevuto come parametro,
     * garantendone l'unicità. Qualora il nuovo username fosse già presente viene
     * sollevata la <code>InvalidzUsernameException</code>
     * @param utente l'utente a cui modificare lo username
     * @param username il nuovo username
     * @throws RemoteException
     * @throws InvalidUsernameException 
     */
    public void modificaUsername(Utente utente, String username) throws RemoteException, InvalidUsernameException;
    
    /**
     * Il metodo effettua il cambio password per l'utente specificato, controllando
     * che la password soddisfi i requisiti di sicurezza.
     * Se la nuova password non li soddisfa, o se qualcosa va storto in fase di
     * hashing della password viene sollevata la <code>InvalidPasswordException</code>.
     * @param utente l'utente a cui cambiare la password
     * @param password la nuova password
     * @throws RemoteException
     * @throws InvalidPasswordException 
     */
    public void modificaPassword(Utente utente, String password) throws RemoteException, InvalidPasswordException;
    
    /**
     * Il metodo restituisce la lista dei ristoranti salvati tra i preferiti dall'utente.
     * @param utente l'utente di cui si vogliono visualizzare i preferiti
     * @return ArrayList contenente i ristoranti preferiti
     * @throws RemoteException 
     */
    public ArrayList<Ristorante> visualizzaPreferiti(Utente utente) throws RemoteException;
    
    /**
     * Il metodo aggiunge un ristorante alla lista dei preferiti dell'utente.
     * @param utente l'utente che effettua l'aggiunta ai suoi preferiti
     * @param ristorante il ristorante da aggiungere ai preferiti
     * @throws RemoteException 
     */
    public void aggiungiPreferito(Utente utente, Ristorante ristorante) throws RemoteException;
    
    /**
     * Il metodo rimuove un ristorante dalla lista dei preferiti dell'utente.
     * @param utente l'utente che effettua la rimozione di un ristorante dai suoi prefefriti
     * @param ristorante il ristorante da rimuovere
     * @throws RemoteException 
     */
    public void rimuoviPreferito(Utente utente, Ristorante ristorante) throws RemoteException;
}

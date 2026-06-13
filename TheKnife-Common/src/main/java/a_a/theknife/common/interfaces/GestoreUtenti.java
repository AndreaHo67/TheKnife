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
     * sollevata la <code>InvalidUsernameException</code>
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
    
    
}
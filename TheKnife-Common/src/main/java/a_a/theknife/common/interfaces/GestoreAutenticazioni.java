package a_a.theknife.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;

public interface GestoreAutenticazioni extends Remote{
    /**
     * Il metodo controlla che in database non sia presente un utente con lo stesso
     * username e/o la stessa email. Se non è presente lo aggiunge, se invece
     * è presente viene sollevata l'eccezione <code>InvalidUsernameException</code>
     * o la <code>InvalidEMailException</code>.
     * @param utente l'utente da registrare
     * @throws RemoteException
     * @throws InvalidUsernameException
     * @throws InvalidEMailException 
     */
    public void registraUtente(Utente utente) throws RemoteException, InvalidUsernameException, InvalidEMailException;
    
    /**
     * Se non è già attiva una sessione per l'utente specificato, il metodo apre
     * una sessione per l'utente riportando l'informazione presso il database.
     * Se invece è già attiva una sessione per l'utente specificato, il metodo
     * solleva la <code>InvalidSessionRequestException</code>.
     * @param utente
     * @throws RemoteException 
     * @throws <code>InvalidSessionRequestException</code>
     */
    
    /* Ho modificato il comportamento del "riportare l'informazione presso il db". Nello script di creazione delle tabelle del db, 
    non esiste alcuna colonna (es. is_logged_in) per tracciare lo stato della sessione. Modificare continuamente il db per tracciare il login
    non è ottimale, in quanto introduce colli di bottigli in operazioni I/O.
    
    Ho utilizzato l'infrastruttura del server (la memoria "RAM" del server tramite HashSet) come "db in memoria" per le sessioni attive sulla piattaforma.
    */
    public void login(Utente utente) throws RemoteException, InvalidSessionRequestException;
    
    /**
     * Il metodo effettua il logout per l'utente specificato, riportando tale
     * informazione nel database. Qualora per l'utente passato come parametro non
     * sia già attiva una sessione, il metodo solleva una <code>InvalidSessionRequestException</code>.
     * @param utente
     * @throws RemoteException 
     * @throws <code>InvalidSessionRequestException</code>
     */
    
    /*
    Dato che nello script sql è stato impostato ON DELETE CASCADE su ogni chiave esterna delle tabelle del db.
    Pertanto, eliminando il record padre nella tabella Utenti, dal database verranno eliminate a cascata tutti i record correlati (ristoranti, recensioni, preferiti)
    */
    public void logout(Utente utente) throws RemoteException, InvalidSessionRequestException;
    
    /**
     * Elimina TUTTE le informazioni relative all'utente, inclusi i ristoranti,
     * le risposte lasciate alle recensioni e le recensioni ricevute ai ristoranti
     * (in caso di utente-ristoratatore), le recensioni ed i preferiti
     * (in caso di utente-cliente).
     * @param utente l'utente il cui account viene eliminato
     * @throws RemoteException 
     */
    public void eliminaAccount(Utente utente) throws RemoteException;
}

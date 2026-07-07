package a_a.theknife.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;

public interface GestoreRecensioni extends Remote{
    /**
     * Il metodo restituisce un elenco di tutte le recensioni scritte dall'utente
     * passato come parametro.
     * @param utente
     * @return un elenco delle recensioni scritte dall'utente.
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Recensione> visualizzaRecensioni(Utente utente) throws RemoteException;
    
    /**
     * Il metodo restituisce un elenco di tutte le recensioni lasciate al
     * ristorante passato come parametro.
     * @param ristorante
     * @return un elenco di recensioni lasciate al ristorante passato come parametro
     */
    // public ArrayList<Recensione> visualizzaRecensioni(Ristorante ristorante);
    
    /**
     * Il metodo riceve in input un oggetto recensione e lo aggiunge al database.
     * I controlli di base di validazione della recensione (valutazione valida
     * e testo valido) sono delegati al costruttore della classe <code>Recensione</code>.
     * @param recensione
     * @throws <code>RemoteException</code>
     */
    public void scriviRecensione(Recensione recensione, int ristorante) throws RemoteException;

    /**
     * Il metodo riceve in input un oggetto recensione e lo elimina dal database.
     * @param recensione
     * @throws <code>RemoteException</code>
     */
    public void eliminaRecensione(Recensione recensione) throws RemoteException;

    /**
     * Il metodo riceve in input un oggetto recensione e un oggetto risposta, quindi
     * controlla se nel database è presente una recensione identica. Se è presente
     * e ad essa non è associata nessuna risposta, tale risposta viene registrata.
     * Se nel database non è presente una recensione identica viene sollevata
     * l'eccezione <code>ReviewNotExistsException</code>.
     * Se alla recensione è già stata lasciata una risposta viene sollevata
     * l'eccezione <code>AnswerAlreadyExistsException</code>.
     * @param recensione
     * @param risposta
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewNotExistsException</code>
     * @throws <code>AnswerAlreadyExistsException</code>
     */
    public void scriviRispostaRecensione(Recensione recensione, Risposta risposta) throws RemoteException, ReviewNotExistsException, AnswerAlreadyExistsException;
}
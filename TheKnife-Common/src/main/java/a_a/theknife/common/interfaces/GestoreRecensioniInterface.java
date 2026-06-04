package a_a.theknife.common.interfaces;

import java.rmi.*;

import a_a.theknife.common.models.Recensione;

import a_a.theknife.common.exceptions.ReviewAlreadyExistsException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;

public interface GestoreRecensioniInterface extends Remote{
    /**
     * Il metodo riceve in input un oggetto recensione, controlla se nel database
     * è presente una recensione identica. Se non è presente viene registrata,
     * altrimenti viene sollevata l'eccezione <code>ReviewAlreadyExistsException</code>.
     * @param recensione
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewAlreadyExistsException</code>
     */
    public void aggiungiRecensione(Recensione recensione) throws RemoteException, ReviewAlreadyExistsException;

    /**
     * Il metodo riceve in input un oggetto recensione, controlla se nel database
     * è presente una recensione identica. Se è presente viene eliminata,
     * altrimenti viene sollevata l'eccezione <code>ReviewNotExistsException</code>.
     * @param recensione
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewNotExistsException</code>
     */
    public void eliminaRecensione(Recensione recensione) throws RemoteException, ReviewNotExistsException;

    /**
     * Il metodo riceve in input un oggetto recensione e una risposta, quindi
     * controlla se nel database è presente una recensione identica. Se è presente
     * e ad essa non è associata nessuna risposta, e se la risposta passata come
     * parametro è inferiore ai 200 caratteri, tale risposta viene registrata.
     * Se nel database non è presente una recensione identica viene sollevata
     * l'eccezione <code>ReviewNotExistsException</code>.
     * Se alla recensione è già stata lasciata una risposta viene sollevata
     * l'eccezione <code>AnswerAlreadyExistsException</code>.
     * Se invece la recensione in questione esiste e non ha una risposta,
     * se la lunghezza della risposta supera i 200 caratteri (spazi inclusi)
     * viene sollevata l'eccezione <code>TextTooLongException</code>.
     * @param recensione
     * @param risposta
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewNotExistsException</code>
     * @throws <code>AnswerAlreadyExistsException</code>
     * @throws <code>TextTooLongException</code>
     */
    public void scriviRispostaRecensione(Recensione recensione, String risposta) throws RemoteException, ReviewNotExistsException, AnswerAlreadyExistsException, TextTooLongException;
}
package a_a.theknife.common.interfaces;

import java.rmi.*;

import a_a.theknife.common.models.Recensione;

import a_a.theknife.common.exceptions.ReviewAlreadyExistsException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.exceptions.InvalidRatingException;

public interface GestoreRecensioni extends Remote{
    /**
     * Il metodo riceve in input un oggetto recensione, controlla se nel database
     * è presente una recensione identica. Se non è presente viene registrata,
     * altrimenti viene sollevata l'eccezione <code>ReviewAlreadyExistsException</code>.
     * @param recensione 
     * @throws <code>RemoteException</code>
     * @throws <code>a_a.theknife.common.exceptions.ReviewAlreadyExistsException</code>
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
     * Il metodo riceve in input un oggetto recensione, controlla se nel database
     * è presente una recensione identica. Se è presente e se la lunghezza del
     * nuovo testo è minore di 200 caratteri la modifica viene apportata.
     * Se nel database non è presente una recensione identica viene sollevata
     * l'eccezione <code>ReviewNotExistsException</code>.
     * Se il nuovo testo supera i 200 caratteri (spazi inclusi) viene sollevata
     * l'eccezione <code>TextTooLongException</code>.
     * @param recensione
     * @param testo
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewNotExistsException<code> 
     * @throws <code>TextTooLongException</code> 
     */
    public void modificaTestoRecensione(Recensione recensione, String testo) throws RemoteException, ReviewNotExistsException, TextTooLongException;
    
    /**
     * Il metodo riceve in input un oggetto recensione e una nuova valutazione,
     * controlla se nel database è presente una recensione identica. Se è presente
     * viene verificato se il valore della nuova valutazione è valido, e se lo è
     * la modifica viene apportata. Una valutazione si ritiene valida se è compresa
     * fra 0 e 5, con valutazione intere o decimale cifra tonda (es. 3.5 è valida, 3.7 no).
     * Se nel database non è presente una recensione identica viene sollevata
     * l'eccezione <code>ReviewNotExistsException</code>.
     * Se la nuova valutazione non è valida viene sollevata l'eccezione
     * <code>InvalidRatingException</code>.
     * @param recensione
     * @param valutazione
     * @throws <code>RemoteException</code>
     * @throws <code>ReviewNotExistsException</code>
     * @throws <code>InvalidRatingException</code>
     */
    public void modificaValutazioneRecensione(Recensione recensione, float valutazione) throws RemoteException, ReviewNotExistsException, InvalidRatingException;
}

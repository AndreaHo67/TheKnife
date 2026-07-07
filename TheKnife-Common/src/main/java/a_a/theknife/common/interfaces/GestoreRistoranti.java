package a_a.theknife.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Utente;
import a_a.theknife.common.models.Filtro;

import a_a.theknife.common.exceptions.InvalidCoordinatesException;
import a_a.theknife.common.exceptions.NoReviewsException;

public interface GestoreRistoranti extends Remote{
    /**
     * Restituisce il numero di ristoranti salvati in database
     * @return un intero che indica il numero di ristoranti registrati sulla piattaforma
     * @throws <code>RemoteException</code>
     */
    public int numeroRistorantiRegistrati() throws RemoteException;
    
    /**
     * Il metodo calcola la media di tutte le valutazioni delle recensioni di un ristorante.
     * Se un ristorante non ha recensioni viene sollevata l'eccezione <code>NoReviewsException</code>.
     * @param ristorante
     * @return
     * @throws <code>RemoteException</code>
     * @throws <code>NoReviewsException</code>
     */
    public double calcolaValutazioneMedia(Ristorante ristorante) throws RemoteException, NoReviewsException;
    
    /**
     * Il metodo ritorna un elenco di ristoranti il cui nome contiene il nome ricevuto
     * come parametro (case unsensitive).
     * @param nomeRistorante il nome usato come filtro di ricerca
     * @return un elenco di ristoranti il cui nome contiene il nome ricevuto come parametro
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Ristorante> ricercaRistorantiPerNome(String nomeRistorante) throws RemoteException;
    
    /**
     * Il metodo ritorna un elenco di ristoranti che si trovano nella città e nella
     * nazione specificate.
     * @param citta la città a cui appartiene il ristorante.
     * @param nazione la nazione a cui appartiene il ristorante.
     * @return
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Ristorante> ricercaRistorantiPerLocalita(String citta, String nazione) throws RemoteException; // per utenti guest
    
    /**
     * Il metodo restituisce un elenco di ristoranti che soddisfano tutti i filtri
     * di ricerca ricevuti in input
     * @param filtri un elenco di filtri
     * @return un elenco di ristoranti che soddisfano tutti i filtri specificati
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Ristorante> ricercaRistorantiPerFiltri(Filtro[] filtri) throws RemoteException;
    
    /**
     * Restituisce un elenco di ristoranti che sono gestiti dall'utente passato
     * come parametro.
     * @param utente l'utente gestore dei ristoranti
     * @return un elenco di ristoranti gestiti dall'utente proprietario
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Ristorante> visualizzaRistoranti(Utente utente) throws RemoteException;
    
    /**
     * Restituisce un elenco di recensioni, tutte lasciate al ristorante passato
     * come parametro.
     * @param ristorante il ristorante di cui cercare le recensioni
     * @return un elenco di recensioni lasciate al ristorante passato come parametro
     * @throws <code>RemoteException</code>
     */
    public ArrayList<Recensione> visualizzaRecensioniPerRistorante(Ristorante ristorante) throws RemoteException;
    
    public void apriRistorante(Ristorante ristorante) throws RemoteException, InvalidCoordinatesException;
    
    /**
     * Il metodo incapsula il metodo toString() della classe <code>Ristorante</code>.
     * @param ristorante
     * @return
     * @throws <code>RemoteException</code>
     */
    public String ristoranteInfo(Ristorante ristorante) throws RemoteException;
}
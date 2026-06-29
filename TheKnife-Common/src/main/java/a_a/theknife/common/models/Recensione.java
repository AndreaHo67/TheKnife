package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.TextTooLongException;

public class Recensione implements Serializable{
    private static final int LUNGHEZZA_MASSIMA_TESTO = 200;
    
    private int id;
    private String autore;
    private int valutazione;
    private String testo;
    private LocalDate ultimaModifica;
    private Risposta risposta;
    
    /**
     * Costruttore incompleto della classe <code>Recensione</code>; costruisce un
     * oggetto recensione privo di id e privo di risposta.
     * Questo costruttore viene utilizzato per creare nuove recensioni, inizialmente
     * prive di risposta, e il cui id viene assegnato dal database, in modo seriale.
     * @param autore
     * @param valutazione
     * @param testo
     * @throws <code>InvalidRatingException</code>
     * @throws <code>TextTooLongException</code>
     */
    public Recensione(String autore, int valutazione, String testo) throws InvalidRatingException, TextTooLongException{
        if(valutazione < 1 || valutazione > 5){
            throw(new InvalidRatingException());
        }
        if(testo.length() > LUNGHEZZA_MASSIMA_TESTO){
            throw(new TextTooLongException());
        }
        this.autore = autore;
        this.valutazione = valutazione;
        this.testo = testo;
        this.ultimaModifica = LocalDate.now();
        this.risposta = null;
    }
    
    /**
     * Costruttore completo della classe <code>Recensione</code>; costruisce un
     * oggetto recensione inizializzando tutti i suoi campi, id e risposta inclusi.
     * Solo il campo risposta può essere nullo, qualora non esista una risposta
     * associata alla recensione.
     * Questo costruttore viene utilizzato per creare oggetti recensione leggendoli
     * dal database.
     * @param id
     * @param autore
     * @param valutazione
     * @param testo
     * @param data
     * @param risposta
     * @throws <code>InvalidRatingException</code>
     * @throws <code>TextTooLongException</code>
     */
    public Recensione(int id, String autore, int valutazione, String testo, LocalDate ultimaModifica, Risposta risposta)  throws InvalidRatingException, TextTooLongException{
        if(valutazione < 1 || valutazione > 5){
            throw(new InvalidRatingException());
        }
        if(testo.length() > LUNGHEZZA_MASSIMA_TESTO){
            throw(new TextTooLongException());
        }
        
        this.id = id;
        this.autore = autore;
        this.valutazione = valutazione;
        this.testo = testo;
        this.ultimaModifica = ultimaModifica;
        this.risposta = risposta;
    }
    
    public int getId(){
        return(this.id);
    }
    public void setId(int id){
        this.id = id;
    }
    
    public String getAutore(){
        return(this.autore);
    }
    public void setAutore(String autore){
        this.autore = autore;
    }
    
    public int getValutazione(){
        return(this.valutazione);
    }
    public void setValutazione(int valutazione){
        this.valutazione = valutazione;
    }
    
    public String getTesto(){
        return(this.testo);
    }
    public void setTesto(String testo){
        this.testo = testo;
    }
    
    public LocalDate getUltimaModifica(){
        return(this.ultimaModifica);
    }
    public void setUltimaModifica(LocalDate ultimaModifica){
        this.ultimaModifica = ultimaModifica;
    }
    
    public Risposta getRisposta(){
        return(this.risposta);
    }
    public void setRisposta(Risposta risposta){
        this.risposta = risposta;
    }
    
    @Override
    public String toString(){
        return("Recensione n°" + this.id + "\n" +
               "Valutazione " + this.valutazione + "\n" +
               this.testo + "\n" + 
               "Ultima modifica: " + this.ultimaModifica.toString() + "\n" +
               (this.risposta == null ? "Nessuna risposta" : this.risposta.toString()));
    }
}
package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.TextTooLongException;

public class Recensione implements Serializable{
    private int id;
    private float valutazione;
    private String testo;
    private LocalDate data;
    private Risposta risposta;
    
    /**
     * Costruttore incompleto della classe <code>Recensione</code>; costruisce un
     * oggetto recensione privo di id e privo di risposta.
     * Questo costruttore viene utilizzato per creare nuove recensioni, inizialmente
     * prive di risposta, e il cui id viene assegnato dal database, in modo seriale.
     * @param valutazione
     * @param testo
     * @throws <code>InvalidRatingException</code>
     * @throws <code>TextTooLongException</code>
     */
    public Recensione(float valutazione, String testo) throws InvalidRatingException, TextTooLongException{
        this.valutazione = valutazione;
        this.testo = testo;
        this.data = LocalDate.now();
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
     * @param valutazione
     * @param testo
     * @param data
     * @param risposta
     * @throws <code>InvalidRatingException</code>
     * @throws <code>TextTooLongException</code>
     */
    public Recensione(int id, float valutazione, String testo, LocalDate data, Risposta risposta) throws InvalidRatingException, TextTooLongException{
        this.id = id;
        this.valutazione = valutazione;
        this.testo = testo;
        this.data = data;
        this.risposta = risposta;
    }
    
    public int getId(){
        return(this.id);
    }
    public void setId(int id){
        this.id = id;
    }
    
    public float getValutazione(){
        return(this.valutazione);
    }
    public void setValutazione(float valutazione){
        this.valutazione = valutazione;
    }
    
    public String getTesto(){
        return(this.testo);
    }
    public void setTesto(String testo){
        this.testo = testo;
    }
    
    public LocalDate getData(){
        return(this.data);
    }
    public void setData(LocalDate data){
        this.data = data;
    }
    
    @Override
    public String toString(){
        return("Recensione n°" + this.id + "\n" +
               "Valutazione " + this.valutazione + "\n" +
               this.testo + "\n" + 
               "Ultima modifica: " + this.data.toString());

    }
}
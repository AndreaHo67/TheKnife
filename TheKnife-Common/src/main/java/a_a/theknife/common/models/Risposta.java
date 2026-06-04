package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

public class Risposta implements Serializable{
    private int id;
    private String testo;
    private LocalDate data;
    
    /**
     * Costruttore incompleto della classe <code>Risposta</code>; costruisce un
     * oggetto risposta privo di id.
     * Questo costruttore viene usato per creare nuove risposte, il cui id viene
     * assegnato dal database in modo seriale.
     * @param testo 
     */
    public Risposta(String testo){
        this.testo = testo;
        this.data = LocalDate.now();
    }
    
    /**
     * Costruttore completo della classe <code>Risposta</code>; costruisce un
     * oggetto risposta inizializzando tutti i suoi campi, id incluso.
     * Questo costruttore viene usato per creare oggetti risposta leggendoli
     * dal database.
     * @param id
     * @param testo 
     */
    public Risposta(int id, String testo){
        this.id = id;
        this.testo = testo;
        this.data = LocalDate.now();
    }
    
    public int getId(){
        return(this.id);
    }
    public void setId(int id){
        this.id = id;
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
    
    /**
     * Il metodo restituisce una sintetica ed esaustiva descrizione dell'oggetto
     * risposta.
     * @return Una stringa contenente le informazioni relative all'oggetto risposta.
     */
    @Override
    public String toString(){
        return("Risposta n°" + this.id + "\n" +
               "Lasciata il " + this.data.toString() + "\n" +
               this.getTesto());
    }
}
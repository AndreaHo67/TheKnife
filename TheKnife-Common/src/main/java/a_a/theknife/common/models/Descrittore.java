package a_a.theknife.common.models;

import java.io.Serializable;

public class Descrittore implements Serializable{
    private Utente utenteAttivo;
    private Ristorante ristoranteSelezionato;
    private Recensione recensioneSelezionata;
    private Risposta rispostaSelezionata;
    
    public Descrittore(Utente utenteAttivo){
        this.utenteAttivo = utenteAttivo;
        this.ristoranteSelezionato = null;
        this.recensioneSelezionata = null;
        this.rispostaSelezionata = null;
    }

    public Utente getUtenteAttivo(){
        return(this.utenteAttivo);
    }
    public void setUtenteAttivo(Utente utenteAttivo){
        this.utenteAttivo = utenteAttivo;
    }

    public Ristorante getRistoranteSelezionato(){
        return(this.ristoranteSelezionato);
    }
    public void setRistoranteSelezionato(Ristorante ristoranteSelezionato){
        this.ristoranteSelezionato = ristoranteSelezionato;
    }

    public Recensione getRecensioneSelezionata(){
        return(this.recensioneSelezionata);
    }
    public void setRecensioneSelezionata(Recensione recensioneSelezionata){
        this.recensioneSelezionata = recensioneSelezionata;
    }

    public Risposta getRispostaSelezionata(){
        return(this.rispostaSelezionata);
    }
    public void setRispostaSelezionata(Risposta rispostaSelezionata){
        this.rispostaSelezionata = rispostaSelezionata;
    }
}
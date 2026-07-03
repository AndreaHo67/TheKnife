package a_a.theknife.client.utils;

import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

public class Sessione{
    private static Sessione sessione;
    
    private Utente utenteLoggato;
    private Ristorante ristoranteSelezionato;
    private Recensione recensioneSelezionata;
    private Risposta rispostaSelezionata;
    
    private Sessione(){
        this.utenteLoggato = null;
        this.ristoranteSelezionato = null;
        this.recensioneSelezionata = null;
        this.rispostaSelezionata = null;
    }
    
    public static Sessione getSessione(){
        if(sessione == null){
            sessione = new Sessione();
        }
        return(sessione);
    }
    
    public Utente getUtenteLoggato(){
        return(this.utenteLoggato);
    }
    public void setUtenteLoggato(Utente utenteLoggato){
        this.utenteLoggato = utenteLoggato;
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
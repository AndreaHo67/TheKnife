package a_a.theknife.common.models;

import java.io.Serializable;

public class Ristorante implements Serializable{
    private int id;
    private String nome;
    private String indirizzo;
    private String citta;
    private String nazione;
    private String fasciaPrezzo;
    private double latitudine;
    private double longitudine;
    private String recapitoTelefonico;
    private String paginaMichelin;
    private String paginaWeb;
    private String riconoscimento;
    private boolean stellaGreen;
    private String descrizione;
    private boolean prenotazioneOnline;
    private boolean delivery;
    private String[] servizi;
    private String[] tipiCucina;

    public Ristorante(int id, String nome, String indirizzo, String citta, String nazione, String fasciaPrezzo, double latitudine, double longitudine, String recapitoTelefonico, String paginaMichelin, String paginaWeb, String riconoscimento, boolean stellaGreen, String descrizione, boolean prenotazioneOnline, boolean delivery, String[] servizi, String[] tipiCucina){
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.nazione = nazione;
        this.fasciaPrezzo = fasciaPrezzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.recapitoTelefonico = recapitoTelefonico;
        this.paginaMichelin = paginaMichelin;
        this.paginaWeb = paginaWeb;
        this.riconoscimento = riconoscimento;
        this.stellaGreen = stellaGreen;
        this.descrizione = descrizione;
        this.prenotazioneOnline = prenotazioneOnline;
        this.delivery = delivery;
        this.servizi = servizi;
        this.tipiCucina = tipiCucina;
    }

    public int getId(){
        return(this.id);
    }
    public void setId(int id){
        this.id = id;
    }
    
    public String getNome(){
        return(this.nome);
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public String getIndirizzo(){
        return(this.indirizzo);
    }
    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }
    
    public String getCitta(){
        return(this.citta);
    }
    public void setCitta(String citta){
        this.citta = citta;
    }
    
    public String getNazione(){
        return(this.nazione);
    }
    public void setNazione(String nazione){
        this.nazione = nazione;
    }
    
    public String getFasciaPrezzo(){
        return(this.fasciaPrezzo);
    }
    public void setFasciaPrezzo(String fasciaPrezzo){
        this.fasciaPrezzo = fasciaPrezzo;
    }
    
    public double getLatitudine(){
        return(this.latitudine);
    }
    public void setLatitudine(double latitudine){
        this.latitudine = latitudine;
    }

    public double getLongitudine(){
        return(this.longitudine);
    }
    public void setLongitudine(double longitudine){
        this.longitudine = longitudine;
    }

    public String getRecapitoTelefonico(){
        return(this.recapitoTelefonico);
    }
    public void setRecapitoTelefonico(String recapitoTelefonico){
        this.recapitoTelefonico = recapitoTelefonico;
    }

    public String getPaginaMichelin(){
        return(this.paginaMichelin);
    }
    public void setPaginaMichelin(String paginaMichelin){
        this.paginaMichelin = paginaMichelin;
    }

    public String getPaginaWeb(){
        return(this.paginaWeb);
    }
    public void setPaginaWeb(String paginaWeb){
        this.paginaWeb = paginaWeb;
    }

    public String getRiconoscimento(){
        return(this.riconoscimento);
    }
    public void setRiconoscimento(String riconoscimento){
        this.riconoscimento = riconoscimento;
    }

    public boolean getStellaGreen(){
        return(this.stellaGreen);
    }
    public void setStellaGreen(boolean stellaGreen){
        this.stellaGreen = stellaGreen;
    }

    public String getDescrizione(){
        return(this.descrizione);
    }
    public void setDescrizione(String descrizione){
        this.descrizione = descrizione;
    }

    public boolean getPrenotazioneOnline(){
        return(this.prenotazioneOnline);
    }
    public void setPrenotazioneOnline(boolean prenotazioneOnline){
        this.prenotazioneOnline = prenotazioneOnline;
    }

    public boolean getDelivery(){
        return(this.delivery);
    }
    public void setDelivery(boolean delivery){
        this.delivery = delivery;
    }

    public String[] getServizi(){
        return(this.servizi);
    }
    public void setServizi(String[] servizi){
        this.servizi = servizi;
    }

    public String[] getTipiCucina(){
        return(this.tipiCucina);
    }
    public void setTipiCucina(String[] tipiCucina){
        this.tipiCucina = tipiCucina;
    }
}
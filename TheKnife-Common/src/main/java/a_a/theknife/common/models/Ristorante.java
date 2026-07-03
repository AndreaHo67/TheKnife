package a_a.theknife.common.models;

import java.io.Serializable;

import java.util.ArrayList;

import a_a.theknife.common.exceptions.InvalidCoordinatesException;
import a_a.theknife.common.exceptions.NoMenuException;
import a_a.theknife.common.exceptions.NoServicesException;

public class Ristorante implements Serializable{
    private int id;
    private int proprietario;
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
    private ArrayList<String> servizi;
    private ArrayList<String> tipiCucina;

    /**
     * Costruttore incompleto della classe <code>Ristorante</code>; costruisce
     * un oggetto ristorante privo di id.
     * Questo costruttore viene utilizzato per creare nuovi ristoranti, il cui id
     * viene assegnato dal database, in modo seriale.
     * @param proprietario
     * @param nome
     * @param indirizzo
     * @param citta
     * @param nazione
     * @param fasciaPrezzo
     * @param latitudine
     * @param longitudine
     * @param recapitoTelefonico
     * @param paginaMichelin
     * @param paginaWeb
     * @param riconoscimento
     * @param stellaGreen
     * @param descrizione
     * @param prenotazioneOnline
     * @param delivery
     * @param servizi
     * @param tipiCucina 
     * @throws <code>InvalidCoordinatesException</code>
     */
    public Ristorante(int proprietario, String nome, String indirizzo, String citta, String nazione, String fasciaPrezzo, double latitudine, double longitudine, String recapitoTelefonico, String paginaMichelin, String paginaWeb, String riconoscimento, boolean stellaGreen, String descrizione, boolean prenotazioneOnline, boolean delivery, ArrayList<String> servizi, ArrayList<String> tipiCucina) throws InvalidCoordinatesException, NoMenuException, NoServicesException{
        if(latitudine > 90 || latitudine < -90 || longitudine > 180 || longitudine < -180){
            throw(new InvalidCoordinatesException());
        }
        if(tipiCucina.isEmpty()){
            throw(new NoMenuException());
        }
        if(servizi.isEmpty()){
            throw(new NoServicesException());
        }
        this.nome = nome;
        this.proprietario = proprietario;
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
    
    /**
     * Costruttore completo della classe <code>Ristorante</code>; costruisce un
     * oggetto ristorante inizializzando tutti i suoi campi, id incluso.
     * Questo costruttore viene utilizzato per creare oggetti ristorante legggendoli
     * dal database.
     * @param id
     * @param proprietario
     * @param nome
     * @param indirizzo
     * @param citta
     * @param nazione
     * @param fasciaPrezzo
     * @param latitudine
     * @param longitudine
     * @param recapitoTelefonico
     * @param paginaMichelin
     * @param paginaWeb
     * @param riconoscimento
     * @param stellaGreen
     * @param descrizione
     * @param prenotazioneOnline
     * @param delivery
     * @param servizi
     * @param tipiCucina 
     * @throws <code>InvalidCoordinatesException</code>
     */
    public Ristorante(int id, int proprietario, String nome, String indirizzo, String citta, String nazione, String fasciaPrezzo, double latitudine, double longitudine, String recapitoTelefonico, String paginaMichelin, String paginaWeb, String riconoscimento, boolean stellaGreen, String descrizione, boolean prenotazioneOnline, boolean delivery, ArrayList<String> servizi, ArrayList<String> tipiCucina) throws InvalidCoordinatesException{
        if(latitudine > 90 || latitudine < -90 || longitudine > 180 || longitudine < -180){
            throw(new InvalidCoordinatesException());
        }
        this.id = id;
        this.proprietario = proprietario;
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
    
    public int getProprietario(){
        return(this.proprietario);
    }
    public void setProprietario(int proprietario){
        this.proprietario = proprietario;
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

    public ArrayList<String> getServizi(){
        return(this.servizi);
    }
    public void setServizi(ArrayList<String> servizi){
        this.servizi = servizi;
    }

    public ArrayList<String> getTipiCucina(){
        return(this.tipiCucina);
    }
    public void setTipiCucina(ArrayList<String> tipiCucina){
        this.tipiCucina = tipiCucina;
    }
    
    @Override
    public String toString(){
        String _servizi = "";
        for(int i = 0; i < this.servizi.size(); i++){
            _servizi += (this.servizi.get(i) + ", ");
        }
        _servizi = _servizi.substring(0, _servizi.length() - 2);
        
        String _tipiCucina = "";
        for(int i = 0; i < this.tipiCucina.size(); i++){
            _tipiCucina += (this.tipiCucina.get(i) + ", ");
        }
        _tipiCucina = _tipiCucina.substring(0, _tipiCucina.length() - 2);
        return("Ristorante n°" + this.id + "\n" +
               this.indirizzo + "\n" +
               this.nome + ", " + this.citta + ", " + this.nazione + "\n" +
               this.fasciaPrezzo + "\n" +
               "lat. " + this.latitudine + ", long. " + this.longitudine + "\n" +
               "Recapito telefonico: " + this.recapitoTelefonico + "\n" +
               "Link utili: " + this.paginaMichelin + ", " + this.paginaWeb + "\n" +
               "Ristorante riconosciuto come: " + this.riconoscimento + "\n" +
               "Stella green: " + (this.stellaGreen == true ? "si" : "no") + "\n" +
               this.descrizione + "\n" +
               "Propone tipi di cucina di " + _tipiCucina + "\n" +
               "Servizi offerti: " + _servizi + "\n" +
               "Prenotazioni online: " + (this.prenotazioneOnline == true ? "si" : "no") + "\n" +
               "Servizio delivery: " + (this.delivery == true ? "si" : "no"));
    }
}
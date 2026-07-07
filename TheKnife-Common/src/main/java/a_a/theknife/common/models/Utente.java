package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidEMailException;

public class Utente implements Serializable{
    private int id;
    private String nome;
    private String cognome;
    private String username;
    private String eMail;
    private String password;
    private LocalDate dataNascita;
    private String citta;
    private String nazione;
    private String ruolo;

    /**
     * Costruttore incompleto della classe <code>Utente</code>; costruisce un
     * oggetto utente privo del campo id.
     * Questo costruttore viene utilizzato per creare nuovi utenti in fase di
     * registrazione, il cui id viene assegnato dal database in modo seriale.
     * @param nome
     * @param cognome
     * @param username
     * @param eMail
     * @param password
     * @param dataNascita
     * @param citta
     * @param nazione
     * @throws <code>InvalidEMailException</code>
     * @throws <code>InvalidPasswordException</code>
     */
    public Utente(String nome, String cognome, String username, String eMail, String password, LocalDate dataNascita, String citta, String nazione, String ruolo) throws InvalidEMailException, InvalidPasswordException{
        if(!eMail.contains("@")){
            throw(new InvalidEMailException());
        }
        
        if(!GestorePassword.validate(password)){
            throw(new InvalidPasswordException());
        }
        String hashedPassword = GestorePassword.hashPassword(password);
        
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.eMail = eMail;
        this.password = hashedPassword;
        this.dataNascita = dataNascita;
        this.citta = citta;
        this.nazione = nazione;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore completo della classe <code>Utente</code>; costruisce un
     * oggetto utente inizializzando tutti i suoi campi, id incluso.
     * Questo costruttore viene utilizzato per creare oggetti utente leggendoli
     * dal database.
     * @param id
     * @param nome
     * @param cognome
     * @param username
     * @param eMail
     * @param password
     * @param dataNascita
     * @param citta
     * @param nazione
     * @param ruolo
     */
    public Utente(int id, String nome, String cognome, String username, String eMail, String password, LocalDate dataNascita, String citta, String nazione, String ruolo){
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.eMail = eMail;
        this.password = password;
        this.dataNascita = dataNascita;
        this.citta = citta;
        this.nazione = nazione;
        this.ruolo = ruolo;
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

    public String getCognome(){
        return(this.cognome);
    }
    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public String getUsername(){
        return(this.username);
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getEMail(){
        return(this.eMail);
    }
    public void setEMail(String eMail){
        this.eMail = eMail;
    }

    public String getPassword(){
        return(this.password);
    }
    public void setPassword(String password) throws InvalidPasswordException{
        if(GestorePassword.validate(password)){
            this.password = GestorePassword.hashPassword(password);
        }
        else{
            throw(new InvalidPasswordException());
        }
    }

    public LocalDate getDataNascita(){
        return(this.dataNascita);
    }
    public void setDataNascita(LocalDate dataNascita){
        this.dataNascita = dataNascita;
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
    
    public String getRuolo(){
        return(this.ruolo);
    }
    public void setRuolo(String ruolo){
        this.ruolo = ruolo;
    }
}
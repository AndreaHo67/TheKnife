package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

import a_a.theknife.common.exceptions.InvalidUsernameException;
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
     * @throws <code>InvalidUsernameException</code>
     * @throws <code>InvalidEMailException</code>
     * @throws <code>InvalidPasswordException</code>
     */
    public Utente(String nome, String cognome, String username, String eMail, String password, LocalDate dataNascita, String citta, String nazione){
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.eMail = eMail;
        
        Password pw;
        try{
            pw = new Password(password);
        }
        catch(InvalidPasswordException e){
            return;
        }
        try{
            this.password = Password.hashPassword(pw.getPassword());
        }
        catch(Exception e){
            return;
        }
        
        this.dataNascita = dataNascita;
        this.citta = citta;
        this.nazione = nazione;
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
    public void setPassword(String password) throws InvalidPasswordException, Exception{
        try{
            this.password = Password.hashPassword(new Password(password).getPassword());
        }
        catch(InvalidPasswordException e){
            return;
        }
        catch(Exception e){
            return;
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
}
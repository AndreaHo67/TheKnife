package a_a.theknife.common.models;

import java.io.Serializable;

import java.time.LocalDate;

import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidPasswordException;

public class Cliente extends Utente implements Serializable{
    public Cliente(String nome, String cognome, String username, String eMail, String password, LocalDate dataNascita, String citta, String nazione) throws InvalidUsernameException, InvalidEMailException, InvalidPasswordException{
        super(nome, cognome, username, eMail, password, dataNascita, citta, nazione);
    }
}
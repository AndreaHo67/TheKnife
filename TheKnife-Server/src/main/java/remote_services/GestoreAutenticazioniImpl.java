package remote_services;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.exceptions.InvalidUsernameException;


public class GestoreAutenticazioniImpl extends UnicastRemoteObject implements GestoreAutenticazioni{
    public GestoreAutenticazioniImpl() throws RemoteException{
    }
    
    @Override
    public void registraUtente(Utente utente) throws RemoteException, InvalidUsernameException, InvalidEMailException{
    }

    @Override
    public void login(Utente utente) throws RemoteException, InvalidSessionRequestException{
    }

    @Override
    public void logout() throws RemoteException, InvalidSessionRequestException{
    }

    @Override
    public void eliminaAccount(Utente utente) throws RemoteException{
    }
}
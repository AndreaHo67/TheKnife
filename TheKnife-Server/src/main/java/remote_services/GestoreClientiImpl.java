package remote_services;

import a_a.theknife.common.interfaces.GestoreClienti;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.exceptions.InvalidUsernameException;


public class GestoreClientiImpl extends UnicastRemoteObject implements GestoreClienti{
    public GestoreClientiImpl() throws RemoteException{
    }
    
    @Override
    public ArrayList<Ristorante> visualizzaPreferiti(Utente utente) throws RemoteException{
    }

    @Override
    public void aggiungiPreferito(Utente utente, Ristorante ristorante) throws RemoteException{
    }

    @Override
    public void rimuoviPreferito(Utente utente, Ristorante ristorante) throws RemoteException{
    }
}

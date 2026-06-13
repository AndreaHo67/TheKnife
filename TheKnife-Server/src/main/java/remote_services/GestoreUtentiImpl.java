package remote_services;

import a_a.theknife.common.interfaces.GestoreUtenti;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidUsernameException;

public class GestoreUtentiImpl extends UnicastRemoteObject implements GestoreUtenti{
    public GestoreUtentiImpl() throws RemoteException{
    }

    @Override
    public void modificaUsername(Utente utente, String username) throws RemoteException, InvalidUsernameException{
    }

    @Override
    public void modificaPassword(Utente utente, String password) throws RemoteException, InvalidPasswordException{
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
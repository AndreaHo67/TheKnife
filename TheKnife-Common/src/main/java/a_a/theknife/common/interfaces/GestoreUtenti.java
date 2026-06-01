package a_a.theknife.common.interfaces;

import java.rmi.*;

import a_a.theknife.common.models.Utente;

public interface GestoreUtenti extends Remote{
    public void registraUtente(Utente utente) throws RemoteException;
    
    public void login(Utente utente) throws RemoteException;
    
    public void logout(Utente utente) throws RemoteException;
    
    
}
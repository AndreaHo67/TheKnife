package a_a.theknife.common.interfaces;

import java.rmi.*;

import a_a.theknife.common.models.Ristorante;

public interface GestoreRistoranti extends Remote{
    public int numeroRistorantiRegistrati() throws RemoteException;
    
    public Ristorante[] cercaRistorantiPerNome() throws RemoteException;
}
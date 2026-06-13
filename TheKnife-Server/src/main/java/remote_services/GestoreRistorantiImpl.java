package remote_services;

import a_a.theknife.common.interfaces.GestoreRistoranti;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Utente;

public class GestoreRistorantiImpl extends UnicastRemoteObject implements GestoreRistoranti{
    public GestoreRistorantiImpl() throws RemoteException{
    }
    
    @Override
    public int numeroRistorantiRegistrati() throws RemoteException{
    }

    @Override
    public double calcolaValutazioneMedia(Ristorante ristorante) throws RemoteException{
    }

    @Override
    public ArrayList<Ristorante> ricercaRistorantiPerNome(String nomeRistorante) throws RemoteException{
    }

    @Override
    public ArrayList<Ristorante> ricercaRistorantiPerLocalita(String citta, String nazione) throws RemoteException{
    }

    @Override
    public ArrayList<Ristorante> ricercaRistorantiPerFiltri(ArrayList<String> filtri) throws RemoteException{
    }

    @Override
    public ArrayList<Ristorante> visualizzaRistoranti(Utente utente) throws RemoteException{
    }

    @Override
    public ArrayList<Recensione> visualizzaRecensioniPerRistorante(Ristorante ristorante) throws RemoteException{
    }
}
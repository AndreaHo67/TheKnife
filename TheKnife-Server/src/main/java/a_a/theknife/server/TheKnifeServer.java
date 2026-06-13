package a_a.theknife.server;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.interfaces.GestoreUtenti;

import remote_services.GestoreAutenticazioniImpl;
import remote_services.GestoreRecensioniImpl;
import remote_services.GestoreRistorantiImpl;
import remote_services.GestoreUtentiImpl;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import a_a.theknife.common.interfaces.GestoreRecensioni;

public class TheKnifeServer{
    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.createRegistry(1099);
        
        GestoreAutenticazioni gestoreAutenticazioni = new GestoreAutenticazioniImpl();
        GestoreRecensioni gestoreRecensioni = new GestoreRecensioniImpl();
        GestoreRistoranti gestoreRistoranti = new GestoreRistorantiImpl();
        GestoreUtenti gestoreUtenti = new GestoreUtentiImpl();
        
        registry.bind("GestoreAutenticazioni", gestoreAutenticazioni);
        registry.bind("GestoreRecensioni", gestoreRecensioni);
        registry.bind("GestoreRistoranti", gestoreRistoranti);
        registry.bind("GestoreUtenti", gestoreUtenti);
    }
}
package a_a.theknife.server;



import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.interfaces.GestoreUtenti;
import a_a.theknife.server.remote_services.GestoreAutenticazioniImpl;
import a_a.theknife.server.remote_services.GestoreRecensioniImpl;
import a_a.theknife.server.remote_services.GestoreRistorantiImpl;
import a_a.theknife.server.remote_services.GestoreUtentiImpl;

import java.rmi.registry.*;

public class TheKnifeServer{
    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.createRegistry(1099);
        
        GestoreAutenticazioni gestoreAutenticazioni = new GestoreAutenticazioniImpl();
        GestoreRecensioni gestoreRecensioni = new GestoreRecensioniImpl();
        GestoreRistoranti gestoreRistoranti = new GestoreRistorantiImpl();
        GestoreUtenti gestoreUtenti = new GestoreUtentiImpl();
        
        registry.rebind("gestoreAutenticazioni", gestoreAutenticazioni);
        registry.rebind("gestoreRecensioni", gestoreRecensioni);
        registry.rebind("gestoreRistoranti", gestoreRistoranti);
        registry.rebind("gestoreUtenti", gestoreUtenti);
        System.out.println("Server pronto!");
    }
}
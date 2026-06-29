package a_a.theknife.server;



import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.server.remote_services.GestoreRecensioniImpl;
import a_a.theknife.server.remote_services.GestoreRistorantiImpl;

import java.rmi.registry.*;

public class TheKnifeServer{
    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.createRegistry(1099);
        
        GestoreRecensioni gestoreRecensioni = new GestoreRecensioniImpl();
        GestoreRistoranti gestoreRistoranti = new GestoreRistorantiImpl();
        
        registry.rebind("gestoreRecensioni", gestoreRecensioni);
        registry.rebind("gestoreRistoranti", gestoreRistoranti);
        System.out.println("Server pronto!");
    }
}
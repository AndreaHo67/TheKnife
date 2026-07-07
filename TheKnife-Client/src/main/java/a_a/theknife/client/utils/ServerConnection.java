package a_a.theknife.client.utils;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.interfaces.GestoreUtenti;

import java.rmi.registry.*;

public class ServerConnection{
    private static ServerConnection serverConnection;
    
    private GestoreAutenticazioni gestoreAutenticazioni;
    private GestoreRecensioni gestoreRecensioni;
    private GestoreRistoranti gestoreRistoranti;
    private GestoreUtenti gestoreUtenti;
    
    private ServerConnection() throws Exception{
        Registry registry = LocateRegistry.getRegistry(1099);
        
        this.gestoreAutenticazioni = (GestoreAutenticazioni) registry.lookup("gestoreAutenticazioni");
        this.gestoreRecensioni = (GestoreRecensioni) registry.lookup("gestoreRecensioni");
        this.gestoreRistoranti = (GestoreRistoranti) registry.lookup("gestoreRistoranti");
        this.gestoreUtenti = (GestoreUtenti) registry.lookup("gestoreUtenti");
    }
    
    public static synchronized ServerConnection getServerConnection() throws Exception{
        if(serverConnection == null){
            serverConnection = new ServerConnection();
        }
        return(serverConnection);
    }
    
    public GestoreAutenticazioni getGestoreAutenticazioni(){
        return(this.gestoreAutenticazioni);
    }
    
    public GestoreRecensioni getGestoreRecensioni(){
        return(this.gestoreRecensioni);
    }
    
    public GestoreRistoranti getGestoreRistoranti(){
        return(this.gestoreRistoranti);
    }
    
    public GestoreUtenti getGestoreUtenti(){
        return(this.gestoreUtenti);
    }
}
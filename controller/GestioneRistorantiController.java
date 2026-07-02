package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;

/**
 *
 * @author ANDREA
 */
public class GestioneRistorantiController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreRecensioni gestoreRecensioni;
    private GestoreRistoranti gestoreRistoranti;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreRecensioni = this.serverConnection.getGestoreRecensioni();
        this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
    }
    
    
}
package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.interfaces.GestoreUtenti;
import java.io.IOException;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class ProfiloRistoratoreController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreAutenticazioni gestoreAutenticazioni;
    private GestoreUtenti gestoreUtenti;
    private GestoreRistoranti gestoreRistoranti;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
        this.gestoreUtenti = this.serverConnection.getGestoreUtenti();
        this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
    }
    
    // elementi fxml
    @FXML private Label lblNome;
    @FXML private Label lblCognome;
    @FXML private Label lblUsername;
    @FXML private Label lblMail;
    @FXML private Label lblCitta;
    @FXML private Label lblNazione;
    @FXML private Label lblNumeroRistoranti;
    @FXML private Label intestazione;
    @FXML private Button tornaHomeRistoratore;
    @FXML private Button btnUpdateUsername;
    @FXML private Button btnUpdatePassword;
    
    // bottone dinamico
    @FXML private Button bottoneDinamico;
    private String risorsa;
    
    @FXML
    public void initialize(){
    }
    
    public void setup(){
        this.lblNome.setText(this.sessione.getUtenteLoggato().getNome());
        this.lblCognome.setText(this.sessione.getUtenteLoggato().getCognome());
        this.lblUsername.setText(this.sessione.getUtenteLoggato().getUsername());
        this.lblMail.setText(this.sessione.getUtenteLoggato().getEMail());
        this.lblCitta.setText(this.sessione.getUtenteLoggato().getCitta());
        this.lblNazione.setText(this.sessione.getUtenteLoggato().getNazione());
        this.intestazione.setText("Profilo utente di " + this.sessione.getUtenteLoggato().getUsername());
        try{
            int ristorantiGestiti = this.gestoreRistoranti.visualizzaRistoranti(this.sessione.getUtenteLoggato()).size();
            
            if(ristorantiGestiti > 0){
                if(ristorantiGestiti == 1){
                    this.lblNumeroRistoranti.setText("Al momento gestisci 1 ristorante");
                }
                else{
                    this.lblNumeroRistoranti.setText("Al momento gestisci " + ristorantiGestiti + " ristoranti");
                }
                
                this.bottoneDinamico.setText("Gestisci i miei ristoranti");
                this.risorsa = "/gui/fxml/GestioneRistoranti.fxml";
            }
            else{
                this.lblNumeroRistoranti.setText("Al momento non gestisci alcun ristorante");
                
                this.bottoneDinamico.setText("Apri un nuovo ristorante");
                this.risorsa = "/gui/fxml/RegistraRistorante.fxml";
            }
        }
        catch(RemoteException e){
            /*
                Se non funziona il servizio, ossia se non riesco a contare
                i ristoranti di un utente, questa non è la pagina principale
                dove aprire o gestire un ristorante, quindi non viene resa
                disponibile la shortcut per la pagina corrispondente
            */
            this.lblNumeroRistoranti.setVisible(false);
            this.bottoneDinamico.setVisible(false);
        }
    }
    
    // gestione bottone dinamico
    @FXML
    private void gestisciBottoneDinamico(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(this.risorsa));
            Parent root = loader.load();

            if(this.risorsa.equals("/gui/fxml/GestioneRistoranti.fxml")){
                GestioneRistorantiController controller = loader.getController();
                controller.init(this.serverConnection, this.sessione);
                controller.setup();
            }
            else{
                RegistraRistoranteController controller = loader.getController();
                controller.init(this.serverConnection, this.sessione);
            }
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void tornaAHomeRistoratore(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomepageRistoratore.fxml"));
            Parent root = loader.load();

            HomepageRistoratoreController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void updatePassword(ActionEvent event){
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/CambioPassword.fxml"));
        Parent root = loader.load();
        CambioPasswordController controller = loader.getController();
        controller.init(this.serverConnection, this.sessione);

        Stage popup = new Stage();
        popup.setScene(new Scene(root));

        // Rende il popup modale
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        }
        catch(IOException e){
            error("Impossibile cambiare la password", "Riavviare l'applicazione");
        }
    }
    
    @FXML
    private void updateUsername(ActionEvent event){
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/CambioUsername.fxml"));
        Parent root = loader.load();
        CambioUsernameController controller = loader.getController();
        controller.init(this.serverConnection, this.sessione);

        Stage popup = new Stage();
        popup.setScene(new Scene(root));

        // Rende il popup modale
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        }
        catch(IOException e){
            error("Impossibile cambiare lo username", "Riavviare l'applicazione");
        }
    }
    
    /**
     * Popup di errore durante un'operazione.
     * @param titolo il titolo della finestra di errore
     * @param messaggio il contenuto del messaggio
     */
    private void error(String titolo, String messaggio){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
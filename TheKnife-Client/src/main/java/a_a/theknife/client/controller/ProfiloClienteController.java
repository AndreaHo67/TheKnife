package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
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

public class ProfiloClienteController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreAutenticazioni gestoreAutenticazioni;
    private GestoreUtenti gestoreUtenti;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
        this.gestoreUtenti = this.serverConnection.getGestoreUtenti();
    }
    
    // setup
    public void setup(){
        this.intestazione.setText(this.sessione.getUtenteLoggato().getUsername());
        this.lblNome.setText(this.sessione.getUtenteLoggato().getNome());
        this.lblCognome.setText(this.sessione.getUtenteLoggato().getCognome());
        this.lblUsername.setText(this.sessione.getUtenteLoggato().getUsername());
        this.lblMail.setText(this.sessione.getUtenteLoggato().getEMail());
        this.lblCitta.setText(this.sessione.getUtenteLoggato().getCitta());
        this.lblNazione.setText(this.sessione.getUtenteLoggato().getNazione());
        
    }
    
    // elementi grafici
    @FXML private Label intestazione;
    @FXML private Label lblNome;
    @FXML private Label lblCognome;
    @FXML private Label lblUsername;
    @FXML private Label lblMail;
    @FXML private Label lblCitta;
    @FXML private Label lblNazione;
    
    @FXML private Button btnLogout;
    @FXML private Button btnRecensioni;
    @FXML private Button btnPreferiti;
    @FXML private Button btnUpdateUsername;
    @FXML private Button btnUpdatePassword;
    @FXML private Button btnTornaHomeClienti;
    
    // metodi vari
    @FXML
    private void gestisciLogout(ActionEvent event){
        try{
            this.gestoreAutenticazioni.logout(this.sessione.getUtenteLoggato());
            this.sessione.setUtenteLoggato(null);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/Accesso.fxml"));
            Parent root = loader.load();

            AccessoController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        }
        catch(IOException e){
            error("Errore", e.getMessage());
        }
        catch(InvalidSessionRequestException e){
            error("Errore", e.getMessage());
        }
    }
    
    @FXML
    private void gestisciRecensioni(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/GestioneRecensioni.fxml"));
            Parent root = loader.load();

            GestioneRecensioniController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);
            controller.setup();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void gestisciPreferiti(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/GestionePreferiti.fxml"));
            Parent root = loader.load();

            GestionePreferitiController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);
            controller.setup();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
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
            this.setup();
        }
        catch(IOException e){
            error("Impossibile cambiare la password", "Riavviare l'applicazione");
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
    private void tornaAHome(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomepageCliente.fxml"));
            Parent root = loader.load();

            HomepageClienteController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);
            controller.setup();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
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
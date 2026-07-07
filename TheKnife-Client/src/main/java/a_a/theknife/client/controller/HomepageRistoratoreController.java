package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import java.io.IOException;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomepageRistoratoreController {
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreAutenticazioni gestoreAutenticazioni;
    
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
        this.lblBenvenuto.setText("Bentornato/a, " + this.sessione.getUtenteLoggato().getUsername());
    }
    
    // tasti per spostarsi nell'applicazione
    @FXML private Button btnNuovoRistorante;
    @FXML private Button btnGestisciRistoranti;
    @FXML private Button btnProfilo;
    @FXML private Button btnLogout;
    
    // label di benvenuto dinamica
    @FXML private Label lblBenvenuto;
    
    // Riquadri per l'animazione di hover
    @FXML private VBox cardGestisci;
    @FXML private VBox cardNuovo;
    
    @FXML
    private void initialize() {
        // Setup delle animazioni di hover per le card
        setupHoverAnimation(cardGestisci);
        setupHoverAnimation(cardNuovo);
    }
    
    private void setupHoverAnimation(VBox card) {
        if (card == null) return;
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), card);
        scaleIn.setToX(1.05);
        scaleIn.setToY(1.05);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), card);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        
        card.setOnMouseEntered(e -> {
            scaleOut.stop();
            scaleIn.playFromStart();
        });
        
        card.setOnMouseExited(e -> {
            scaleIn.stop();
            scaleOut.playFromStart();
        });
    }
    
    @FXML
    private void vaiAGestisciProfilo(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/ProfiloRistoratore.fxml"));
            Parent root = loader.load();

            ProfiloRistoratoreController controller = loader.getController();
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
    private void vaiARegistraRistorante(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/RegistraRistorante.fxml"));
            Parent root = loader.load();

            RegistraRistoranteController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);

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
    private void vaiAGestisciRistoranti(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/GestioneRistoranti.fxml"));
            Parent root = loader.load();

            GestioneRistorantiController controller = loader.getController();
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
    private void eseguiLogout(ActionEvent event){
        try{
            this.gestoreAutenticazioni.logout(this.sessione.getUtenteLoggato());
            this.sessione.setUtenteLoggato(null);
                    
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/Accesso.fxml"));
            Parent root = loader.load();

            AccessoController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace(); // modificare con un alert errore
        }
        catch(InvalidSessionRequestException e){
            e.printStackTrace(); // modificare con un alert errore
        }
    }
}
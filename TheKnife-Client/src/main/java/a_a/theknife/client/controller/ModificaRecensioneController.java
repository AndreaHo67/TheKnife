package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ModificaRecensioneController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreRecensioni gestoreRecensioni;
    private GestoreAutenticazioni gestoreAutenticazioni;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreRecensioni = this.serverConnection.getGestoreRecensioni();
        this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
    }

    // setup
    public void setup(){
        Recensione recensione = this.sessione.getRecensioneSelezionata();
        VBox card = creaCardRecensione(recensione);
        this.popup.getChildren().add(card);
    }
    
    // elementi grafici
    @FXML private VBox popup;
        
        
    private VBox creaCardRecensione(Recensione recensione){
        VBox card = new VBox(8);
        card.setStyle(
            "-fx-padding: 10;" +
            "-fx-border-color: #007161;" +
            "-fx-border-radius: 6;" +
            "-fx-background-color: #ffffff;"
        );

        // --- header ---
        HBox header = new HBox(10);

        HBox rating = new HBox(10);
        Label lblRating1 = new Label("★ ");
        TextField tFRating2 = new TextField("" + recensione.getValutazione());
        Label lblRating3 = new Label("/5");
        rating.getChildren().addAll(lblRating1, tFRating2, lblRating3);
        
        Label autore = new Label(recensione.getAutore());
        Label data = new Label(String.valueOf(LocalDate.now()));

        for(Label l : new Label[]{autore, data}){
            l.setStyle(
                "-fx-font-family: Georgia;" +
                "-fx-text-fill: #007161;" +
                "-fx-font-size: 12px;"
            );
        }
        rating.setStyle("-fx-font-family: Georgia; -fx-text-fill : #007161; -fx-font-size: 12px");

        header.getChildren().addAll(rating, autore, data);

        // --- testo recensione ---
        TextField testo = new TextField(recensione.getTesto());
        // testo.setWrapText(true);
        testo.setStyle("-fx-font-family: Georgia;");

        card.getChildren().addAll(header, testo);

        
        
        HBox pulsanti = new HBox(8);
            
        Button btnConferma = new Button("Conferma");
        btnConferma.setOnAction(event -> {
            try{
                Recensione nuovaRecensione = new Recensione(this.sessione.getUtenteLoggato().getUsername(), Integer.valueOf(tFRating2.getText()), testo.getText(), this.sessione.getRecensioneSelezionata().getRistorante());
                this.gestoreRecensioni.scriviRecensione(nuovaRecensione, recensione.getRistorante());
                this.gestoreRecensioni.eliminaRecensione(this.sessione.getRecensioneSelezionata());
                chiudiPopup(event);
            }
            catch(RemoteException e){
                error("Server irraggiungibile", e.getMessage());
            }
            catch(InvalidRatingException e){
                error("Valutazione non valida", "Una valutazione valida è un numero intero da 1 a 5");
            }
            catch(TextTooLongException e){
                error("Errore", "Il testo di una recensione non deve superare i 200 caratteri");
            }
        });
        
        btnConferma.setStyle(
            "-fx-background-color: #007161;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-family: Georgia;"
        );

        Button btnAnnulla = new Button("Annulla");
        btnAnnulla.setOnAction(event -> {
            chiudiPopup(event);
            this.setup();
        });
        
        btnAnnulla.setStyle(
            "-fx-background-color: #ff0f0f;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-family: Georgia;"
        );

        pulsanti.getChildren().addAll(btnConferma, btnAnnulla);

        card.getChildren().add(pulsanti);
        return(card);
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
    
    /**
     * Popup informativo dei dati di un'operazione.
     * @param titolo il titolo della finestra informativa
     * @param messaggio il contenuto del messaggio
     */
    private void info(String titolo, String messaggio){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    
    @FXML
    private void chiudiPopup(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
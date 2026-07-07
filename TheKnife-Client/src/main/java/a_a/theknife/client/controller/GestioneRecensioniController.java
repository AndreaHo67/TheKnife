package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneRecensioniController{
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
    
    private ArrayList<Recensione> recensioni;
    private HashMap<Integer, String> nomiRistoranti = new HashMap<>();
    
    // setup
    public void setup(){
        this.elencoRecensioni.getChildren().clear();
        try{
            // Precaricamento dei nomi dei ristoranti per visualizzarli nelle card
            for(Ristorante r : this.serverConnection.getGestoreRistoranti().ricercaRistorantiPerNome("")) {
                nomiRistoranti.put(r.getId(), r.getNome());
            }
            
            this.recensioni = this.gestoreRecensioni.visualizzaRecensioni(this.sessione.getUtenteLoggato());
            if(this.recensioni.isEmpty()){
                Label lblNoRecensioni = new Label("Al momento non hai lasciato nessuna recensione");
                this.elencoRecensioni.getChildren().add(lblNoRecensioni);
            }
            else{
                for(int i = 0; i < this.recensioni.size(); i++){
                    VBox card = creaCardRecensione(this.recensioni.get(i));
                    this.elencoRecensioni.getChildren().add(card);
                }
            }
        }
        catch(RemoteException e){
            error("Server irraggiungibile", e.getMessage());
        }
    }
    
    // elementi grafici
    @FXML private Button btnTornaAProfilo;
    @FXML private Button btnTornaAHome;
    
    @FXML private VBox elencoRecensioni;
    
    // metodi vari
    @FXML
    private void tornaAProfilo(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/ProfiloCliente.fxml"));
            Parent root = loader.load();

            ProfiloClienteController controller = loader.getController();
            controller.init(this.serverConnection, this.sessione);
            controller.setup();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
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
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
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
        
        String rName = nomiRistoranti.getOrDefault(recensione.getRistorante(), "Ristorante #" + recensione.getRistorante());
        Label ristoranteNome = new Label("A: " + rName);

        Label rating = new Label("★ " + recensione.getValutazione() + "/5");
        Label autore = new Label(recensione.getAutore());
        Label data = new Label(String.valueOf(recensione.getUltimaModifica()));

        for(Label l : new Label[]{ristoranteNome, rating, autore, data}){
            l.setStyle(
                "-fx-font-family: Georgia;" +
                "-fx-text-fill: #007161;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
            );
        }

        header.getChildren().addAll(ristoranteNome, rating, autore, data);

        // --- testo recensione ---
        Label testo = new Label(recensione.getTesto());
        testo.setWrapText(true);
        testo.setStyle("-fx-font-family: Georgia;");

        card.getChildren().addAll(header, testo);

        // --- risposta ---
        VBox rispostaBox = new VBox(4);

        if(recensione.getRisposta() != null){
            Risposta r = recensione.getRisposta();

            Label info = new Label(r.getAutore() + " ti ha risposto il " + r.getData());
            Label testoRisposta = new Label(r.getTesto());

            info.setStyle("-fx-font-family: Georgia; -fx-text-fill: #007161;");
            testoRisposta.setWrapText(true);
            testoRisposta.setStyle("-fx-font-family: Georgia;");

            rispostaBox.getChildren().addAll(info, testoRisposta);

            rispostaBox.setStyle(
                "-fx-padding: 8;" +
                "-fx-background-color: #f4f4f4;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 4;"
            );
        }
        else{
            Label msg = new Label("Questa recensione non ha avuto risposta");
            msg.setStyle("-fx-font-family: Georgia;");
            rispostaBox.getChildren().add(msg);
        }
        
        HBox pulsanti = new HBox(8);
            
        Button btnModifica = new Button("Modifica");
        btnModifica.setOnAction(event -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/ModificaRecensione.fxml"));
                Parent root = loader.load();
                ModificaRecensioneController controller = loader.getController();
                this.sessione.setRecensioneSelezionata(recensione);
                controller.init(this.serverConnection, this.sessione);
                controller.setup();

                Stage popup = new Stage();
                popup.setScene(new Scene(root));

                // Rende il popup modale
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.showAndWait();
                this.setup();
            }
            catch(IOException e){
                error("Impossibile modificare la recensione", "Riavviare l'applicazione");
                e.printStackTrace();
            }
        });
        
        btnModifica.setStyle(
            "-fx-background-color: #007161;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-family: Georgia;"
        );

        Button btnElimina = new Button("Elimina");
        btnElimina.setOnAction(event -> {
            try{
                this.gestoreRecensioni.eliminaRecensione(recensione);
                this.setup();
            }
            catch(RemoteException e){
                error("Server irraggiungibile", e.getMessage());
            }
        });
        
        btnElimina.setStyle(
            "-fx-background-color: #ff0f0f;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: Georgia;"
        );

        pulsanti.getChildren().addAll(btnModifica, btnElimina);

        card.getChildren().add(rispostaBox);
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
}
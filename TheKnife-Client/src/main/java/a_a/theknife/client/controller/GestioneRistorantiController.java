package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
import a_a.theknife.common.exceptions.NoReviewsException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.models.Ristorante;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    
    // elementi grafici
    @FXML private ScrollPane scrollLista;
    @FXML private VBox listaRistoranti;
    @FXML private Button tornaAHome;
    @FXML private ScrollPane infoScroll;
    @FXML private VBox infoRistorante;
    
    
    @FXML
    public void initialize(){
        this.infoScroll.setFitToWidth(true);
    }
    
    @FXML
    public void setup(){
        try{
            ArrayList<Ristorante> ristoranti = this.gestoreRistoranti.visualizzaRistoranti(this.sessione.getUtenteLoggato());
            if(ristoranti.isEmpty()){
                Label noRistoranti = new Label("Al momento non gestisci alcun ristorante");
                noRistoranti.setFont(Font.font("Georgia", 18));
                Button bottoneDinamico = new Button("Registra un nuovo ristorante");
                bottoneDinamico.setOnAction(event -> {
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
                    catch(IOException f){
                        error("Qualcosa è andato storto", "Riavviare l'applicazione");
                    }
                });
                VBox descrittore = new VBox(3);
                descrittore.getChildren().addAll(noRistoranti, bottoneDinamico);
                this.listaRistoranti.getChildren().add(descrittore);
            }
            else{
                for(int i = 0; i < ristoranti.size(); i++){
                    this.listaRistoranti.getChildren().add(this.creaElemento(ristoranti.get(i)));
                }
            }
        }
        catch(RemoteException e){
            error("Comunicazione con il server non riuscita", "Riavviare l'applicazione");
        }
    }
    
    @FXML
    private void tornaHome(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomepageRistoratore.fxml"));
            Parent root = loader.load();
            
            HomepageRistoratoreController controller = loader.getController();
            controller.init(serverConnection, sessione);
            
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
        catch(IOException e){
            error("Errore imprevisto", "Pagina non trovata, riavviare l'applicazione");
        }
    }
    
    private HBox creaElemento(Ristorante ristorante){

        HBox card = new HBox(12);
        card.setCursor(Cursor.HAND);
        card.setStyle(
            "-fx-border-color: #007161;" +
            "-fx-border-radius: 6;" +
            "-fx-background-color: white;" +
            "-fx-padding: 10;"
        );

        VBox left = new VBox(4);

        Label nome = new Label(ristorante.getNome());
        nome.setStyle("-fx-font-family: Georgia; -fx-font-size: 15px; -fx-text-fill: #007161;");

        Label descrizione = new Label(ristorante.getDescrizione());
        descrizione.setStyle("-fx-font-family: Georgia; -fx-font-size: 12px;");

        left.getChildren().addAll(nome, descrizione);

        // --- valutazione ---
        Label valLabel;
        try{
            double val = this.gestoreRistoranti.calcolaValutazioneMedia(ristorante);
            valLabel = new Label(val == 0.0 ? "N/D" : String.format("%.1f", val));
        }
        catch(Exception e){
            valLabel = new Label("N/D");
        }

        valLabel.setStyle(
            "-fx-font-family: Georgia;" +
            "-fx-text-fill: #007161;" +
            "-fx-font-size: 14px;"
        );

        ImageView star = new ImageView(new Image(getClass().getResource("/images/Star.png").toExternalForm()));
        star.setFitWidth(14);
        star.setFitHeight(14);
        star.setPreserveRatio(true);

        HBox ratingBox = new HBox(4, star, valLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(left, spacer, ratingBox);
        
        card.setOnMouseClicked(e -> {
            try{
                this.infoRistorante.getChildren().clear();

                Label titolo = new Label(ristorante.getNome());
                titolo.setStyle(
                    "-fx-font-family: Georgia;" +
                    "-fx-font-size: 18px;" +
                    "-fx-text-fill: #007161;"
                );

                VBox info = new VBox(6);
                info.setStyle("-fx-padding: 10;");

                Label indirizzo = new Label(
                    ristorante.getIndirizzo() + " - " +
                    ristorante.getCitta() + " - " +
                    ristorante.getNazione()
                );

                indirizzo.setStyle("-fx-font-family: Georgia;");

                Label descr = new Label(ristorante.getDescrizione());
                descr.setWrapText(true);
                descr.setStyle("-fx-font-family: Georgia;");

                info.getChildren().addAll(titolo, indirizzo, descr);

                this.infoRistorante.getChildren().add(info);

                for(Recensione r : this.gestoreRistoranti.visualizzaRecensioniPerRistorante(ristorante)){
                    this.infoRistorante.getChildren().add(creaCardRecensione(r));
                }

            }
            catch(RemoteException f){
                error("Errore server", "Impossibile caricare dati");
            }
        });
        return(card);
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

        Label rating = new Label("★ " + recensione.getValutazione() + "/5");
        Label autore = new Label(recensione.getAutore());
        Label data = new Label(String.valueOf(recensione.getUltimaModifica()));

        for(Label l : new Label[]{rating, autore, data}){
            l.setStyle(
                "-fx-font-family: Georgia;" +
                "-fx-text-fill: #007161;" +
                "-fx-font-size: 12px;"
            );
        }

        header.getChildren().addAll(rating, autore, data);

        // --- testo recensione ---
        Label testo = new Label(recensione.getTesto());
        testo.setWrapText(true);
        testo.setStyle("-fx-font-family: Georgia;");

        card.getChildren().addAll(header, testo);

        // --- risposta ---
        VBox rispostaBox = new VBox(4);

        if(recensione.getRisposta() != null){
            Risposta r = recensione.getRisposta();

            Label info = new Label("Hai risposto il " + r.getData());
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
            Label msg = new Label("Non hai ancora risposto a questa recensione");

            TextField input = new TextField();
            input.setPromptText("Scrivi la risposta...");

            Button btn = new Button("Rispondi");
            btn.setOnAction(event -> {
                if(input.getText().trim().length() > 200){
                    error("Risposta troppo lunga", "Una buona risposta deve essere concisa. Scrivine una sotto i 200 caratteri");
                    return;
                }
                else if(input.getText().trim().isBlank()){
                    error("Risposta vuota", "Non puoi rispondere in bianco");
                    return;
                }
                else{
                    try{
                        Risposta risposta = new Risposta(this.sessione.getUtenteLoggato().getUsername(), input.getText());
                        this.gestoreRecensioni.scriviRispostaRecensione(recensione, risposta);
                        info("Successo", "Risposta registrata correttamente. La pagina verrà riavviata");
                        
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
                    catch(TextTooLongException f){
                        // THMC, ho già impedito che venga sollevata;
                    }
                    catch(RemoteException f){
                        error("Server irraggiungibile", "Riavviare l'applicazione");
                    }
                    catch(ReviewNotExistsException f){
                        error("Recensione inesistente", "Non è stata rilevata nessuna recensione a cui attribuire la risposta. Contattare un tecnico");
                    }
                    catch(AnswerAlreadyExistsException f){
                        error("Risposta già registrata", "Non è possibile rispondere più di una volta alla stessa recensione");
                    }
                    catch(IOException f){
                        error("Impossibile ricaricare la pagina", "Riavviare l'applicazione");
                    }
                }
            });
            btn.setStyle(
                "-fx-background-color: #007161;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: Georgia;"
            );

            msg.setStyle("-fx-font-family: Georgia;");

            rispostaBox.getChildren().addAll(msg, input, btn);
        }

        card.getChildren().add(rispostaBox);

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
    package a_a.theknife.client.controller;

    import a_a.theknife.client.utils.ServerConnection;
    import a_a.theknife.client.utils.Sessione;
    import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
    import a_a.theknife.common.exceptions.NoReviewsException;
    import a_a.theknife.common.exceptions.ReviewNotExistsException;
    import a_a.theknife.common.exceptions.TextTooLongException;
    import a_a.theknife.common.interfaces.GestoreRecensioni;
    import a_a.theknife.common.interfaces.GestoreRistoranti;
    import a_a.theknife.common.interfaces.GestoreUtenti;
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
    import javafx.stage.Modality;
    import javafx.stage.Stage;

    /**
     *
     * @author ANDREA
     */
    public class GestionePreferitiController{
        // connessione al server e sessione
        private ServerConnection serverConnection;
        private Sessione sessione;

        // servizi remoti
        private GestoreRecensioni gestoreRecensioni;
        private GestoreRistoranti gestoreRistoranti;
        private GestoreUtenti gestoreUtenti;

        // init
        public void init(ServerConnection serverConnection, Sessione sessione){
            this.serverConnection = serverConnection;
            this.sessione = sessione;
            this.gestoreRecensioni = this.serverConnection.getGestoreRecensioni();
            this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
            this.gestoreUtenti = this.serverConnection.getGestoreUtenti();
        }

        // elementi grafici
        @FXML private ScrollPane scrollLista;
        @FXML private VBox listaPreferiti;
        @FXML private Button tornaAHome;
        @FXML private ScrollPane infoScroll;
        @FXML private VBox infoPreferito;


        @FXML
        public void initialize(){
            this.infoScroll.setFitToWidth(true);
        }

        @FXML
        public void setup(){
            try{
                this.listaPreferiti.getChildren().clear();
                this.infoPreferito.getChildren().clear();
                ArrayList<Ristorante> preferiti = this.gestoreUtenti.visualizzaPreferiti(this.sessione.getUtenteLoggato());
                if(preferiti.isEmpty()){
                    Label noPreferiti = new Label("Al momento non hai salvato alcun preferito");
                    noPreferiti.setFont(Font.font("Georgia", 18));
                    Button bottoneDinamico = new Button("Torna a cercare preferiti");
                    bottoneDinamico.setOnAction(event -> {
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
                        catch(IOException f){
                            error("Qualcosa è andato storto", "Riavviare l'applicazione");
                        }
                    });
                    VBox descrittore = new VBox(3);
                    descrittore.getChildren().addAll(noPreferiti, bottoneDinamico);
                    this.listaPreferiti.getChildren().add(descrittore);
                }
                else{
                    for(int i = 0; i < preferiti.size(); i++){
                        this.listaPreferiti.getChildren().add(this.creaElemento(preferiti.get(i)));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/ProfiloCliente.fxml"));
                Parent root = loader.load();

                ProfiloClienteController controller = loader.getController();
                controller.init(serverConnection, sessione);
                controller.setup();

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

        private HBox creaElemento(Ristorante preferito){

            HBox card = new HBox(12);
            card.setCursor(Cursor.HAND);
            card.setStyle(
                "-fx-border-color: #007161;" +
                "-fx-border-radius: 6;" +
                "-fx-background-color: white;" +
                "-fx-padding: 10;"
            );

            VBox left = new VBox(4);

            HBox primaRiga = new HBox();
            Label nome = new Label(preferito.getNome());
            Button rimuoviPreferito = new Button("Rimuovi");
            rimuoviPreferito.setStyle(
                "-fx-background-color: #d9534f;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: Georgia;"
            );

            rimuoviPreferito.setOnAction(event -> {
                try {
                    this.gestoreUtenti.rimuoviPreferito(this.sessione.getUtenteLoggato(), preferito);
                    this.setup();
                }
                catch(RemoteException e){

                }
            });

            nome.setStyle("-fx-font-family: Georgia; -fx-font-size: 15px; -fx-text-fill: #007161;");

            Region spazio = new Region();
            HBox.setHgrow(spazio, Priority.ALWAYS);

            primaRiga.getChildren().addAll(nome, spazio, rimuoviPreferito);

            Label descrizione = new Label(preferito.getDescrizione());
            descrizione.setStyle("-fx-font-family: Georgia; -fx-font-size: 12px;");

            left.getChildren().addAll(primaRiga, descrizione);

            // --- valutazione ---
            Label valLabel;
            try{
                double val = this.gestoreRistoranti.calcolaValutazioneMedia(preferito);
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
                    this.infoPreferito.getChildren().clear();

                    Label titolo = new Label(preferito.getNome());
                    titolo.setStyle(
                        "-fx-font-family: Georgia;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #007161;"
                    );

                    VBox info = new VBox(6);
                    info.setStyle("-fx-padding: 10;");

                    Label indirizzo = new Label(
                        preferito.getIndirizzo() + " - " +
                        preferito.getCitta() + " - " +
                        preferito.getNazione()
                    );

                    indirizzo.setStyle("-fx-font-family: Georgia;");

                    Label descr = new Label(preferito.getDescrizione());
                    descr.setWrapText(true);
                    descr.setStyle("-fx-font-family: Georgia;");

                    info.getChildren().addAll(titolo, indirizzo, descr);

                    this.infoPreferito.getChildren().add(info);

                    for(Recensione r : this.gestoreRistoranti.visualizzaRecensioniPerRistorante(preferito)){
                        this.infoPreferito.getChildren().add(creaCardRecensione(r));
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
            Label autore = new Label();
            String autoreNome = this.sessione.getUtenteLoggato().getNome();
            String autoreCognome = this.sessione.getUtenteLoggato().getCognome();
            boolean propria = false;
            if((autoreNome + " " + autoreCognome).equals(recensione.getAutore())){
                autore.setText("Tu");
                propria = true;
            }
            else{
                autore.setText(recensione.getAutore());
            }
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

                Label info = new Label("Il " + r.getData() + " " + r.getAutore() + " ha risposto:");
                Label testoRisposta = new Label(r.getTesto());
                testoRisposta.setWrapText(true);

                info.setStyle("-fx-font-family: Georgia; -fx-text-fill: #007161;");
                testoRisposta.setWrapText(true);
                testoRisposta.setStyle("-fx-font-family: Georgia;");

                rispostaBox.getChildren().addAll(info, testoRisposta);
                card.getChildren().add(rispostaBox);
                rispostaBox.setStyle(
                    "-fx-padding: 8;" +
                    "-fx-background-color: #f4f4f4;" +
                    "-fx-border-color: #ddd;" +
                    "-fx-border-radius: 4;"
                );

            }

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
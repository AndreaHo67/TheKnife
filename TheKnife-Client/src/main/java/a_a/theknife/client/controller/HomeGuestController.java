package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;

import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.ArrayList;

import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.models.Filtro;
import a_a.theknife.common.models.Ristorante;
import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;
import a_a.theknife.common.exceptions.NoReviewsException;
import javafx.scene.Node;

/**
 *
 * @author user
 */
public class HomeGuestController {

    @FXML private Button tornaAccesso;
    
    // [0] nome, [1] citta, [2] nazione
    @FXML private TextField filtroNome; 
    @FXML private TextField filtroCitta;
    @FXML private TextField filtroNazione;
    
    // [3] riconoscimento
    @FXML private ComboBox<String> listaRiconoscimenti;
    
    // [4] valutazione media
    @FXML private Label numValutazione;
    @FXML private Slider sliderValutazione;
    
    // [5] fascia di prezzo
    @FXML private ComboBox<String> listaPrezzi;
    
    // [6] stella green, [7] prenotazione online, [8] servizio delivery
    @FXML private CheckBox checkStellaGreen;
    @FXML private CheckBox checkPrenotazioneOnline;
    @FXML private CheckBox checkDelivery;
    
    // [9] tipi di cucina, [10] servizi offerti
    @FXML private TextField filtroCucina;
    @FXML private TextField filtroServizi;
    
    @FXML private Button btnCerca;
    @FXML private Button btnAzzeraFiltri;
    
    @FXML private Label titoloConsigliati;
    @FXML private Label sottotitoloConsigliati;
    @FXML private HBox boxConsigliati;
    @FXML private Label titoloElenco;
    @FXML private Label contatoreRistoranti;
    @FXML private ListView<Ristorante> listaRistoranti;
    
    @FXML private VBox dettagli;
    @FXML private Label dettaglioNome;
    @FXML private Button btnChiudiDettagli;
    @FXML private Label dettaglioLocalita;
    @FXML private Label dettaglioIndirizzo;
    @FXML private Label dettaglioCoordinate;
    @FXML private Label dettaglioPrezzo;
    @FXML private Label dettaglioRiconoscimento;
    @FXML private Label dettaglioValutazione;
    @FXML private Label dettaglioStellaGreen;
    @FXML private Label dettaglioPrenotazione;
    @FXML private Label dettaglioDelivery;
    @FXML private Label dettaglioDescrizione;
    @FXML private Label dettaglioTipiCucina;
    @FXML private Label dettaglioServizi;
    @FXML private Label dettaglioTelefono;
    @FXML private Label dettaglioPaginaMichelin;
    @FXML private VBox listaRecensioni;
    
    private GestoreRistoranti gestoreRistoranti;
    private ServerConnection serverConnection;
    private Sessione sessione;
    private String cittaGuest;
    private String nazioneGuest;
    
    /**
     * Metodo per inizializzare il controller con i riferimenti al Server e i dati Guest.
     * @param serverConnection la connessione RMI al server
     * @param sessione la sessione corrente
     * @param citta la città inserita dal guest
     * @param nazione la nazione inserita dal guest
     */
    public void init(ServerConnection serverConnection, Sessione sessione, String citta, String nazione){ 
        try{
            this.serverConnection = serverConnection;
            this.sessione = sessione;
            this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
            this.cittaGuest = citta;
            this.nazioneGuest = nazione;
        } catch(Exception e) {
            error("Errore di connessione", "Impossibile connettersi al server. Assicurati che TheKnifeServer sia in esecusione.\n\n Dettaglio: " + e.getMessage());
        }
        
        caricaConsigliati();
    }
    
    // ========================================================== PARTE GESTIONE GRAFICA DI LISTE, BOX E BOTTONI ========================================================== //
    
    /**
     * Metodo chiamato automaticamente da JavaFX.
     * Inserisce nei ComboBox i valori ammessi dal database, 
     * configura il listener dello Slider per la valutazione,
     * imposta la CellFactory della ListView e carica i ristoranti
     */
    @FXML
    public void initialize(){
        listaRiconoscimenti.setItems(FXCollections.observableArrayList("1 Star", "2 Stars", "3 Stars", "Selected Restaurants", "Bib Gourmand"));
        
        sliderValutazione.valueProperty().addListener((obs, oldVal, newVal) -> {
            numValutazione.setText(String.format("%.1f", newVal.doubleValue()));
        });
        
        listaPrezzi.setItems(FXCollections.observableArrayList("€", "€€", "€€€", "€€€€"));
        
        listaRistoranti.setCellFactory(param -> new ListCell<Ristorante>(){
            @Override
            protected void updateItem(Ristorante ristorante, boolean empty){
                super.updateItem(ristorante, empty);
                if(empty || ristorante == null){
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox riga = new HBox(15);
                    riga.setAlignment(Pos.CENTER_LEFT);
                    riga.setPadding(new Insets(8, 15, 8, 15));
                    riga.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-cursor: hand;");
                    
                    // nome ristorante
                    Label nomeRistorante = new Label(ristorante.getNome());
                    nomeRistorante.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
                    nomeRistorante.setMinWidth(250);
                    
                    // località
                    Label localitaRistorante = new Label(ristorante.getCitta() + ", " + ristorante.getNazione());
                    localitaRistorante.setFont(Font.font("Georgia", 13));
                    localitaRistorante.setTextFill(Color.web("#666666"));
                    localitaRistorante.setMinWidth(200);
                    
                    // fascia di prezzo
                    Label prezzoRistorante = new Label(ristorante.getFasciaPrezzo());
                    prezzoRistorante.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
                    prezzoRistorante.setTextFill(Color.web("#007161"));
                    
                    // riconoscimento
                    Label riconoscimentoRistorante = new Label(ristorante.getRiconoscimento());
                    riconoscimentoRistorante.setFont(Font.font("System", 12));
                    riconoscimentoRistorante.setTextFill(Color.web("#888888"));
                    
                    // tipi cucina
                    String tipoCucina = ristorante.getTipiCucina().isEmpty() ? "" : ristorante.getTipiCucina().get(0); // prendo solo il primo tipo della lista dei tipi
                    Label cucinaRistorante = new Label(tipoCucina);
                    cucinaRistorante.setFont(Font.font("Georgia", 12));
                    cucinaRistorante.setTextFill(Color.web("#999999"));
                    
                    // servizi offerti
                    String servizi = ristorante.getServizi().isEmpty() ? "" : ristorante.getServizi().get(0);
                    Label serviziRistorante = new Label(servizi);
                    serviziRistorante.setFont(Font.font("Georgia", 12));
                    serviziRistorante.setTextFill(Color.web("#999999"));
                    
                    riga.getChildren().addAll(nomeRistorante, localitaRistorante, prezzoRistorante, riconoscimentoRistorante, cucinaRistorante, serviziRistorante);
                    setGraphic(riga);
                }
            }
        });
        
        // click su un elemento della listaRistoranti: mostra il pannello dettagli
        listaRistoranti.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                Sessione.getSessione().setRistoranteSelezionato(newVal);
                dettagliRistorante(newVal);
            }
        });
        
        
        // i consigliati vengono caricati ora nel metodo init(), DOPO che la connessione al server è stata stabilita.
    }
    
    // Click su bottone Cerca: costruisce l'array Filtro[11] dai filtri inseriti sulla grafica. Richiama ricercaRistorantiPerFiltri
    @FXML
    private void gestisciBtnCerca(ActionEvent event){
        Filtro [] filtri = creaFiltri();
        try{
            ArrayList<Ristorante> ristorantiFiltrati = gestoreRistoranti.ricercaRistorantiPerFiltri(filtri);
            aggiornaRisultatiRicerca(ristorantiFiltrati, true);
        } catch(RemoteException e) {
            error("Errore di comunicazione", "Impossibile comunicare con il server.\n\nDettaglio: " + e.getMessage());
        }
    }
    
    // Click sul bottone Azzera riceca: rimuove da tutte le sezioni apposite i filtri inseriti precedentemente dall'utente e ricarica i ristoranti consigliati
    @FXML
    private void gestisciBtnAzzeraFiltri(ActionEvent event){
        filtroNome.clear();
        filtroCitta.clear();
        filtroNazione.clear();
        listaRiconoscimenti.getSelectionModel().clearSelection();
        sliderValutazione.setValue(0.0);
        listaPrezzi.getSelectionModel().clearSelection();
        checkStellaGreen.setSelected(false);
        checkPrenotazioneOnline.setSelected(false);
        checkDelivery.setSelected(false);
        filtroCucina.clear();
        filtroServizi.clear();
        
        // chiudere il pannello dei dettagli del ristorante se è aperto
        chiudiDettagli();
        // ricaricare i consigliati
        caricaConsigliati(); // scrivere consigliati in base a località utente guest (tramite form di inserimento prima di HomeGuest.fxml)
    }
    
    // Click sul bottone Accedi / Registrati: torna alla schermata Accesso.fxml
    @FXML
    private void gestisciBtnTornaAccesso(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/Accesso.fxml")); // cambia
            Parent root = loader.load();
            AccessoController controller = loader.getController(); // AccessoController, HomeGuestController, cambiano in base alla pagina a cui devo reindirizzarmi
            controller.init(serverConnection, sessione);
            
            // Stage: contenitore della schermata da aprire (Accesso.fxml)
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false); 
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch(Exception e) {
            error("Errore di navigazione", "Impossibile tornare alla schermata di accesso.\n\nDettaglio: " + e.getMessage());
        }
    }
    
    // Click sul bottone Chiudi nel pannello dettagli del ristorante
    @FXML
    private void gestisciBtnChiudi(ActionEvent event){
        chiudiDettagli();
    }
    
    // ========================================================== CARICAMENTO DEI RISTORANTI ALL'INTERNO DELLA GRAFICA ========================================================== //
    
    /**
     * Carica i ristoranti suggeriti usando come filtro la località (città e nazione) 
     * specificata dall'utente nel popup di accesso guest.
     */
    private void caricaConsigliati(){
        Filtro[] filtriConsigliati = creaFiltriVuoti();
        filtriConsigliati[1] = new Filtro("citta", this.cittaGuest != null ? this.cittaGuest : "");
        filtriConsigliati[2] = new Filtro("nazione", this.nazioneGuest != null ? this.nazioneGuest : "");
        try{
            ArrayList<Ristorante> allRistoranti = gestoreRistoranti.ricercaRistorantiPerFiltri(filtriConsigliati);
            aggiornaRisultatiRicerca(allRistoranti, false);
        } catch(RemoteException e) {
            error("Errore di caricamento", "Impossibile caricare i ristoranti consigliati.\n\nDettaglio: " + e.getMessage());
        }
    }
    
    /**
     * Metodo che aggiorna l'interfaccia grafica con i nuovi risultati della ricerca.
     * I primi 3 ristoranti vengono mostrati come card nell'HBox, tutti gli altri vengono aggiunti alla lista ListView
     * @param ristoranti l'eleno dei ristoranti da visualizzare
     * @param isRicerca true se si tratta di una ricerca filtrata, false se sono i consigliati iniziali
     */
    private void aggiornaRisultatiRicerca(ArrayList<Ristorante> ristoranti, boolean isRicerca){
        boxConsigliati.getChildren().clear();
        listaRistoranti.getItems().clear();
        chiudiDettagli();
        
        if(isRicerca){
            titoloConsigliati.setText("In evidenza per te");
            sottotitoloConsigliati.setText(ristoranti.size() + " ristoranti trovati");
        } else {
            titoloConsigliati.setText("Consigliati per te");
            sottotitoloConsigliati.setText("Ecco alcuni ristoranti che potrebbero interessarti");
        }
        
        if(ristoranti.isEmpty()){
            if(isRicerca){
                sottotitoloConsigliati.setText("Nessun ristorante trovato con i filtri selezionati.");
            } else {
                sottotitoloConsigliati.setText("Ops... nella tua località non sono ancora stati registrati ristoranti.\n\nClicca su \"Cerca\" per visualizzare tutti i ristoranti ora dispobili!");
            }
            contatoreRistoranti.setText("");
            return;  
        } 
        
        // prime 3 card nell'hbox
        int numCard = Math.min(3, ristoranti.size());
        for(int i = 0; i < numCard; i++){
            VBox card = creaCard(ristoranti.get(i));
            boxConsigliati.getChildren().add(card);
        }
        
        if(ristoranti.size() > 3){
            ArrayList<Ristorante> ristorantiRimanenti = new ArrayList<>(ristoranti.subList(3, ristoranti.size()));
            listaRistoranti.setItems(FXCollections.observableArrayList(ristorantiRimanenti));
            contatoreRistoranti.setText("Altri " + ristorantiRimanenti.size() + " ristoranti - clicca per visualizzare i dettagli");
        } else {
            contatoreRistoranti.setText("");
        }
    }
    
    // ========================================================== CREAZIONE GRAFICA DINAMICA: CARD RISTORANTI ========================================================== //
    private VBox creaCard(Ristorante ristorante){
        VBox card = new VBox();
        card.setPrefSize(330, 320);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 12, 0, 0, 4); -fx-cursor: hand;");
        
        AnchorPane imgRistorante = new AnchorPane();
        imgRistorante.setPrefHeight(140);
        imgRistorante.setStyle("-fx-background-color: linear-gradient(to bottom right, #66bb6a, #007161); -fx-background-radius: 12 12 0 0;");
        
        Label badgeRiconoscimento = new Label(ristorante.getRiconoscimento());
        badgeRiconoscimento.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 5; -fx-padding: 3 8;");
        badgeRiconoscimento.setFont(Font.font("System", FontWeight.BOLD, 11));
        badgeRiconoscimento.setTextFill(Color.web("#007161"));
        AnchorPane.setTopAnchor(badgeRiconoscimento, 10.0);
        AnchorPane.setRightAnchor(badgeRiconoscimento, 10.0);
        imgRistorante.getChildren().add(badgeRiconoscimento);
        
        Label ploRistorante = new Label("[The Knife]");
        ploRistorante.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        ploRistorante.setStyle("-fx-text-fill: white;");
        ploRistorante.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(ploRistorante, 55.0);
        AnchorPane.setLeftAnchor(ploRistorante, 0.0);
        AnchorPane.setRightAnchor(ploRistorante, 0.0);
        imgRistorante.getChildren().add(ploRistorante);
        
        VBox infoRistorante = new VBox(6);
        infoRistorante.setPadding(new Insets(15, 18, 15, 18));
        
        Label nomeRis = new Label(ristorante.getNome());
        nomeRis.setFont(Font.font("Georgia", FontWeight.BOLD, 18)); 
        
        Label localitaRis = new Label(ristorante.getCitta() + ", " + ristorante.getNazione());
        localitaRis.setFont(Font.font("Georgia", 13));
        localitaRis.setTextFill(Color.web("#666666"));
        
        HBox rigaInfo = new HBox(15); // valutazione + fascia di prezzo + tipo di cucina
        rigaInfo.setAlignment(Pos.CENTER_LEFT);
        
        String valutazioneCardRis = "N/D";
        try{
            double media = gestoreRistoranti.calcolaValutazioneMedia(ristorante);
            valutazioneCardRis = String.format("%.1f", media);
        } catch(NoReviewsException e) {
            valutazioneCardRis = "--";
        } catch(RemoteException e) {
            error("Errore di comunicazione", "Impossibile comunicare con il server.\n\nDettaglio: " + e.getMessage());
        }
        
        Label valutazioneRis = new Label(valutazioneCardRis);
        valutazioneRis.setFont(Font.font("System", FontWeight.BOLD, 14));
        valutazioneRis.setTextFill(Color.web("#ff9900"));
        
        Label prezzoRis = new Label(ristorante.getFasciaPrezzo());
        prezzoRis.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        prezzoRis.setTextFill(Color.web("#007161"));
        
        String tipoCucinaPrincipale = ristorante.getTipiCucina().isEmpty() ? "" : ristorante.getTipiCucina().get(0);
        Label cucinaRis = new Label(tipoCucinaPrincipale);
        cucinaRis.setFont(Font.font("Georgia", 12));
        cucinaRis.setTextFill(Color.web("#888888"));
        
        rigaInfo.getChildren().addAll(valutazioneRis, prezzoRis, cucinaRis);
        
        HBox rigaBadge = new HBox(8);
        rigaBadge.setAlignment(Pos.CENTER_LEFT);
        
        if(ristorante.getStellaGreen()){
            Label bStellaGreen = new Label("Stella green");
            bStellaGreen.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 5; -fx-padding: 2 6;");
            bStellaGreen.setFont(Font.font("System", 11));
            bStellaGreen.setTextFill(Color.web("#2e7d32"));
            rigaBadge.getChildren().add(bStellaGreen);
        }
        
        if(ristorante.getDelivery()){
            Label bDelivery = new Label("Delivery");
            bDelivery.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 5; -fx-padding: 2 6;");
            bDelivery.setFont(Font.font("System", 12));
            bDelivery.setTextFill(Color.web("#1565c0"));
            rigaBadge.getChildren().add(bDelivery);
        }
        
        if(ristorante.getPrenotazioneOnline()){
            Label bPrenotazione = new Label("Prenota");
            bPrenotazione.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 5; -fx-padding: 2 6;");
            bPrenotazione.setFont(Font.font("System", 11));
            rigaBadge.getChildren().add(bPrenotazione);
        }
        
        infoRistorante.getChildren().addAll(nomeRis, localitaRis, rigaInfo, rigaBadge);
        card.getChildren().addAll(imgRistorante, infoRistorante);
        
        // click sulla card, mostra i dettagli
        card.setOnMouseClicked(e -> {
           Sessione.getSessione().setRistoranteSelezionato(ristorante);
           dettagliRistorante(ristorante);
        });
        
        return card;
    }
    
    private void dettagliRistorante(Ristorante ristorante){
        dettaglioNome.setText(ristorante.getNome());
        dettaglioLocalita.setText(ristorante.getCitta() + ", " + ristorante.getNazione());
        dettaglioIndirizzo.setText(ristorante.getIndirizzo());
        dettaglioCoordinate.setText("Lat. " + ristorante.getLatitudine() + " Long. " + ristorante.getLongitudine());
        dettaglioPrezzo.setText(ristorante.getFasciaPrezzo());
        dettaglioRiconoscimento.setText(ristorante.getRiconoscimento());
        dettaglioDescrizione.setText(ristorante.getDescrizione());
        dettaglioTelefono.setText(ristorante.getRecapitoTelefonico());
        dettaglioPaginaMichelin.setText(ristorante.getPaginaMichelin());
        
        dettaglioTipiCucina.setText(String.join(", ", ristorante.getTipiCucina()));
        dettaglioServizi.setText(String.join(", ", ristorante.getServizi()));
        
        dettaglioStellaGreen.setVisible(ristorante.getStellaGreen());
        dettaglioStellaGreen.setManaged(ristorante.getStellaGreen());
        dettaglioPrenotazione.setVisible(ristorante.getPrenotazioneOnline());
        dettaglioPrenotazione.setManaged(ristorante.getPrenotazioneOnline());
        dettaglioDelivery.setVisible(ristorante.getDelivery());
        dettaglioDelivery.setManaged(ristorante.getDelivery());
        
        try{
            double media = gestoreRistoranti.calcolaValutazioneMedia(ristorante);
            dettaglioValutazione.setText(String.format("%.1f / 5.0", media));
        } catch(NoReviewsException e) {
            dettaglioValutazione.setText("Nessuna recensione");
        } catch(RemoteException e) {
            dettaglioValutazione.setText("Errore");
        }
        
        recensioniRistorante(ristorante);
        
        dettagli.setVisible(true);
        dettagli.setManaged(true);
        
        ScaleTransition st = new ScaleTransition(Duration.millis(250), dettagli);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1.0);
        st.setToY(1.0);
        
        FadeTransition ft = new FadeTransition(Duration.millis(250), dettagli);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        
        st.play();
        ft.play();   
    }
    
    /**
     * Metodo che chiude il pannello dei dettagli del ristorante con animazione.
     */
    private void chiudiDettagli(){
        if(dettagli.isVisible()){
            FadeTransition ft = new FadeTransition(Duration.millis(200), dettagli);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
               dettagli.setVisible(false);
               dettagli.setManaged(false);
            });
            ft.play();
        }
    }
    
    /**
     * Metodo che carica le recensioni (con relative risposte) di un ristorante, dal server, tramite GestoreRistoranti.visualizzaRecensioniPerRistorante().
     * Per ogni recensione, genera un blocco con autore, valutazione (espressa in stelle), testo, data e la risposta del ristoratore (se presente).
     * L'utente guest non può né scrivere recensioni né rispondere.
     * @param ristorante 
     */
    private void recensioniRistorante(Ristorante ristorante){
        listaRecensioni.getChildren().clear();
       
        try{
            ArrayList<Recensione> recensioni = gestoreRistoranti.visualizzaRecensioniPerRistorante(ristorante);
            if(recensioni.isEmpty()){
                Label noRec = new Label("Questo ristorante non ha nessuna recensione.");
                noRec.setFont(Font.font("Georgia", 14));
                noRec.setTextFill(Color.web("#999999"));
                listaRecensioni.getChildren().add(noRec);
            }
           
            for(Recensione r : recensioni){
                VBox bRec = new VBox(6);
                bRec.setPadding(new Insets(12, 15, 12, 15));
                bRec.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");
               
                // intestazione: autore + valutazione + data
                HBox intestazione = new HBox(15);
                intestazione.setAlignment(Pos.CENTER_LEFT);
               
                Label autore = new Label(r.getAutore());
                autore.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
               
                // Genera stelle in base alla valutazione (1-5)
                String stars = "★".repeat(r.getValutazione()) + "☆".repeat(5 - r.getValutazione());
                Label stelle = new Label(stars);
                stelle.setFont(Font.font("System", 14));
                stelle.setTextFill(Color.web("#ff9900"));
               
                Label data = new Label(r.getUltimaModifica().toString());
                data.setFont(Font.font("Georgia", 12));
                data.setTextFill(Color.web("#999999"));
                
                intestazione.getChildren().addAll(autore, stelle, data);
               
                // Testo della recensione
                Label testoRecensione = new Label(r.getTesto());
                testoRecensione.setFont(Font.font("Georgia", 13));
                testoRecensione.setWrapText(true);
               
                bRec.getChildren().addAll(intestazione, testoRecensione);
                
                // se il ristoratore ha risposto alla recensione, viene messa sotto
                if(r.getRisposta() != null){
                    Risposta risposta = r.getRisposta();
                   
                    VBox bRisp = new VBox(4);
                    bRisp.setPadding(new Insets(10, 12, 10, 12));
                    bRisp.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 6; -fx-border-color: #c8e6c9; -fx-border-radius: 6;");
                   
                    HBox intRisposta = new HBox(10);
                    intRisposta.setAlignment(Pos.CENTER_LEFT);
                   
                    Label rispAutore = new Label("Risposta di " + risposta.getAutore());
                    rispAutore.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
                    rispAutore.setTextFill(Color.web("#007161"));
                   
                    Label dataRisp = new Label(risposta.getData().toString());
                    dataRisp.setFont(Font.font("Georgia", 11));
                    dataRisp.setTextFill(Color.web("#999999"));
                   
                    intRisposta.getChildren().addAll(rispAutore, dataRisp);
                   
                    Label testoRisp = new Label(risposta.getTesto());
                    testoRisp.setFont(Font.font("Georgia", 12));
                    testoRisp.setWrapText(true);
                   
                    bRisp.getChildren().addAll(intRisposta, testoRisp);
                    bRec.getChildren().add(bRisp);
                }
                listaRecensioni.getChildren().add(bRec);
            }   
        } catch(RemoteException e) {
           Label error = new Label("Impossibile caricare le recensioni.");
           error.setFont(Font.font("Georgia", 14));
           error.setTextFill(Color.web("#cc0000"));
           listaRecensioni.getChildren().add(error);
        }
    }
    
    /**
     * Metodo che costruisce l'array Filtro[11] dai valori inseriti tramite interfaccia grafica.
     * Segue esattamente l'ordine atteso da GestoreRistorantiImpl.ricercaRistorantiPerFiltri().
     * 
     * NOTA SLIDER VALUTAZIONE:
     * Lo slider parte da 0.0. Il valore 0.0 rappresenta uno stato "neutro" o "spento",
     * per cui il filtro inviato sarà null (nessun filtro, mostra anche ristoranti senza recensioni).
     * Se l'utente sposta lo slider a >= 1.0, il filtro diventerà attivo e i ristoranti
     * privi di recensioni verranno correttamente scartati dalla query SQL.
     * 
     * [0] nome ("" se vuoto)
     * [1] citta ("" se vuoto)
     * [2] nazione ("" se vuoto)
     * [3] riconoscimento ("" se non selezionato)
     * [4] valutazioneMedia (null se slider == 0.0)
     * [5] fasciaPrezzo (null se non selezionata)
     * [6] stellaGreen (true se selezionato, false altrimenti)
     * [7] prenotazione (true se selezionato, false altrimenti)
     * [8] delivery (true se selezionato, false altrimenti)
     * [9] tipiCucina (null se campo vuoto)
     * [10] servizi (null se campo vuoto)
     * 
     * @return array Filtro di 11 elementi, pronti per l'invocazione RMI
     */
    private Filtro[] creaFiltri(){
        Filtro[] filtri = new Filtro[11];
        
        filtri[0] = new Filtro("nome", filtroNome.getText().trim());
        filtri[1] = new Filtro("citta", filtroCitta.getText().trim());
        filtri[2] = new Filtro("nazione", filtroNazione.getText().trim());
        String riconoscimento = listaRiconoscimenti.getValue();
        filtri[3] = new Filtro("riconoscimento", riconoscimento != null ? riconoscimento : "");
        
        double valSlider = sliderValutazione.getValue();
        Double valutazione = (valSlider > 0.0) ? valSlider : null;
        filtri[4] = new Filtro("valutazioneMedia", valutazione);
        
        filtri[5] = new Filtro("fasciaPrezzo", listaPrezzi.getValue());
        filtri[6] = new Filtro("stellaGreen", checkStellaGreen.isSelected());
        filtri[7] = new Filtro("prenotazioneOnline", checkPrenotazioneOnline.isSelected());
        filtri[8] = new Filtro("delivery", checkDelivery.isSelected());
        
        String tCucina = filtroCucina.getText().trim();
        if(!tCucina.isEmpty()){
            ArrayList<String> tipiCucina = new ArrayList<>();
            for(String tipo : tCucina.split(",")){
                String t = tipo.trim();
                if(!t.isEmpty()){
                    tipiCucina.add(t);
                }
            }
            filtri[9] = new Filtro("tipiCucina", tipiCucina.isEmpty() ? null : tipiCucina);
        } else {
            filtri[9] = new Filtro("tipiCucina", null);
        }
        
        String services = filtroServizi.getText().trim();
        if(!services.isEmpty()){
            ArrayList<String> servizi = new ArrayList<>();
            for(String s : services.split(",")){
                String sv = s.trim();
                if(!sv.isEmpty()){
                    servizi.add(sv);
                }
            }
            filtri[10] = new Filtro("servizi", servizi.isEmpty() ? null : servizi);
        } else {
            filtri[10] = new Filtro("servizi", null);
        }
        return filtri;        
    }
    
    private Filtro[] creaFiltriVuoti(){
        Filtro[] filtri = new Filtro[11];
        filtri[0] = new Filtro("nome", "");
        filtri[1] = new Filtro("citta", "");
        filtri[2] = new Filtro("nazione", "");
        filtri[3] = new Filtro("riconoscimento", "");
        filtri[4] = new Filtro("valutazioneMedia", null);
        filtri[5] = new Filtro("fasciaPrezzo", null);
        filtri[6] = new Filtro("stellaGreen", false);
        filtri[7] = new Filtro("prenotazioneOnline", false);
        filtri[8] = new Filtro("delivery", false);
        filtri[9] = new Filtro("tipiCucina", null);
        filtri[10] = new Filtro("servizi", null);
        return filtri;
    }
    
    private void error(String titolo, String messaggio){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    
    private void info(String titolo, String messaggio){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
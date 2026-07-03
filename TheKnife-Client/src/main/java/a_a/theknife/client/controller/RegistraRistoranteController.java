package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidCoordinatesException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.rmi.RemoteException;

import java.time.LocalDate;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.exceptions.NoMenuException;
import a_a.theknife.common.exceptions.NoServicesException;
import a_a.theknife.common.interfaces.GestoreRecensioni;
import a_a.theknife.common.models.Ristorante;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistraRistoranteController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreRistoranti gestoreRistoranti;
    private GestoreRecensioni gestoreRecensioni;
    
    // elementi grafici
    @FXML private TextField nome;
    @FXML private TextField indirizzo;
    @FXML private TextField citta;
    @FXML private TextField nazione;
    @FXML private TextField paginaMichelin;
    @FXML private TextField paginaWeb;
    @FXML private ComboBox<String> fasciaPrezzo;
    @FXML private TextField latitudine;
    @FXML private TextField longitudine;
    @FXML private TextField recapitoTelefonico;
    @FXML private TextField riconoscimento;
    @FXML private CheckBox stellaGreen;
    @FXML private CheckBox servizioDelivery;
    @FXML private CheckBox prenotazioneOnline;
    @FXML private TextArea descrizione;
    @FXML private VBox tipiCucina;
    @FXML private VBox servizi;
    @FXML private Button btnRegistrati;
    @FXML private Button btnAnnulla;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
        this.gestoreRecensioni = this.serverConnection.getGestoreRecensioni();
    }
    
    @FXML
    private void initialize(){
        aggiungiTipoCucina();
        aggiungiServizio();
        this.fasciaPrezzo.getItems().addAll("€", "€€", "€€€", "€€€€");
    }
    
    private void aggiungiTipoCucina(){
        TextField tipoCucina = new TextField();
        tipoCucina.setPromptText("Inserisci un tipo di cucina");

        this.tipiCucina.getChildren().add(tipoCucina);

        tipoCucina.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal){
                if(!tipoCucina.getText().trim().isEmpty()){
                    // se è l'ultimo campo, crea un altro
                    if(isUltimo(tipoCucina, tipiCucina)){
                        aggiungiTipoCucina();
                    }
                }
            }
        });
    }
    
    private void aggiungiServizio(){
        TextField servizio = new TextField();
        servizio.setPromptText("Inserisci un servizio");
        
        this.servizi.getChildren().add(servizio);
        
        servizio.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal){
                if(!servizio.getText().trim().isEmpty()){
                    if(isUltimo(servizio, servizi)){
                        aggiungiServizio();
                    }
                }
            }
        });
    }
    
    private boolean isUltimo(TextField elemento, VBox elenco){
        return(elenco.getChildren().indexOf(elemento) == elenco.getChildren().size() - 1);
    }
    
    private ArrayList<String> estraiValori(VBox elenco){
        ArrayList<String> valori = new ArrayList<>();
        for(Node nodo : elenco.getChildren()){
            if(nodo instanceof TextField campo){
                String testo = campo.getText().trim();
                if(!testo.isEmpty()){
                    valori.add(testo);
                }
            }
        }
        return valori;
    }
    
    @FXML
    private void gestisciRegistrazione(ActionEvent event){
        try{
            int rProprietario = this.sessione.getUtenteLoggato().getId();
            String rNome = this.nome.getText().trim();
            String rIndirizzo = this.indirizzo.getText().trim();
            String rCitta = this.citta.getText().trim();
            String rNazione = this.nazione.getText().trim();
            String rFasciaPrezzo = this.fasciaPrezzo.getValue();
            double rLatitudine = Double.parseDouble(this.latitudine.getText().trim());
            double rLongitudine = Double.parseDouble(this.longitudine.getText().trim());
            String rRecapitoTelefonico = this.recapitoTelefonico.getText().trim();
            String rPaginaMichelin = this.paginaMichelin.getText().trim();
            String rPaginaWeb = this.paginaWeb.getText().trim();
            String rRiconoscimento = this.riconoscimento.getText().trim();
            boolean rStellaGreen = this.stellaGreen.isSelected();
            String rDescrizione = this.descrizione.getText().trim();
            boolean rPrenotazioneOnline = this.prenotazioneOnline.isSelected();
            boolean rDelivery = this.servizioDelivery.isSelected();
            ArrayList<String> rServizi = this.estraiValori(this.servizi);
            ArrayList<String> rTipiCucina = this.estraiValori(this.tipiCucina);
            
            if(rNome.isEmpty() || rIndirizzo.isEmpty() || rCitta.isEmpty() || rNazione.isEmpty() ||
               this.latitudine.getText().trim().isEmpty() || this.longitudine.getText().trim().isEmpty() ||
               rRecapitoTelefonico.isEmpty() || rPaginaWeb.isEmpty() || rDescrizione.isEmpty() || rTipiCucina.isEmpty() || rServizi.isEmpty()){
                error("Campi vuoti", "Assicurarsi che tutti i campi contrassegnati dal carattere * siano non vuoti.\n"
                    + "Deve essere riportato almeno un tipo di cucina");
                
                return;
            }
            if(!rRecapitoTelefonico.matches("\\+\\d{8,15}")){
                error("Numero di telefono non valido!", "I numeri di telefono devono essere scritti nella forma '+xxxxxxxx'");
                return;
            }
            
            Ristorante ristorante = null;
            try{
                ristorante = new Ristorante(rProprietario, rNome, rIndirizzo, rCitta, rNazione, rFasciaPrezzo, rLatitudine, rLongitudine, rRecapitoTelefonico, rPaginaMichelin, rPaginaWeb, rRiconoscimento, rStellaGreen, rDescrizione, rPrenotazioneOnline, rDelivery, rServizi, rTipiCucina);
            }
            catch(NoMenuException e){
                error("Tipi di cucina non inseriti", "Inseririre almeno un tipo di cucina");
            }
            catch(NoServicesException e){
                error("Servizi non inseriti", "Inserire almeno un servizio");
            }
            
            this.gestoreRistoranti.apriRistorante(ristorante);
            info("Ristorante registrato correttamente", "Verrai reindirizzato alla homepage");
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
                error("Errore imprevisto", "Riavviare l'applicazione");
            }
        }
        catch(NumberFormatException e){
            error("Coordinate non valide!", "Le coordinate devono essere numeri decimali separati dal '.'");
        }
        catch(InvalidCoordinatesException e){
            error("Coordinate non valide!", "Controllare che la latitudine e la longitudine inserite siano valide");
        }
        catch(RemoteException e){
            error("Registrazione fallita", "Modificare la pagina web e/o il recapito telefonico del ristorante e riprovare");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void annullaRegistrazione(ActionEvent event){
        try{
            info("Registrazione annullata", "Verrai reindirizzato alla schermata home");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomepageRistoratore.fxml"));
            Parent root = loader.load();
            
            HomepageRistoratoreController controller = loader.getController();
            controller.init(serverConnection, sessione);
            
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
        }
        catch(IOException e){
            error("Errore imprevisto", "Riavviare l'applicazione");
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
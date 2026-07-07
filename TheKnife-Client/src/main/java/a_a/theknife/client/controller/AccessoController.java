package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import java.rmi.RemoteException;

import java.time.LocalDate;

import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.models.Utente;

import a_a.theknife.common.exceptions.InvalidEMailException;
import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Controller associato ad Accesso.fxml.
 * Gestisce le azioni della schermata di accesso: login, registrazione e accesso guest.
 * @author user
 */
public class AccessoController {
    
    // Campi per ACCEDI
    @FXML private TextField loginUsername;
    @FXML private TextField loginPassword;
    @FXML private Button btnAccedi;
    @FXML private Button btnGuest;
    
    // Campi per REGISTRATI
    @FXML private TextField nomeReg;
    @FXML private TextField cognomeReg;
    @FXML private TextField usernameReg;
    @FXML private TextField emailReg;
    @FXML private TextField passwordReg;
    @FXML private DatePicker dataNascitaReg;
    @FXML private TextField cittaReg;
    @FXML private TextField nazioneReg;
    @FXML private RadioButton ruoloCliente;
    @FXML private RadioButton ruoloRistoratore;
    @FXML private ToggleGroup ruoli;
    @FXML private Button btnRegistrati;
                    
    // riferimenti RMI
    private GestoreAutenticazioni gestoreAutenticazioni;
    private GestoreRistoranti gestoreRistoranti; // necessario per la ricerca dei ristoranti in modalità guest
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    /**
     * Metodo chiamato automaticamente da JavaFX. 
     * Si occupa di ottenere gli stub RMI dal Registry del server.
     * @param serverConnection
     */
    public void init(ServerConnection serverConnection, Sessione sessione){ 
        try{
            this.serverConnection = serverConnection;
            this.sessione = sessione;
            this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
            this.gestoreRistoranti = this.serverConnection.getGestoreRistoranti();
        } catch(Exception e) {
            e.printStackTrace();
            error("Errore di connessione", "Impossibile connettersi al server. Assicurati che TheKnifeServer sia in esecusione.\n\n Dettaglio: " + e.getMessage());
        }
    }
    
    /**
     * Metodo chiamato automaticamente da JavaFX. 
     * Si occupa di ottenere gli stub RMI dal Registry del server.
     */
    @FXML 
    public void initialize(){
    }
    
    @FXML
    private void gestisciAccesso(ActionEvent event){
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();
        
        if(username.isEmpty() || password.isEmpty()){
            error("Campi obbligatori", "Inserisci username e password per effettuare l'accesso");
            return;
        }
        
        try{
            // Il metodo login() sul server verifica le credenziali dell'utente che si vuole loggare
            Utente loggedUser = gestoreAutenticazioni.login(username, password);
            info("Login riuscito!", "Benvenuto " + loggedUser.getUsername() + "!\nRuolo: " + loggedUser.getRuolo());
            
            loginUsername.clear();
            loginPassword.clear();
            this.sessione.setUtenteLoggato(loggedUser);            
            // navigare alla pagina appropriata (cliente o ristoratore). Per farlo scrivo lo stesso codice del main Client, solo che gli passo la pagina fxml corretta
            if(loggedUser.getRuolo().equals("ristoratore")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomepageRistoratore.fxml"));
                Parent root = loader.load();
                
                HomepageRistoratoreController controller = loader.getController();
                controller.init(this.serverConnection, this.sessione);
                
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(true);
                stage.setMaximized(false);
                stage.setMaximized(true);
                stage.centerOnScreen();
                stage.show();
            }
            else{
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
                stage.centerOnScreen();
                stage.show();
            }
        }
        catch(InvalidSessionRequestException e){
            error("Accesso negato", "Username o password errati, oppure hai già una sessione attiva al sistema.");
        }
        catch(RemoteException e){
            error("Errore di comunicazione", "Impossibile comunicare con il server.\n\n Dettaglio: " + e.getMessage());
        }
        catch(IOException e){
            error("Pagina non trovata", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void gestisciRegistrazione(ActionEvent event){
        String nome = nomeReg.getText().trim();
        String cognome = cognomeReg.getText().trim();
        String username = usernameReg.getText().trim();
        String email = emailReg.getText().trim();
        String password = passwordReg.getText();
        LocalDate dataNascita = dataNascitaReg.getValue();
        String citta = cittaReg.getText().trim();
        String nazione = nazioneReg.getText().trim();
        String ruolo = ruoloCliente.isSelected() ? "cliente" : "ristoratore";
        
        // controllo: tutti i campi con * sono obbligatori
        if(nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || citta.isEmpty() || nazione.isEmpty()){
            error("Campi obbligatori", "Tutti i campi contrassegnati con * sono obbligatori!");
            return; 
        }
        
        try{
            Utente nuovoUtente = new Utente(
                    nome,
                    cognome,
                    username,
                    email,
                    password,
                    dataNascita,
                    citta,
                    nazione,
                    ruolo
            );
            gestoreAutenticazioni.registraUtente(nuovoUtente);
            info("Registrazione completata", "Account creato con successo! Ora puoi eseguire il login alla piattaforma.");
            
            // pulizia campi del form dopo la registrazione riuscita
            nomeReg.clear();
            cognomeReg.clear();
            usernameReg.clear();
            emailReg.clear();
            passwordReg.clear();
            dataNascitaReg.setValue(null);
            cittaReg.clear();
            nazioneReg.clear();
            ruoloCliente.setSelected(true); // ripristino al ruolo Cliente di default
        
        } catch(InvalidUsernameException e) {
            error("Username non disponibile", "Lo username " + username + " è già in uso. Sceglierne uno diverso.");
        } catch(InvalidEMailException e) {
            error("Email non valida", "L'indirizzo email inserito non è valido o già registrato"); 
        } catch(InvalidPasswordException e) {
            error("Password non valida", "La password non soddisfa i requisiti minimi di sicurezza");
        } catch(RemoteException e) {
            error("Errore di comunicazione", "Impossibile comunicare con il server.\n\nDettaglio: " + e.getMessage());
        }
    }
    
    /**
     * Gestisce il click sul pulsante "Prosegui come utente guest".
     * Mostra un Dialog per inserire città e nazione dell'utente per poter filtrare
     * i ristoranti consigliati nella sua zona. Se l'utente compila e procede, 
     * viene reindirizzato alla HomeGuest.
     */
    @FXML
    private void gestisciGuest(ActionEvent event){
        // verifica connessione al server per accesso ai ristoranti
        if(gestoreRistoranti == null){
            error("Errore di connessione", "Impossibile accedere come ospite, il server non è raggiungibile");
            return;
        }
        
        // creazione popup per raccogliere città e nazione dell'utente
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Accesso guest");
        dialog.setHeaderText("Benvenuto! Inserisci la tua località per trovare subito i ristoranti consigliati nella tua zona");
        
        ButtonType btnProsegui = new ButtonType("Prosegui", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, btnProsegui);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField cittaField = new TextField();
        cittaField.setPromptText("es. Como");
        TextField nazioneField = new TextField();
        nazioneField.setPromptText("es. Italia");
        
        grid.add(new Label("Città:"), 0, 0);
        grid.add(cittaField, 1, 0);
        grid.add(new Label("Nazione:"), 0, 1);
        grid.add(nazioneField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.showAndWait().ifPresent(response -> {
            if(response == btnProsegui){
                String cittaInserita = cittaField.getText().trim();
                String nazioneInserita = nazioneField.getText().trim();
                
                try{
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/HomeGuest.fxml"));
                    Parent root = loader.load();
                    HomeGuestController controller = loader.getController();
                    controller.init(serverConnection, sessione, cittaInserita, nazioneInserita);
                    
                    // Stage: contenitore della schermata da aprire (HomeGuest.fxml)
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setResizable(true);
                    stage.setMaximized(true);
                    stage.show();
                } catch(Exception e) {
                    error("Errore di navigazione", "Impossibile reindirizzarsi alla schermata guest.\n\nDettaglio: " + e.getMessage());
                }
            }
        });
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
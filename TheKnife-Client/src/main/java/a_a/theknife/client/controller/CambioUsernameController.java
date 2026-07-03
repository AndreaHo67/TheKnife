package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.exceptions.InvalidSessionRequestException;
import a_a.theknife.common.exceptions.InvalidUsernameException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreUtenti;
import a_a.theknife.common.models.GestorePassword;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CambioUsernameController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreUtenti gestoreUtenti;
    private GestoreAutenticazioni gestoreAutenticazioni;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreUtenti = this.serverConnection.getGestoreUtenti();
        this.gestoreAutenticazioni = this.serverConnection.getGestoreAutenticazioni();
    }
    
    // elementi grafici
    @FXML private TextField nuovoUsername;
    @FXML private PasswordField password;
    @FXML private Button btnCambiaUsername;
    @FXML private Button btnAnnulla;
    
    @FXML private void annulla(ActionEvent event){
        info("Cambio username annullato", "Hai annullato l'aggiornamento");
        this.chiudiPopup(event);
    }
    
    @FXML
    private void cambiaUsername(ActionEvent event){
        if(this.nuovoUsername.getText().trim().isEmpty() || this.password.getText().trim().isEmpty()){
            error("Campi vuoti", "Inserire tutti i campi obbligatori");
            return;
        }
        else{
            try{
                if(GestorePassword.verifyPassword(this.password.getText().trim(), this.sessione.getUtenteLoggato().getPassword())){
                    this.gestoreUtenti.modificaUsername(this.sessione.getUtenteLoggato(), this.nuovoUsername.getText().trim());
                    info("Username aggiornato correttamente", "Verrai reindirizzato alla schermata di accesso");
                    this.chiudiPopup(event);
                    
                    try {
                        // prova
                        this.gestoreAutenticazioni.logout(this.sessione.getUtenteLoggato());
                        this.sessione.getUtenteLoggato().setUsername(nuovoUsername.getText().trim());
                        this.gestoreAutenticazioni.login(this.sessione.getUtenteLoggato().getUsername(), this.password.getText().trim());
                    } catch (InvalidSessionRequestException ex) {
                        error("Errore", "Errore");
                    }
                    
                    
                    
                }
                else{
                    error("Password invalida", "Inserire la password dell'utente in uso per aggiornare lo username");
                }
            }
            catch(InvalidPasswordException e){
                error("Impossibile verificare la password", "Riavviare l'applicazione");
            }
            catch(RemoteException e){
                error("Server irraggiungibile", "Riavviare l'applicazione");
            }
            catch(InvalidUsernameException e){
                error("Username non valido", "Lo username inserito non è valido, riprovare");
            }
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
    
    @FXML
    private void chiudiPopup(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
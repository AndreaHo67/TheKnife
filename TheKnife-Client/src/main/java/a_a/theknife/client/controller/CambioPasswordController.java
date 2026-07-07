package a_a.theknife.client.controller;

import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import a_a.theknife.common.exceptions.InvalidPasswordException;
import a_a.theknife.common.interfaces.GestoreAutenticazioni;
import a_a.theknife.common.interfaces.GestoreRistoranti;
import a_a.theknife.common.interfaces.GestoreUtenti;
import a_a.theknife.common.models.GestorePassword;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;



public class CambioPasswordController{
    // connessione al server e sessione
    private ServerConnection serverConnection;
    private Sessione sessione;
    
    // servizi remoti
    private GestoreUtenti gestoreUtenti;
    
    // init
    public void init(ServerConnection serverConnection, Sessione sessione){
        this.serverConnection = serverConnection;
        this.sessione = sessione;
        this.gestoreUtenti = this.serverConnection.getGestoreUtenti();
    }
    
    // elementi grafici
    @FXML private PasswordField vecchiaPassword;
    @FXML private PasswordField nuovaPassword;
    @FXML private PasswordField confermaPassword;
    @FXML private Button btnCambiaPassword;
    @FXML private Button btnAnnulla;
    
    @FXML private void annulla(ActionEvent event){
        info("Cambio password annullato", "Hai annullato l'aggiornamento");
        this.chiudiPopup(event);
    }
    
    @FXML
    private void cambiaPassword(ActionEvent event){
        try{
            if(GestorePassword.verifyPassword(this.vecchiaPassword.getText(), this.sessione.getUtenteLoggato().getPassword())){
                if(this.nuovaPassword.getText().trim().isEmpty() || this.confermaPassword.getText().trim().isEmpty()){
                    error("Campi vuoti", "Inserire tutti i campi obbligatori");
                    return;
                }
                else if(!this.nuovaPassword.getText().trim().equals(this.confermaPassword.getText().trim())){
                    error("Le password non combaciano", "Assicurarsi che la nuova password e la conferma della nuova password coincidano");
                    return;
                }
                else{
                    try{
                        this.gestoreUtenti.modificaPassword(this.sessione.getUtenteLoggato(), this.nuovaPassword.getText().trim());
                        this.sessione.getUtenteLoggato().setPassword(nuovaPassword.getText().trim());
                        info("Successo", "Password modificata correttamente");
                        this.chiudiPopup(event);
                    }
                    catch(RemoteException e){
                        error("Server irraggiungibile", "Riavviare l'applicazione");
                    }
                }
            }
            else{
                error("Password non corretta", "Assicurarsi di inserire la vecchia password correttamente");
            }
        }
        catch(InvalidPasswordException e){
            error("Password non valida", "La nuova password non rispecchia i criteri di sicurezza");
            return;
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
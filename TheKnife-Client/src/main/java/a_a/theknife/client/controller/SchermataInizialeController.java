package a_a.theknife.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class SchermataInizialeController{
    
    @FXML
    private ToggleButton bottoneLogin;
    
    @FXML
    private ToggleButton bottoneRegister;
    
    @FXML
    private ToggleButton bottoneGuest;
    
    @FXML
    private ToggleGroup menuGroup;
    
    
    @FXML private Pane loginPane;
    @FXML private Pane registerPane;
    @FXML private Pane guestPane;

    @FXML
    private void initialize(){
        System.out.println("Controller caricato");
    }

    @FXML
    private void gestisciClickLogin(){
        System.out.println("Bottone Login!");
    }
    
    @FXML
    private void gestisciClickRegister(){
        System.out.println("Bottone Register!");
    }
    
    @FXML
    private void gestisciClickGuest(){
        System.out.println("Bottone Guest");
    }
}
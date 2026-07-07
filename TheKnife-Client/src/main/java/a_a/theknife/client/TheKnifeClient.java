package a_a.theknife.client;

import a_a.theknife.client.controller.AccessoController;
import a_a.theknife.client.utils.ServerConnection;
import a_a.theknife.client.utils.Sessione;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class TheKnifeClient extends Application{

    @Override
    public void start(Stage stage) throws Exception{
        ServerConnection serverConnection = ServerConnection.getServerConnection();
        Sessione sessione = Sessione.getSessione();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/Accesso.fxml"));
        Parent root = loader.load();
        
        AccessoController controller = loader.getController();
        controller.init(serverConnection, sessione);
        
        Scene scene = new Scene(root);
        
        stage.setTitle("The Knife");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    
    public static void main(String[] args){
        launch(args);
    }
}
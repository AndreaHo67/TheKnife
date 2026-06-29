package a_a.theknife.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class TheKnifeClient extends Application{

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/gui/fxml/SchermataIniziale.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("The Knife");
        stage.setScene(scene);
        stage.show();
    }
    
    
    public static void main(String[] args){
        launch(args);
    }
}
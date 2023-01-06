package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.application.App.App;

import java.io.IOException;

public class WelcomeControler {



    @FXML
    private Button inscriptionButton;

    @FXML
    private Button connectionButton;

    protected void goToInscription(){}


    @FXML
    protected void goToConnection() throws IOException {
        Stage primaryStage = App.primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/loginView.fxml"));
        Scene Connectionscene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setScene(Connectionscene);

    }


}

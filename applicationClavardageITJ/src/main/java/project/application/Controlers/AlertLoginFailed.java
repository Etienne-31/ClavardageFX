package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.application.App.App;

import java.io.IOException;

public class AlertLoginFailed {

    private static Stage Window;

    @FXML
    private Button closeButton;
    public static void display() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/loginView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Attention !");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    protected void close(){
        Window.close();
    }
}

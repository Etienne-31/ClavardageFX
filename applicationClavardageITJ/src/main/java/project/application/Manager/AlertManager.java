package project.application.Manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.application.App.App;

import java.io.IOException;

public class AlertManager {

    public static Stage Window;


    public static void displayLoginFailed() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/loginFailedView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Attention !");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    public static void displayPseudoFailed() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/pseudoFailedView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Attention !");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }



}

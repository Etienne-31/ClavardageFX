package project.application.Manager;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.application.App.App;

import java.io.IOException;

public class AlertManager {

    public static Stage Window;
    public static boolean answer;

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

    public static void displayPseudoSucceed() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/pseudoSucceedView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Connexion");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    public static void displayInscriptionFailed() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/inscriptionFailedView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Connexion");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    public static void displayInscriptionSucceed() throws IOException {
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/inscriptionSucceedView.fxml"));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Connexion");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    public static void displayAlert(String alertPATH) throws IOException {

        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        Window = alertWindow;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(alertPATH));
        Scene myScene = new Scene(fxmlLoader.load());

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Connexion");
        alertWindow.setScene(myScene);
        alertWindow.showAndWait();
    }

    public static void  Alert(String title,String messageToDisplay){
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setWidth(250);
        Label label = new Label(messageToDisplay);
        Button yesButton = new Button("Close");
        yesButton.setOnAction(e -> {
            alertWindow.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();

    }


    public static Boolean confirmAlert(String title,String messageToDisplay){
        Stage alertWindow = new Stage();   //Crée la nouvele fenêtre
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setWidth(400);
        Label label = new Label(messageToDisplay);

        Button yesButton = new Button("Oui");
        Button noButton = new Button("non");

        yesButton.setOnAction(e -> {
                answer = true;
                alertWindow.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            alertWindow.close();
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton,noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();

        return answer;
    }





}

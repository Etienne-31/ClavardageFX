package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import project.application.App.App;
import project.application.Models.SessionChat;
import project.application.Models.Utilisateur;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatControler implements Initializable {

    public static Stage primaryStage = null;
    public static Utilisateur interlocuteur = null;
    public static SessionChat sessionChatFenêtre = null;

    public static Boolean mode = null;







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChatControler.primaryStage = App.primaryStage;

        //Gerer ici verifiaction et Init des session chat et de tous le reste


    }
}

package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.application.App.App;
import project.application.Manager.AlertManager;

import java.io.IOException;

public class LoginControler {

    public static String id;

    @FXML
    private TextField mdpBar;

    @FXML
    private TextField idBar;

    @FXML
    private Button loginButton;

    @FXML
    private Label wrongMdp;


    @FXML
    protected void login() throws IOException {
        //Gestion BD
        String login;
        String mdp;

        //GestionInterface
        Stage primaryStage = App.primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/pseudoChooseView.fxml")); //Sert à loader la scen fait sur fxml
        Scene myScene;

        //Récupère le login
        login = idBar.getText();

        //récupère le mdp
        mdp = mdpBar.getText();

        if((login.equals("Etienne"))&(mdp.equals("123"))){   //Ici on devra faire une requête sur la base de donnée et comparer ce qu'on a
            App.user.setId(login);
            System.out.println("Connected");
            myScene = new Scene(fxmlLoader.load());
            primaryStage.setScene(myScene);
        }
        else{
            AlertManager.displayLoginFailed();
        }
    }
}

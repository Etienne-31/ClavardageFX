package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class loginControler {

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
        String login;
        String mdp;
        Boolean bool;

        //Récupère le login
        login = idBar.getText();

        //récupère le mdp
        mdp = mdpBar.getText();

        if((login.equals("Etienne"))&(mdp.equals("123"))){
            //To do
            // Faire broadcast udp
            //Passez à la fenêtre pour voir les gens connecté
            System.out.println("Connected");
        }
        AlertLoginFailed.display();


    }
}

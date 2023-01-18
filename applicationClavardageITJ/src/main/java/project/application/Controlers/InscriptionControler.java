package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.application.Manager.AlertManager;

import java.io.IOException;

public class InscriptionControler {


    @FXML
    TextField idTextfiled;

    @FXML
    TextField passwordTextField;

    @FXML
    Button  submitButton;

    @FXML

    protected void submit() throws IOException{
        AlertManager.displayInscriptionFailed();

        //Ecrire Fonction Submit
        //Enuite Dans login écrire Query depuis la BD

    }
}

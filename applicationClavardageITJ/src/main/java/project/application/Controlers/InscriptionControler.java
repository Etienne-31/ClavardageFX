package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.application.Manager.AlertManager;
import project.application.Manager.UserDBManager;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.util.List;

public class InscriptionControler {


    @FXML
    TextField idTextfiled;

    @FXML
    TextField passwordTextField;

    @FXML
    Button  submitButton;

    @FXML

    protected void submit() throws IOException{
        String idUser = idTextfiled.getText();
        String passWord = passwordTextField.getText();
        List<Utilisateur> listUserFromDB = UserDBManager.getListUser(idUser);
        Boolean isGood = true;

        for(Utilisateur user : listUserFromDB){
            if(user.getIdUser().equals(idUser)){
                isGood = false;
                break;
            }
        }

        if(isGood){
            UserDBManager.
        }

    }
}

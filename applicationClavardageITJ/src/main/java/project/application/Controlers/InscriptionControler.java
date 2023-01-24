package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.application.App.App;
import project.application.Manager.AlertManager;
import project.application.Manager.UserDBManager;
import project.application.Models.UserLogin;
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
        Stage primaryStage = App.primaryStage;
        String idUser = idTextfiled.getText();
        String passWord = passwordTextField.getText();
        List<String> listUserFromDB = UserDBManager.getListUser();
        Boolean isGood = true;
        for(String ite : listUserFromDB){
            if(idUser.equals(ite)){
                isGood = false;
            }

        }
        if((idUser.equals(""))|(passWord.equals(""))|(idUser == null)|(passWord == null)){
            isGood = false;
        }


        if(isGood){
            UserLogin newUser = new UserLogin(idUser,passWord);
            UserDBManager.InsertDetached(newUser);
            newUser = null;
            AlertManager.displayInscriptionSucceed();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/loginView.fxml"));
            Scene Connectionscene = new Scene(fxmlLoader.load(), 600, 400);
            primaryStage.setScene(Connectionscene);
        }
        else{
            AlertManager.displayInscriptionFailed();
        }

    }
}

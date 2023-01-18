package project.application.Controlers;

import javafx.fxml.Initializable;
import project.application.App.App;

import java.net.URL;
import java.util.ResourceBundle;

public class AcceuilControler implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Lancement du thread de gestion des messages");
        App.udpManager.start();

    }
}

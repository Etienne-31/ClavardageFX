package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.application.App.App;
import project.application.Manager.udpManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePseudoControler implements Initializable {

    private udpManager broadcastManager;
    private int myPortUDP;
    private InetAddress broadcastAdress;

    @FXML
    TextField pseudoBar;

    @FXML
    Button submitButton;

    @FXML
    protected void submit() throws IOException {
        Boolean response;
        String pseudo;
        pseudo = pseudoBar.getText();
        App.user.setPseudo(pseudo);            // On set le pseudo qui vient d'être rentrer dans bar
        broadcastManager.envoyerBoradcastUDP();    // On broadcast le pseudo
       // response = attendre response UDP


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            broadcastManager = new udpManager(App.user,myPortUDP,broadcastAdress);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        pseudoBar.setText(App.user.getIdUser());

    }
}

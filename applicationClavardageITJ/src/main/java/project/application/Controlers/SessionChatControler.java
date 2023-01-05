package project.application.Controlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.application.Models.Message;

public class SessionChatControler {

    //Attributs du controler

    NetworkManager TCPmanagement;
    //Rajouter liste de Message qui sera affichez
    //Attributs de la primary stage
    @FXML
    private Button sendButton;

    @FXML
    private TextField txtSendMsg;



    @FXML
    public void sendMessage(ActionEvent event) {
        String txt = txtSendMsg.getText();
        txtSendMsg.clear();
        System.out.println(txt);

    }
}

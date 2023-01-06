package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChatControler {

    //Attributs de la primary stage
    @FXML
    private Button sendButton;

    @FXML
    private TextField txtSendMsg;



    @FXML
    protected void sendMessage() {
        String txt = txtSendMsg.getText();
        txtSendMsg.clear();
        System.out.println(txt);

    }
}

package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePseudoControler implements Initializable {

    @FXML
    TextField pseudoBar;

    @FXML
    Button submitButton;

    @FXML
    protected void submit(){
        String pseudo;
        pseudo = pseudoBar.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pseudoBar.setText(LoginControler.id);

    }
}

package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import project.application.Manager.AlertManager;

public class AlertControler {
    @FXML
    private Button closeButton;

    @FXML
    private Button submitButton;



    @FXML
    protected void close(){
        AlertManager.Window.close();
    }
}

package project.application.Manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import project.application.App.App;

import java.io.IOException;

public class WindowManager {



    public static void closeWindow(Stage windowToClose){
        windowToClose.close();
        System.out.println("Window closed ! ");
    }

    public static Stage initWindowChat(Stage newStage,String FXMLpath) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(FXMLpath)); // Charge la page FXML
        Scene myScene = new Scene(fxmlLoader.load());   // prepare la scene
        newStage.setScene(myScene);                     // set la scene
        return newStage;
    }





}

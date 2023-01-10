package project.application.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class App extends Application {
    public static Stage primaryStage;
    public static Utilisateur user;

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/welcomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws UnknownHostException {
      /*  System.out.println("Debut");
        try{
            user = new Utilisateur();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }


        launch(); */
        System.out.println("My adress : "+InetAddress.getLocalHost().toString());

    }
}
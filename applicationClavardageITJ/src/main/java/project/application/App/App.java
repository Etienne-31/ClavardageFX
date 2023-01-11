package project.application.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.application.Manager.udpManager;
import project.application.Models.Annuaire;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class App extends Application {
    public static Stage primaryStage;

    public final static int udpPort = 1511;
    public static Utilisateur user;
    public static Annuaire userAnnuaire;

    public static udpManager udpManager;

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
        System.out.println("Debut");
        try{
            App.user = new Utilisateur();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        App.userAnnuaire = new Annuaire();
        App.udpManager = new udpManager(App.user,App.udpPort,/*adr broadcast a faire encore */);


        launch();
        System.out.println("My adress : "+InetAddress.getLocalHost().toString());

    }
}
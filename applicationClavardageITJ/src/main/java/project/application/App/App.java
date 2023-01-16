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
import java.net.SocketException;
import java.net.UnknownHostException;


public class App extends Application {
    public static Stage primaryStage;     //Cet Attribut est l'interface graphique , cad la fenêtre où s'affiche les informations

    public final static int udpPort = 1511;  // Port sur leque on communique en UDP , c'est le même sur tous les devices utilisant l'applicatioj
    public static Utilisateur user;       // représente l'utilisateur de l'application
    public static Annuaire userAnnuaire; // l'annuaire de l'application

    public static InetAddress  adrBroadcast= null; // Adresse de broadcast , a définir encore
    public static Boolean connected;              // Attribut servant à indiquer si nous sommes connecté ou pas
    public static udpManager udpManager;           // manager pour UDP

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/welcomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {

        System.out.println("Debut");

        try{
            App.user = new Utilisateur();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        App.userAnnuaire = new Annuaire();
        App.udpManager = new udpManager(App.user,App.udpPort,adrBroadcast);
        System.out.println("Je lance l'appli My adress : "+InetAddress.getLocalHost().toString());

        launch();


    }
}
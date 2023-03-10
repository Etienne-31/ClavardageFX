package project.application.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.application.Manager.AlertManager;
import project.application.Manager.ConnexionChatManager;
import project.application.Manager.udpManager;
import project.application.Models.Annuaire;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class App extends Application {
    public static Stage primaryStage;     //Cet Attribut est l'interface graphique , cad la fenêtre où s'affiche les informations

    public final static int udpPort = 1512;  // Port sur leque on communique pour la gestion de l'annuaire  en UDP , c'est le même sur tous les devices utilisant l'application
    public final static int portUdpGestionTCP = 1515;  // Port sur leque on communique des demandes de connexion TCP, c'est le même sur tous les devices utilisant l'application
    public static Utilisateur user = null;       // représente l'utilisateur de l'application
    public static Annuaire userAnnuaire; // l'annuaire de l'application

    public static InetAddress  adrBroadcast= null; // Adresse de broadcast , a définir encore
    public static Boolean connected;              // Attribut servant à indiquer si nous sommes connecté ou pas
    public static udpManager udpManager = null;           // manager pour UDP

    public static ConnexionChatManager chatManager = null;

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/welcomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


        stage.setTitle("Application clavardage 1.0");
        stage.setOnCloseRequest( e -> {
            e.consume();
            try {
                closeApp();
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {

        System.out.println("Debut");
        System.out.println(System.getProperty("java.class.path"));

        try{
            App.user = new Utilisateur();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        App.userAnnuaire = new Annuaire();
        App.udpManager = new udpManager(App.user,App.udpPort);
        System.out.println("Je lance l'appli My adress : "+InetAddress.getLocalHost().toString());

        launch();


    }

    private void closeApp() throws InterruptedException, IOException {
        if(AlertManager.confirmAlert("Closing App","Voulez-vous quitter l'application ? ")){

            if(App.connected != null){
                synchronized (App.connected){

                    if(App.connected){
                        synchronized (ConnexionChatManager.mapConversationActive){
                            for(String key : ConnexionChatManager.mapConversationActive.keySet()){
                                ConnexionChatManager.mapConversationActive.get(key).deconnexion();
                            }
                        }

                    }
                    App.connected = false;
                }
            }

            if(App.user.getUserPseudo() != null){
                ConnexionChatManager.endAllChat();
                App.udpManager.broadcastDeconnexion();
            }

            App.userAnnuaire.clearAnnuaire();
            Thread.sleep(1000);
            System.out.println("Fermeture de l'appli");
            primaryStage.close();
        }
    }
}


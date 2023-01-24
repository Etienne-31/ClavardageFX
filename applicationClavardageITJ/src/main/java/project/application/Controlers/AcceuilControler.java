package project.application.Controlers;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.application.App.App;
import project.application.Manager.AlertManager;
import project.application.Manager.ConnexionChatManager;
import project.application.Manager.udpManager;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AcceuilControler implements Initializable {



    private MapChangeListener<String, Utilisateur> listener;

    private Stage primaryStage;

    @FXML
    AnchorPane root;

    @FXML
    AnchorPane listConteneur;

    @FXML
    Button decoButton;

    @FXML
    ListView<String>  ListAnnuaire;

    @FXML
    Button test;


    @FXML
    protected void goToChat(){
        String pseudo;                             // A récupérer de l'endroit ou on clique sur la listView
        Boolean chatDejactif = false;
        Scene scene = null;
        for(String key : ConnexionChatManager.mapConversationActive.keySet()){
            if(pseudo.equals(key)){
                chatDejactif = true;
                break;
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/welcomeView.fxml"));

        try {
             scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(chatDejactif){
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else{
            ConnexionChatManager.envoyerDemandeConnexionTCP(//Mettre info ici);
            primaryStage.setScene(scene);
            primaryStage.show();

        }






    }

    @FXML
    protected void Deconnexion() throws IOException {
        if(AlertManager.confirmAlert("Notification","êtes vous sur de vouloir vous déconnecter et quitter l'application ? ")){
            ConnexionChatManager.endAllChat();
            AlertManager.Alert("Notfication","Tous les Interlocuteurs ont été avertit de votre déconnexion");

            if(App.user.getUserPseudo() != null){
                try {
                    App.udpManager.broadcastDeconnexion();
                    App.userAnnuaire.clearAnnuaire();
                    Thread.sleep(1000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            App.udpManager.getDgramSocket().close();
            App.chatManager.getDgramSocket().close();
            primaryStage.close();
        }

    };




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Lancement du thread de gestion des messages");
        primaryStage = App.primaryStage;

        try {
            App.userAnnuaire.addAnnuaire("Thimoté",new Utilisateur());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        listener = change -> {
            if ((change.wasAdded()) | (change.wasRemoved())) {
                ListAnnuaire.getItems().clear();
                ListAnnuaire.getItems().addAll(App.userAnnuaire.getListPseudos());
            }
        };

        ListAnnuaire.getItems().addAll(App.userAnnuaire.getListPseudos());
        App.userAnnuaire.getAnnuaire().addListener(listener);


        //Finit tuto pour pouvoir créer listView et voir comment vien rajouter et afficher annuaire
        //Ajout de la méthode de construction de la view.

        //Quand on clique sur un pseudo de l'annuaire , on vient vérifier si la conversation n'existe pas déjà dans la map de conversation Active
        // Si oui on vient juste afficher la conversation déjà active
        // Si non
        // - On vient vérifier le nombre de conversation active et si le nombre est trop élévé on affiche qu il faut fermer des discussion
        // - Si non
        // - on envoie demande de connexion et on charge une page similaire a une page de chat mais où il sera marqué : en attente de réponse

        //Penser à rajouter



        //App.userAnnuaire.getAnnuaire().addListener(listener);
        //App.userAnnuaire.getAnnuaire().removeListener(listener
    }

    @FXML
    protected void startTest(){
        TestAjout test = new TestAjout();
        test.start();
    }
}

class TestAjout extends Thread{

    public TestAjout(){}

    public void run(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            try {
                App.userAnnuaire.addAnnuaire("Thomas",new Utilisateur());
                App.userAnnuaire.addAnnuaire("Tom",new Utilisateur());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }


        });


    }


}

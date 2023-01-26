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
import javafx.scene.control.SelectionMode;
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

import static project.application.App.App.primaryStage;

public class AcceuilControler implements Initializable {



    public static MapChangeListener<String, Utilisateur> listener;

    private Stage primaryStage;

    private static int acceuilControler = 0;

    @FXML
    AnchorPane root;

    @FXML
    AnchorPane listConteneur;

    @FXML
    Button decoButton;

    @FXML
    ListView<String>  ListAnnuaire;

    @FXML
    Button ChatWith;

    @FXML
    Button dis;

    @FXML
    protected void display(){
        System.out.println("Dans mon annuaire il y a ");
        for(String key : App.userAnnuaire.getAnnuaire().keySet()){
            System.out.println(" ------ "+App.userAnnuaire.getUserFromAnnuaire(key).getUserPseudo());
        }

    }

    @FXML
    protected void goToChat(){
        String pseudo = ListAnnuaire.getSelectionModel().getSelectedItem();                             // A récupérer de l'endroit ou on clique sur la listView
        Boolean chatDejactif = false;
        Scene scene = null;
        for(String key : ConnexionChatManager.mapConversationActive.keySet()){
           if(pseudo.equals(key)){
                chatDejactif = true;
                break;
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/chatView.fxml"));

        try {
             scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatControler.PseudoInterlocuteur = pseudo;


        if(chatDejactif){

            ChatControler.PseudoInterlocuteur = pseudo;
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else{
            try {
                ConnexionChatManager.envoyerDemandeConnexionTCP(App.chatManager.getDgramSocket(),App.user,App.userAnnuaire.getUserFromAnnuaire(pseudo),App.portUdpGestionTCP,ConnexionChatManager.numeroPortLibre);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ChatControler.PseudoInterlocuteur = "";
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
            App.userAnnuaire.getAnnuaire().removeListener(listener);
            App.udpManager.getDgramSocket().close();
            App.chatManager.getDgramSocket().close();
            primaryStage.close();
        }

    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage = App.primaryStage;

        listener = change -> {
            if ((change.wasAdded()) | (change.wasRemoved())) {
                ListAnnuaire.getItems().clear();
                ListAnnuaire.getItems().addAll(App.userAnnuaire.getListPseudos());
            }
        };

        ListAnnuaire.getItems().addAll(App.userAnnuaire.getListPseudos());
        ListAnnuaire.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        App.userAnnuaire.getAnnuaire().addListener(listener);
        if(acceuilControler == 0){
            acceuilControler = 1;
            try {
                AlertManager.displayPseudoSucceed();       // On affiche qu'on est connecté
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }


}



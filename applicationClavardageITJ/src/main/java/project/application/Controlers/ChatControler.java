package project.application.Controlers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hibernate.event.internal.OnUpdateVisitor;
import project.application.App.App;
import project.application.Manager.AlertManager;
import project.application.Manager.ConnexionChatManager;
import project.application.Models.SessionChat;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ChatControler implements Initializable {

    public static Stage primaryStage = null;
    public static String PseudoInterlocuteur = "";

    public SessionChat sessionChatFenêtre;


    public static Boolean mode = false;

    public static Boolean ouvertureChatOkay = false;


    private ListChangeListener<String> listener;

    @FXML
    TextArea textBar;

    @FXML
    Button sendButton;

    @FXML
    Button backToAnnuaire;

    @FXML
    Button deconnectChat;

    @FXML
    ListView<String> affichageMessages;

    @FXML
    protected void sendMessage() {
        synchronized (ChatControler.ouvertureChatOkay) {
            if (ChatControler.ouvertureChatOkay) {
                String getText = textBar.getText();
                System.out.println("J'ai retiré ce message de la bar de text "+getText);
                if (!((getText.equals("")) | (getText == null))) {
                    this.sessionChatFenêtre.sendMessage(getText);
                }
            }
        }
    }

    @FXML
    protected void goBackToAnnuaire() throws IOException {

        if (ChatControler.primaryStage != null) {
            synchronized (ChatControler.primaryStage) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
                Scene myScene;
                myScene = new Scene(fxmlLoader.load());
                if(this.sessionChatFenêtre.listProperty != null) {
                    this.sessionChatFenêtre.listProperty.removeListener(listener);
                }
                primaryStage.setScene(myScene);
            }
        }
    }

    @FXML
    protected void deconnectFromOtherUser() {
        if (AlertManager.confirmAlert("Déconnexion", "Voulez-vous , vous déconnexter de ce chat")) {
            sessionChatFenêtre.deconnexion();
            if (ChatControler.primaryStage != null) {
                synchronized (ChatControler.primaryStage) {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
                    Scene myScene;
                    try {
                        myScene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    this.sessionChatFenêtre.listProperty.removeListener(listener);
                    primaryStage.setScene(myScene);
                }
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChatControler.primaryStage = App.primaryStage;
        if(!(ChatControler.PseudoInterlocuteur.equals(""))){

            SessionChat sessionChatWindow;
            Boolean convDejaActive = false;

            synchronized (ConnexionChatManager.mapConversationActive) {
                for (String key : ConnexionChatManager.mapConversationActive.keySet()) {
                    if (ChatControler.PseudoInterlocuteur.equals(key)) {
                        this.sessionChatFenêtre = ConnexionChatManager.mapConversationActive.get(key);
                        convDejaActive = true;
                        break;
                    }
                }
            }

            if(!convDejaActive){
                boolean mode = false;
                synchronized (ChatControler.mode){
                    mode = ChatControler.mode;
                }
                Utilisateur otherUser = App.userAnnuaire.getUserFromAnnuaire(ChatControler.PseudoInterlocuteur);
                String pseudo = ChatControler.PseudoInterlocuteur;
                if(mode == true){
                    this.sessionChatFenêtre = new SessionChat(App.user,otherUser,true,null,ConnexionChatManager.numeroPortLibre-1);
                }
                else{
                    this.sessionChatFenêtre = new SessionChat(App.user,otherUser,false,otherUser.getIpUser(),otherUser.getPortOuContacter());
                }

                synchronized (ConnexionChatManager.mapConversationActive) {
                    ConnexionChatManager.mapConversationActive.put(pseudo,this.sessionChatFenêtre);
                }

            }
            //On créer le listener
            this.listener = change -> {
                if (change.wasAdded()) {
                    affichageMessages.getItems().addAll(this.sessionChatFenêtre.listProperty);
                }
            };
            this.sessionChatFenêtre.listProperty.addListener(listener);
            affichageMessages.setItems(this.sessionChatFenêtre.listProperty);

        }
        else{
            affichageMessages.getItems().add(" En attente de connexion de l'autre utilisateur ");
        }
    }


}

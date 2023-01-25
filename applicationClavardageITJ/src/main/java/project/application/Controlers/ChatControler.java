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
    public static Utilisateur interlocuteur;

    static {
        try {
            interlocuteur = new Utilisateur();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionChat sessionChatFenêtre = new SessionChat();

    public static Boolean mode = false;

    public static Boolean ouvertureChatOkay = false;

    public static int numPortLibre = 1600;



    private ListChangeListener<String> listener;
    @FXML
    Label pseudoDisplay;
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
    protected void sendMessage(){
        synchronized (ChatControler.ouvertureChatOkay){
            if(ChatControler.ouvertureChatOkay){
                String getText = textBar.getText();
                if(!((getText.equals(""))|(getText == null))){
                    sessionChatFenêtre.sendMessage(getText);
                }
            }
        }
    }

    @FXML
    protected void goBackToAnnuaire() throws IOException {

        if(ChatControler.primaryStage != null){
            synchronized (ChatControler.primaryStage){
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
                Scene myScene;
                myScene = new Scene(fxmlLoader.load());
                ChatControler.sessionChatFenêtre.listProperty.removeListener(listener);
                primaryStage.setScene(myScene);
            }
        }
    }

    @FXML
    protected void deconnectFromOtherUser(){
        if(AlertManager.confirmAlert("Déconnexion","Voulez-vous , vous déconnexter de ce chat")){
            sessionChatFenêtre.deconnexion();
            if(ChatControler.primaryStage != null){
                synchronized (ChatControler.primaryStage){
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
                    Scene myScene;
                    try {
                        myScene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ChatControler.sessionChatFenêtre.listProperty.removeListener(listener);
                    primaryStage.setScene(myScene);
                }
            }
        }
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChatControler.primaryStage = App.primaryStage;
        
            pseudoDisplay.setText(ChatControler.interlocuteur.getUserPseudo());

        if(!ChatControler.ouvertureChatOkay){
            affichageMessages.getItems().add("En attente de réponse de l'autre utilisateur  ");
        }
        else{
            Boolean convDejaActive = false;

            synchronized (ConnexionChatManager.mapConversationActive){
                for(String key : ConnexionChatManager.mapConversationActive.keySet()){
                    if(ChatControler.interlocuteur.getUserPseudo().equals(key)){
                        convDejaActive = true;
                        break;
                    }
                }
            }
            if (convDejaActive){
                synchronized (ChatControler.sessionChatFenêtre){
                    synchronized (ConnexionChatManager.mapConversationActive){
                        ChatControler.sessionChatFenêtre = ConnexionChatManager.mapConversationActive.get(ChatControler.interlocuteur.getUserPseudo());
                    }
                }
            }
            else{
                synchronized (ChatControler.mode){
                    synchronized (ChatControler.sessionChatFenêtre){
                        synchronized (ConnexionChatManager.mapConversationActive){
                            if(ChatControler.mode){
                                ChatControler.sessionChatFenêtre = new SessionChat(App.user,ChatControler.interlocuteur,true,App.userAnnuaire.getUserFromAnnuaire(ChatControler.interlocuteur.getUserPseudo()).getIpUser().toString(),ChatControler.numPortLibre);
                            }
                            else{
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                ChatControler.sessionChatFenêtre = new SessionChat(App.user,ChatControler.interlocuteur,false,App.userAnnuaire.getUserFromAnnuaire(ChatControler.interlocuteur.getUserPseudo()).getIpUser().toString(),ChatControler.numPortLibre);
                            }
                            ConnexionChatManager.mapConversationActive.put(ChatControler.interlocuteur.getUserPseudo(),ChatControler.sessionChatFenêtre);
                        }
                    }
                }
                ChatControler.sessionChatFenêtre.start();
            }
            listener = change -> {
                if(change.wasAdded()){
                    affichageMessages.getItems().addAll(ChatControler.sessionChatFenêtre.listProperty);
                }
            };
            ChatControler.sessionChatFenêtre.listProperty.addListener(listener);
            affichageMessages.getItems().addAll(ChatControler.sessionChatFenêtre.listProperty);

        }


    }
}

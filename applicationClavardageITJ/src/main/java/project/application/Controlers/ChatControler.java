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
import project.application.Models.SessionChatUDP;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ChatControler implements Initializable {

    public static Stage primaryStage = null;
    public static String PseudoInterlocuteur = "";

    public SessionChatUDP sessionChatFenĂȘtre;


    public static Boolean mode = false;

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
        synchronized (ChatControler.PseudoInterlocuteur) {
            if (!ChatControler.PseudoInterlocuteur.equals("")) {
                String getText = textBar.getText();
                System.out.println("J'ai retirĂ© ce message de la bar de text "+getText);
                if(!(this.sessionChatFenĂȘtre.getSocket().isClosed())){
                    if (!((getText.equals("")) | (getText == null))) {
                        if (getText.length() < 1000){
                            this.sessionChatFenĂȘtre.sendMessage(getText);
                            textBar.clear();
                        }
                        else{
                            AlertManager.Alert("Attention","Le message est trop long");
                        }
                    }

                }
                else{
                    AlertManager.Alert("Attention ","La Connexion a Ă©tĂ© fermĂ© on ne peut plus envoyer de messages");
                }

            }
        }
    }

    @FXML
    protected void goBackToAnnuaire() throws IOException {

        if (ChatControler.primaryStage != null) {
            synchronized (ChatControler.primaryStage) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert Ă  loader la scene fait sur fxml
                Scene myScene;
                myScene = new Scene(fxmlLoader.load());
                if((this.sessionChatFenĂȘtre.getListMessageDataObservable() != null)&(this.sessionChatFenĂȘtre != null)) {
                    this.sessionChatFenĂȘtre.getListMessageDataObservable().removeListener(listener);
                }
                primaryStage.setScene(myScene);
            }
        }
    }

    @FXML
    protected void deconnectFromOtherUser() {
        if (AlertManager.confirmAlert("DĂ©connexion", "Voulez-vous , vous dĂ©connexter de ce chat")) {
            sessionChatFenĂȘtre.deconnexion();
            if (ChatControler.primaryStage != null) {
                synchronized (ChatControler.primaryStage) {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert Ă  loader la scene fait sur fxml
                    Scene myScene;
                    try {
                        myScene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    this.sessionChatFenĂȘtre.getListMessageDataObservable().removeListener(listener);
                    primaryStage.setScene(myScene);
                }
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChatControler.primaryStage = App.primaryStage;
        System.out.println("Initialisation de la chat view l'interlocuteur est : "+ChatControler.PseudoInterlocuteur);
        if(!(ChatControler.PseudoInterlocuteur.equals(""))){

            SessionChat sessionChatWindow;
            Boolean convDejaActive = false;

            synchronized (ConnexionChatManager.mapConversationActive) {
                for (String key : ConnexionChatManager.mapConversationActive.keySet()) {
                    if (ChatControler.PseudoInterlocuteur.equals(key)) {
                        System.out.println("Acceuil controler :La conversation entre "+App.user.getUserPseudo()+ " et "+  ConnexionChatManager.mapConversationActive.get(key).getOtherUser().getUserPseudo() +" a Ă©tĂ© trouvĂ©e et fonctionne avec le socket : "+ConnexionChatManager.mapConversationActive.get(key).getSocket().toString());
                        this.sessionChatFenĂȘtre = ConnexionChatManager.mapConversationActive.get(key);
                        convDejaActive = true;
                        break;
                    }
                }
            }

            if(!convDejaActive) {

                System.out.println("Chat controler , la conv n a pas Ă©tĂ© trouvĂ© on la crĂ©e ");
                Utilisateur otherUser = App.userAnnuaire.getUserFromAnnuaire(ChatControler.PseudoInterlocuteur);
                String pseudo = ChatControler.PseudoInterlocuteur;
                System.out.println("Chat controler Pret Ă  initier la connexion avec : " + pseudo + " qui propose de se sonnecter au port : " + otherUser.getPortOuContacter());
                this.sessionChatFenĂȘtre = new SessionChatUDP(App.user, otherUser, otherUser.getIpUser(), ConnexionChatManager.numeroPortLibre - 1, otherUser.getPortOuContacter());

                synchronized (ConnexionChatManager.mapConversationActive) {
                    ConnexionChatManager.mapConversationActive.put(pseudo, this.sessionChatFenĂȘtre);
                }
                this.sessionChatFenĂȘtre.start();
            }




            affichageMessages.getItems().addAll(this.sessionChatFenĂȘtre.getListMessageDataObservable());

            this.listener = change -> {
                if(change.next()){
                    if(change.wasAdded()){
                        affichageMessages.getItems().clear();
                        affichageMessages.getItems().addAll(this.sessionChatFenĂȘtre.getListMessageDataObservable());
                    }
                }

            };
            this.sessionChatFenĂȘtre.getListMessageDataObservable().addListener(listener);


        }
        else{
            affichageMessages.getItems().add(" En attente de connexion de l'autre utilisateur ");
        }
    }

}


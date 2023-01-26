package project.application.Controlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.application.App.App;
import project.application.Manager.AlertManager;
import project.application.Manager.ConnexionChatManager;
import project.application.Manager.udpManager;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
public class ChoosePseudoControler implements Initializable {

    private udpManager broadcastManager;
    private int myPortUDP;
    private InetAddress broadcastAdress;
    private ConnexionChatManager chatManager;

    private ArrayList<String> listResponse;

    @FXML
    TextField pseudoBar;

    @FXML
    Button submitButton;

    @FXML
    protected void submit() throws IOException, InterruptedException {
        Stage primaryStage = App.primaryStage;
        listResponse = new ArrayList<String>();
        boolean finRetour = false;

        String message_recu;
        boolean pseudoGood = true;
        boolean sameIP;
        DatagramPacket receivedDatagram;
        int debutPseudoResponse;
        int finPseudoResponse;
        int debutAdresse;
        int finAdresse;

        String fullResponseAdress;
        InetAddress responseAdress;

        App.user.setUserPseudo(pseudoBar.getText());
        System.out.println("User from choosePseudoControler :"+App.user.getIdUser()+" : Je vais broadcast mon pseudo");
        broadcastManager.broadcastPseudo();    // On broadcast le pseudo
        System.out.println("User :"+App.user.getIdUser()+" : J'ai broadcast mon pseudo ");



        while(!finRetour){
            sameIP = false;
            message_recu = "";               // On re initialise la variable message recu pour que rien ne soit contenu dedans
            Thread.sleep(100);
            receivedDatagram = App.udpManager.attendreMessageTO();   // On attend une response , si il y en a pas received_datagram sera null
            if(receivedDatagram != null){
                sameIP = App.udpManager.checkIP(receivedDatagram.getAddress());
            }

            if(sameIP){
                receivedDatagram = null;
                message_recu="nope";
            }

            if(receivedDatagram != null){
                System.out.println("From ChoosePseudo controler - message recu ");
                message_recu = new String(receivedDatagram.getData());
                System.out.println("From ChoosePseudo controler submit() - message recu :"+message_recu+" depuis l'adresse : "+receivedDatagram.getAddress().toString());
            }

            if(message_recu.equals("")){    //Une fois qu'on aura recu toutes les réponses , attendreMessage renverra Null
                System.out.println("From choosePseudoControler submit() : Fin du timer socket on attend plus de message");
                finRetour = true;
            }
            else if(!message_recu.equals("nope")){
                System.out.println("From choosePseudoControler submit() : On ajoute à la liste le msg :"+message_recu);
                listResponse.add(message_recu);   //On ajoute les messages recu dans une liste pour faire un traitement après le while pour perdre le moins de temps possible
                receivedDatagram = null;
            }
            else{
                System.out.println("From choosePseudoControler submit() : le message est égale à :"+message_recu);
            }
        }

        if(listResponse.size() > 0) {
            for (String response : listResponse) {
                int debutResponse = response.indexOf("Response:") + "Response:".length();
                int finResponse = response.indexOf("/finResponse");
                String rep = response.substring(debutResponse, finResponse);
                if (rep.equals("non")) {
                    App.user.setUserPseudo("");
                    App.userAnnuaire.getAnnuaire().clear(); // On reset l'annuaire
                    pseudoGood = false;
                    break;
                } else if (rep.equals("oui")) {
                    System.out.println("Response recu : " + response);
                    debutPseudoResponse = response.indexOf("/Pseudo:") + "/Pseudo:".length();
                    finPseudoResponse = response.indexOf("/Adresse:");

                    debutAdresse = response.indexOf("/Adresse:") + "/Adresse:".length();
                    finAdresse = response.indexOf("/finAdresse");

                    fullResponseAdress = response.substring(debutAdresse, finAdresse);   //On récupère l'adresse
                    System.out.println("From ChoosePseudoControler submit() pour la réponse : " + response + " l'adresse est " + fullResponseAdress);
                    responseAdress = InetAddress.getByName(fullResponseAdress.substring(fullResponseAdress.indexOf("/") + 1)); // On la met en InetAdress en enlevant adress mac
                    App.userAnnuaire.addAnnuaire(response.substring(debutPseudoResponse, finPseudoResponse), new Utilisateur(response.substring(debutPseudoResponse, finPseudoResponse), responseAdress));//On ajoute l'utilisateur à l'annuaire

                }

            }
        }
        System.out.println("From choosePseudoControler submit() : Fin de lecture de  listeResponse");
        System.out.println("From choosePseudoControler submit() : Le variable pseudoGood est "+pseudoGood);

        if(pseudoGood){
            App.connected = true;                   // Après toutes les étapes on est enfin connecté donc l'attribut boolean static dans App passe à true
            App.udpManager.broadcastConfirmationPseudo(); //Confirmer pseudo en broadcastant à nouveau
            try {
                chatManager = new ConnexionChatManager(App.user,App.portUdpGestionTCP);
                App.chatManager = chatManager;
            } catch (SocketException e) {
                App.connected = false;
                App.primaryStage.close();
                e.printStackTrace();
            }

            chatManager.start();
            App.udpManager.start();

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
            Scene myScene;
            myScene = new Scene(fxmlLoader.load());
            primaryStage.setScene(myScene);

        }
        else{
            AlertManager.displayPseudoFailed();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {  //Cette fonction s"execute au chargement de la page

        this.broadcastManager = App.udpManager;
        pseudoBar.setText(App.user.getIdUser());

    }
}

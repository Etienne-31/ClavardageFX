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
import project.application.Manager.udpManager;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
public class ChoosePseudoControler implements Initializable {

    private udpManager broadcastManager;
    private int myPortUDP;
    private InetAddress broadcastAdress;

    private ArrayList<String> listResponse;

    @FXML
    TextField pseudoBar;

    @FXML
    Button submitButton;

    @FXML
    protected void submit() throws IOException {
        Stage primaryStage = App.primaryStage;
        listResponse = new ArrayList<String>();
        Boolean finRetour = false;
        String pseudo;
        String message_recu;
        Boolean pseudoGood = true;
        DatagramPacket receivedDatagram;
        int debutPseudoResponse;
        int finPseudoResponse;
        int debutAdresse;
        int finAdresse;
        int debutPort;
        int finPort;
        String fullResponseAdress;
        InetAddress responseAdress;

        pseudo = pseudoBar.getText();
        System.out.println("User :"+App.user.getIdUser()+" : Je vais broadcast mon pseudo");
        broadcastManager.broadcastPseudo();    // On broadcast le pseudo
        System.out.println("User :"+App.user.getIdUser()+" : J'ai broadcast mon pseudo ");



        while(finRetour == false){
            message_recu = null;               // On re initialise la variable message recu pour que rien ne soit contenu dedans
            receivedDatagram = App.udpManager.attendreMessage();   // On attend une response , si il y en a pas received_datagram sera null

            if(receivedDatagram != null){
                message_recu = new String(receivedDatagram.getData());
            }

            if(message_recu.equals(null)){    //Une fois qu'on aura recu toutes les réponses , attendreMessage renverra Null
                finRetour = true;
            }
            else{
                listResponse.add(message_recu);   //On ajoute les messages recu dans une liste pour faire un traitement après le while pour perdre le moins de temps possible
            }
        }

        for(String response : listResponse){
            if(response.equals("non")){
                App.userAnnuaire.getAnnuaire().clear(); // On reset l'annuaire
                pseudoGood = false;
                break;
            }
            else{
                debutPseudoResponse = response.indexOf("/Pseudo:")+"/Pseudo:".length();
                finPseudoResponse = response.indexOf("/Adresse:");

                debutAdresse = response.indexOf("/Adresse:")+"/Adresse:".length();
                finAdresse = response.indexOf("/port:");

                fullResponseAdress = response.substring(debutAdresse,finAdresse);   //On récupère l'adresse
                responseAdress = InetAddress.getByName(fullResponseAdress.substring(fullResponseAdress.indexOf("/")+1)); // On la met en InetAdress en enlevant adress mac
                App.userAnnuaire.getAnnuaire().put(response.substring(debutPseudoResponse,finPseudoResponse),new Utilisateur(response.substring(debutPseudoResponse,finPseudoResponse),responseAdress));//On ajoute l'utilisateur à l'annuaire
            }
        }

        if(pseudoGood == true){
            App.connected = true;                   // Après toutes les étapes on est enfin connecté donc l'attribut boolean static dans App passe à true
            App.user.setUserPseudo(pseudo);            // On set le pseudo qui vient d'être rentrer dans bar
            App.udpManager.broadcastConfirmationPseudo(); //Confirmer pseudo en broadcastant à nouveau
            AlertManager.displayPseudoSucceed();       // On affiche qu'on est connecté


            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scen fait sur fxml
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

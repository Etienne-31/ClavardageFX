package project.application.Manager;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import project.application.App.App;
import project.application.Controlers.AcceuilControler;
import project.application.Controlers.ChatControler;
import project.application.Models.SessionChat;
import project.application.Models.SessionChatUDP;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;

public class ConnexionChatManager extends Thread {


    private  Integer port;
    private DatagramSocket dgramSocket = null;
    private Utilisateur user;

    public static int conversationActive = 0;
    public static final HashMap<String, SessionChatUDP> mapConversationActive = new HashMap<String,SessionChatUDP>();

    public static int numeroPortLibre = 1610;



    public ConnexionChatManager(Utilisateur user, Integer portEcoute) throws SocketException {
        this.user = user;
        this.port = portEcoute;
        this.dgramSocket = new DatagramSocket(port);


    }
    public DatagramSocket getDgramSocket(){return this.dgramSocket;}
    @Override
    public void run(){
        //System.out.println("Lancement du chat de gestion De connexion TCP ");
        ConnexionChatManager.GestionDemande newGestionDemande;
        DatagramPacket paquet = null;
        try {
            this.dgramSocket.setSoTimeout(0);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while(App.connected){
            try {
                synchronized (this.dgramSocket){
                    paquet = attendreDemande();
                }
                if(paquet != null){
                    if(!(paquet.getAddress() == null)){
                        boolean sameIP = checkIP(paquet.getAddress());
                        if(sameIP){paquet = null;}
                    }
                    else{
                        paquet = null;
                    }
                }


            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if(paquet != null){
                String msg = new String(paquet.getData());
                System.out.println("From ConnexionChatManager run() Nouvelle demande de connexion / Le message : "+msg);
                newGestionDemande = new ConnexionChatManager.GestionDemande(this.dgramSocket,paquet);
                newGestionDemande.start();
            }
        }
    }


    public DatagramPacket attendreDemande() throws UnknownHostException {
        DatagramPacket receivedDatagram;
        byte[] receiveData = new byte[500];   //On initialise le buffer de reception


        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.receive(receivedDatagram);                 // On attend la réception d'un message
        }
        catch (SocketException se){
            return null;
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return receivedDatagram;   //Si le datagram n'est pas null, on le renvoie tout simplement
    }

    private boolean checkIP(InetAddress address) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (address.equals(addr)) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void endAllChat() {

        synchronized (App.connected){
            if(App.connected){
                synchronized (ConnexionChatManager.mapConversationActive){
                    for(String key : ConnexionChatManager.mapConversationActive.keySet()){
                        ConnexionChatManager.mapConversationActive.get(key).deconnexion();
                        ConnexionChatManager.mapConversationActive.remove(key);
                    }
                }

            }
            App.connected = false;
        }
        try {
            App.udpManager.broadcastDeconnexion();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void envoyerReponseDemandeConnexionTCP(DatagramSocket dgramSocket,Boolean response, Utilisateur user, Utilisateur utilisateurAcontacter, Integer portOuEnvoyer, Integer portDeConnexionTCP) throws IOException {
        String msg;
        DatagramPacket packet;
        if(response){
            msg = "objet:ReponseDemandeConnexionTCP/finObjet/Response:oui/finResponse"+"/idUser:"+user.getIdUser()+"/finIdUser"+"/Pseudo:"+user.getUserPseudo()+"/finPseudo"+"/PortDeConnexion:"+portDeConnexionTCP.toString()+"/finPortDeConnexion/";
        }
        else{
            msg = "objet:ReponseDemandeConnexionTCP/finObjet/Response:non/finResponse"+"/idUser:"+user.getIdUser()+"/finIdUser"+"/Pseudo:"+user.getUserPseudo()+"/finPseudo"+"/PortDeConnexion:"+portDeConnexionTCP.toString()+"/finPortDeConnexion/";

        }
        packet = new DatagramPacket(msg.getBytes(), msg.length(), utilisateurAcontacter.getIpUser(),portOuEnvoyer);

        dgramSocket.send(packet);      // On envoie le message

    }

    public static void envoyerDemandeConnexionTCP(DatagramSocket dgramSocket, Utilisateur user, Utilisateur utilisateurAcontacter, Integer portOuEnvoyer, Integer portDeConnexionTCP) throws IOException {
        String msg;
        DatagramPacket packet;
        msg = "objet:DemandeConnexionTCP/finObjet/idUser:"+user.getIdUser()+"/finIdUser"+"/Pseudo:"+user.getUserPseudo()+"/finPseudo"+"/PortDeConnexion:"+portDeConnexionTCP.toString()+"/finPortDeConnexion/";
        packet = new DatagramPacket(msg.getBytes(), msg.length(), utilisateurAcontacter.getIpUser(),portOuEnvoyer);
        dgramSocket.send(packet);      // On envoie le message
    }

    private static class GestionDemande extends Thread{

        private final DatagramSocket socketToUse;
        private final DatagramPacket paquet;

        public GestionDemande(DatagramSocket socket,DatagramPacket paquet){
            this.paquet = paquet;
            this.socketToUse = socket;
        }

        public void run(){
            System.out.println("Debut traitement message udp a propos de tcp ");
            String message = new String(this.paquet.getData());
            String objet = null;
            String idOtherUser = null;
            String pseudoOtherUser = null;



            
            int debutObjet = message.indexOf("objet:")+"objet:".length();
            int finObjet = message.indexOf("/finObjet/");
            int debutIdUser = message.indexOf("idUser:")+"idUser:".length();
            int finIdUser = message.indexOf("/finIdUser");
            int debutPseudo = message.indexOf("/Pseudo:")+"/Pseudo:".length();
            int finPseudo = message.indexOf("/finPseudo");
            int debutPort = message.indexOf("/PortDeConnexion:")+"/PortDeConnexion:".length();
            int finPort = message.indexOf("/finPortDeConnexion/");
            int portOuLancerChat = Integer.parseInt(message.substring(debutPort,finPort));



            objet = message.substring(debutObjet,finObjet);
            idOtherUser = message.substring(debutIdUser,finIdUser);
            pseudoOtherUser = message.substring(debutPseudo,finPseudo);

            System.out.println( "le pseudo recu est égal a :" + pseudoOtherUser + " L adresse du mesagge recu est :"+paquet.getAddress().toString()+" et le port d envoie est : "+paquet.getPort());

            App.userAnnuaire.updateAnnuaire(pseudoOtherUser,idOtherUser,paquet.getAddress(),null);

            if(objet.equals("DemandeConnexionTCP")){
                if(ConnexionChatManager.conversationActive >= 50){
                    String finalPseudoOtherUser1 = pseudoOtherUser;
                    Platform.runLater(()->{
                        AlertManager.Alert("Trop de coversation actives","Veuillez fermer un chat actif afin de pouvoir en ouvrir un autre ");
                        try {
                            ConnexionChatManager.envoyerReponseDemandeConnexionTCP(this.socketToUse, false,App.user,App.userAnnuaire.getUserFromAnnuaire(finalPseudoOtherUser1),App.portUdpGestionTCP,ConnexionChatManager.numeroPortLibre);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else{
                    String finalPseudoOtherUser = pseudoOtherUser;
                    Platform.runLater( () -> {
                        if(AlertManager.confirmAlert("nouvelle demande de chat","Voulez vous discuter avec "+ finalPseudoOtherUser+ " ?")){
                               App.userAnnuaire.updateAnnuaire(finalPseudoOtherUser,null,null,portOuLancerChat);
                               ChatControler.PseudoInterlocuteur = finalPseudoOtherUser;

                            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/chatView.fxml")); //Sert à loader la scen fait sur fxml
                            try {
                                ConnexionChatManager.envoyerReponseDemandeConnexionTCP(this.socketToUse, true,App.user,App.userAnnuaire.getUserFromAnnuaire(finalPseudoOtherUser),App.portUdpGestionTCP,ConnexionChatManager.numeroPortLibre);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ConnexionChatManager.numeroPortLibre = ConnexionChatManager.numeroPortLibre +1;
                            try {
                                Scene myScene = new Scene(fxmlLoader.load());
                                App.primaryStage.setScene(myScene);
                                System.out.println(" On affiche la conversation ");
                                App.primaryStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else{
                            try {
                                ConnexionChatManager.envoyerReponseDemandeConnexionTCP(this.socketToUse, false,App.user,App.userAnnuaire.getUserFromAnnuaire(finalPseudoOtherUser),App.portUdpGestionTCP,ConnexionChatManager.numeroPortLibre);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


                }
            }
            else if(objet.equals("ReponseDemandeConnexionTCP")){
                int debutResponse = message.indexOf("Response:")+"Response:".length();
                int finResponse = message.indexOf("/finResponse");

                String responseString = message.substring(debutResponse,finResponse);
                boolean response;


                if(responseString.equals("oui")){
                    response = true;
                }
                else{
                    response = false;
                }

                if(response){
                    App.userAnnuaire.updateAnnuaire(pseudoOtherUser,null,null,portOuLancerChat);
                    ConnexionChatManager.numeroPortLibre = ConnexionChatManager.numeroPortLibre +1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ChatControler.PseudoInterlocuteur = pseudoOtherUser;

                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/chatView.fxml")); //Sert à loader la scen fait sur fxml
                    Platform.runLater(() -> {
                        try {
                            Scene myScene = new Scene(fxmlLoader.load());
                            App.primaryStage.setScene(myScene);
                            App.primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else{
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml"));
                    Platform.runLater(()->{
                        AlertManager.Alert("Connexion refusée","Votre demande de connexion a été refusée");
                        try {
                            Scene myScene = new Scene(fxmlLoader.load());
                            App.primaryStage.setScene(myScene);
                            App.primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }
            }
        }
    }
}

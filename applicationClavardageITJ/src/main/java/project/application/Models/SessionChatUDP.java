package project.application.Models;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import project.application.App.App;
import project.application.Manager.ConnexionChatManager;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedList;

public class SessionChatUDP extends Thread {


    private DatagramSocket dgramSocket;

    private Utilisateur user;
    private Utilisateur other_user = null;

    private int myPort;
    private int portOuCommuniquer;
    private LinkedList<Messages> listMessage;
    private LinkedList<String>  listMessageData;

    private Boolean finConversation;

    private InetAddress adressOtherUser;
    public ListProperty<String> listProperty;
    public SessionChatUDP(Utilisateur user, Utilisateur other_user, InetAddress adressOtherUser, int portOuLancer, int portOuCommuniquer){

        this.user = user;
        this.other_user = other_user;
        this.myPort = portOuLancer;
        this.portOuCommuniquer = portOuCommuniquer;
        this.adressOtherUser = adressOtherUser;
        this.finConversation = false;

        this.listMessage = new LinkedList<Messages>();
        this.listMessageData = new LinkedList<String>();
        this.listProperty = new SimpleListProperty<>(FXCollections.observableList(listMessageData));

        try {
            this.dgramSocket = new DatagramSocket(this.myPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<String> getListMessageData(){return this.listMessageData;}
    public Utilisateur getUser(){return this.user;}
    public Utilisateur getOtherUser() {return this.other_user;}

    public DatagramSocket getSocket(){return this.dgramSocket;}


    public void run(){
        DatagramPacket paquet = null;
        String message_recu = null;
        String MessageFinale = null;
        System.out.println("On est dans le thread");

        while(!this.finConversation){
            try {
                paquet = attendreMessageTO();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if(paquet != null){
                System.out.println("Paquet recu");
                if(!(paquet.getAddress() == null)){
                    boolean sameIP = checkIP(paquet.getAddress());
                    if(sameIP){paquet = null;}
                }
                else{
                    paquet = null;
                }
            }

            if(paquet != null){
                message_recu = new String(paquet.getData());
                int debutMessage = message_recu.indexOf("/Message:")+"/Message:".length();
                int finMessage = message_recu.indexOf("/FinMessage/");
                MessageFinale = message_recu.substring(debutMessage,finMessage);
                System.out.println(" On a recu ce message de "+other_user.getUserPseudo()+" data : "+MessageFinale);
            }

            if(MessageFinale !=null){
                if(MessageFinale.equals("EXIT_CONVERSATION")){
                    this.finConversation = true;
                }
                else{
                    this.listMessage.addLast(new Messages(this.user,this.other_user,MessageFinale));
                    String finalMessageFinale = MessageFinale;
                    Platform.runLater( () -> {
                        this.listMessageData.addLast(finalMessageFinale);
                    });
                }
            }
        }
        synchronized (ConnexionChatManager.mapConversationActive){
            ConnexionChatManager.mapConversationActive.remove(this.other_user.getUserPseudo());
        }
        this.dgramSocket.close();

        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/acceuilView.fxml")); //Sert à loader la scene fait sur fxml
            Scene myScene;
            try {
                myScene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            App.primaryStage.show();

        });

    }


    public void deconnexion(){
        this.finConversation = true;
        String MessageAEnvoyer = "/Message:"+"EXIT_CONVERSATION"+"/FinMessage/";
        DatagramPacket packet = new DatagramPacket(MessageAEnvoyer.getBytes(),MessageAEnvoyer.length(),this.adressOtherUser,this.portOuCommuniquer);
        try {
            this.dgramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        String MessageAEnvoyer = "/Message:"+user.getUserPseudo()+ " : "+message+" -- "+LocalDateTime.now().toString()+"/FinMessage/";
        DatagramPacket packet = new DatagramPacket(MessageAEnvoyer.getBytes(),MessageAEnvoyer.length(),this.adressOtherUser,this.portOuCommuniquer);
        try {
            this.dgramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    public DatagramPacket attendreMessageTO() throws UnknownHostException {
        Boolean sameIP;
        DatagramPacket receivedDatagram;
        int timeout = 5000;
        byte[] receiveData = new byte[1000];   //On initialise le buffer de reception


        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.setSoTimeout(timeout);   //On set le timoutout si aucun message n'est recu alors on lève la socketTimeoutException
            this.dgramSocket.receive(receivedDatagram);                 // On attend la réception d'un message
        }
        catch (SocketException se){
            se.printStackTrace();              //Exception en cas d'erreur avec le socket
        }
        catch(SocketTimeoutException e){
            //System.out.println("From udpManager attendreMessage() : le timeout a expiré ");
            return null;                //Si cette exception est levée alors cette méthode renverra null
        }
        catch(IOException e){
            e.printStackTrace();
        }
        String msg = new String(receivedDatagram.getData());
        int debutMessage = msg.indexOf("/Message:")+"/Message:".length();
        int finMessage = msg.indexOf("/FinMessage/");
        String MessageFinale = msg.substring(debutMessage,finMessage);

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


}

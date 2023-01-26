package project.application.Models;
import com.mysql.cj.protocol.Message;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import project.application.Manager.ConnexionChatManager;
import project.application.Manager.TCPManager;

import java.io.IOException;
//import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class SessionChat extends Thread {

    private boolean mode;
    private Socket socket = null;
    private ServerSocket socketInit;
    private TCPManager networkManagement = null;
    private Utilisateur user = null;
    private Utilisateur other_user = null;
    private BufferedReader is = null;
    private BufferedWriter os = null;

    public Boolean finConversation = null;

    private LinkedList<Messages> listMessage;
    private LinkedList<String>  listMessageData;

    public ListProperty<String> listProperty;

    public SessionChat(){}

    public SessionChat(Utilisateur user, Utilisateur other_user, boolean mode, InetAddress adress, int port){

        System.out.println("Construction de la sessionChat entre : "+user.userPseudo+ " et "+other_user.getUserPseudo()
        +"\n"+ " Dont l'adresse est : "+adress.toString()+" et initialisée sur le port : "+port);

        this.networkManagement = new TCPManager();
        this.user = user;
        this.other_user = other_user;
        this.mode = mode;

        listMessage = new LinkedList<Messages>();
        listMessageData = new LinkedList<String>();
        listProperty = new SimpleListProperty<>(FXCollections.observableList(listMessageData));

        finConversation = false;

        try {
            if(mode == false ){
                Thread.sleep(10);
                this.socket = this.networkManagement.init_socketClientTCP(adress, port, this.socket);
                System.out.println("Connected  with"+other_user.getUserPseudo());
            }
            else{

                this.socketInit = this.networkManagement.init_socketServerTCP(port, this.socketInit);
                this.socket = this.networkManagement.wait_connexionTCP(this.socket, this.socketInit);
                System.out.println("Utilisateur "+other_user.getUserPseudo()+" connecté");
            }
            this.is = this.networkManagement.init_bufferReceptionTCP(this.socket);
            this.os = this.networkManagement.init_bufferEmissionTCP(this.socket);

            Platform.runLater(()->{
                listMessageData.addLast("Debut de la conversation");
            });

            System.out.println("Fin du constructuer");
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        Messages message = null;
        System.out.println("On lance le chat entre l'utilisateur qui est :"+this.user.getUserPseudo()+" et son interlocuteur :"+this.other_user.getUserPseudo());
        synchronized (ConnexionChatManager.mapConversationActive){
            ConnexionChatManager.mapConversationActive.put(this.other_user.getUserPseudo(),this);
        }
        while(!finConversation){

            try{
                message = receptionMessage();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            if(message != null){
                if(message.getData().equals("EXIT_CONVERSATION")){
                    synchronized (this.finConversation){
                        this.finConversation = true;
                    }
                }
                else{
                    this.listMessage.addLast(message);
                    Messages finalMessage = message;
                    Platform.runLater( () -> {
                        this.listMessageData.addLast(finalMessage.getData());
                    });
                }
            }

        }
        try {
            endConnexion();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        synchronized (ConnexionChatManager.mapConversationActive){
            ConnexionChatManager.mapConversationActive.remove(this.other_user.getUserPseudo());
        }
    }

    private void endConnexion() throws IOException {
        System.out.println("fermeture des Buffers");
        this.networkManagement.close_connexion(this.os,this.is);
    }

    public void sendMessage(String message){
        String messageToSend = "Utilisateur : "+this.user.userPseudo+message+" \n"+ LocalDateTime.now().toString();
        this.networkManagement.send(messageToSend,this.os);
        Messages messageAajouter = new Messages(this.user,this.other_user,messageToSend);
        Platform.runLater( () -> {
            this.listMessageData.addLast(String.valueOf(messageToSend));
        });

    }

    public void deconnexion(){
        String msg = "EXIT_CONVERSATION";
        this.networkManagement.send(msg,this.os);
        synchronized (this.finConversation){
            this.finConversation = true;
        }
    }



    public void Testrun() {
        System.out.println("On lance le run");
        System.out.println("On lance le start");
        Messages message = null;
        boolean quit = true;


        if(getMode()==true) {
            System.out.println("Sessionchat.run : envoie des messages");
            BufferedWriter os = this.networkManagement.init_bufferEmissionTCP(this.socket);
            this.networkManagement.send("Test emission serveur", os);
            this.networkManagement.send("Exit", os);
            System.out.println("Sessionchat.run : messages Envoyé!");
            try {
                this.os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            while(quit == false){
                try {
                    message = receptionMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(message != null){
                    this.networkManagement.printMessage(message);
                }
                if(message.getData().equals("Exit")){



                }


            }
            if(this.is != null){

                try {
                    System.out.println("Deconnexion du buffer de reception...");
                    this.is.close();
                    System.out.println("buffer de reception Deconnecté");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }




    private Messages receptionMessage() throws IOException{
        Messages data = null;
        data = networkManagement.receiveMessage(this.is, this.other_user, this.user);
        return data;
    }

    public boolean getMode(){return this.mode;}

    public TCPManager getNetworkMananger(){return this.networkManagement;}

    public Utilisateur getUser(){return this.user;}

    public Utilisateur getOtherUser() {return this.other_user;}

    public BufferedReader getInputFlow() {return this.is;}

    public BufferedWriter getOutputFlow() {return this.os;}

    public Socket getSocket() {return this.socket;}


}

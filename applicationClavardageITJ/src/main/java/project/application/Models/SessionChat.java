package project.application.Models;
import project.application.Manager.TCPManager;

import java.io.IOException;
//import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SessionChat {

    private boolean mode;
    private Socket socket = null;
    private ServerSocket socketInit = null;
    private TCPManager networkManagement = null;
    private Utilisateur user = null;
    private Utilisateur other_user = null;
    private BufferedReader is = null;
    private BufferedWriter os = null;

    public SessionChat(Utilisateur user,Utilisateur other_user,boolean mode,String adress,int port){

        this.networkManagement = new TCPManager();
        this.user = user;
        this.other_user = other_user;
        this.mode = mode;

        try {
            if(mode == false ){

                this.socket = this.networkManagement.init_socketClientTCP(adress, port, this.socket);
                System.out.println("Connected !");
            }
            else{

                this.socketInit = this.networkManagement.init_socketServerTCP(port, this.socketInit);
                this.socket = this.networkManagement.wait_connexionTCP(this.socket, this.socketInit);
            }
            this.is = this.networkManagement.init_bufferReceptionTCP(this.socket);
            this.os = this.networkManagement.init_bufferEmissionTCP(this.socket);
            System.out.println("Fin du constructuer");
        }
        catch (IOException e){
            e.printStackTrace();
        }




    }



    public void run() {
        System.out.println("On lance le run");
        System.out.println("On lance le start");
        Message message = null;
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
                    quit = true;
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




    private Message receptionMessage() throws IOException{
        Message data = null;
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

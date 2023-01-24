package project.application.Manager;


import project.application.Models.Utilisateur;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import project.application.Models.Messages;
public class TCPManager {
    private String listening(BufferedReader is) throws IOException {
        String retour=null;
        try{
            retour = is.readLine();

        }
        catch(IOException e){
            System.out.println("Serveur TCP listening:"+e);
        }
        return retour;

    }

    public Messages receiveMessage(BufferedReader is, Utilisateur sender, Utilisateur receiver) throws  IOException{
        Messages receivedMessage = null;
        String data = listening(is);
        if(data != null){
            receivedMessage = new Messages(sender,receiver,data);
        }
        return receivedMessage;
    }


    public void send(String message,BufferedWriter os){
        try{
            os.write(message);
            os.newLine();
            os.flush();

        }
        catch(IOException e){
            System.out.println("Serveur TCP send:"+e);
        }

    }


    public ServerSocket init_socketServerTCP(int port, ServerSocket socketServeur) throws  IOException {
        try {
            socketServeur = new ServerSocket(port);   // Penser à changer 1234 par port
        } catch (UnknownHostException e) {
            System.out.println("init_socketServerTCP : Port "+port+" indisponible , init échoué ");
            System.exit(1);
        }
        return socketServeur;
    }

    public BufferedReader init_bufferReceptionTCP(Socket socketUsed){
        BufferedReader is = null;
        if(socketUsed.equals(null)){
            System.out.println("init_bufferReceptionTCP : le socketServeur passé en arguments est nul");
            return is;
        }
        try{
            is = new BufferedReader(new InputStreamReader(socketUsed.getInputStream()));
        }
        catch(IOException e){
            System.out.println("init_bufferReceptionTCP : Echec creation du buffer d'input flow "+e);
        }
        if(is == null){
            System.out.println("init_bufferReceptionTCP : Le buffer d'input flow du socket  "+ socketUsed.toString()+" est nul");
        }
        return is;
    }

    public BufferedWriter init_bufferEmissionTCP(Socket socketUsed ){
        BufferedWriter os = null;
        if(socketUsed.equals(null)){
            System.out.println("init_bufferEmissionTCP : le socket passé en arguments est nul");
            return os;
        }

        try{
            os = new BufferedWriter(new OutputStreamWriter(socketUsed.getOutputStream()));
        }
        catch(IOException e){
            System.out.println("init_bufferEmissionTCP : Client TCP init_send:"+e);
        }
        return os;
    }

    public void close_connexion(BufferedWriter os,BufferedReader is) throws IOException {

        try{
            if(is != null){
                is.close();
            }
            if(os != null){
                os.close();
            }

        }
        catch(IOException e){
            System.out.println("close_connexion : "+e);
        }
        System.out.println("close_connexion : Connexion fermée");

    }

    public Socket wait_connexionTCP(Socket socketOfServeur,ServerSocket servSocket) throws IOException {
        if(servSocket.equals(null)){
            System.out.println("wait_connexionTCP : le ServerSocket passé en arguments est nul");
            System.exit(0);
        }
        try{
            System.out.println("En attente d'un client");
            socketOfServeur = servSocket.accept();
            System.out.println("Client accepté");
        }
        catch(IOException e){
            System.out.println("wait_connexionTCP :"+e);
        }
        if(socketOfServeur.equals(null)){
            System.out.println("wait_connexionTCP : le socket servant à initialiser les buffers est nul");

        }
        return socketOfServeur;
    }

    public Socket init_socketClientTCP(String serveurHost, int port,Socket socketOfClient) throws IOException {
        if(serveurHost.equals(null) | (port < 1024)){
            System.out.println("init_socketClientTCP : le serveur host ou le port passé en argument est incorrect");
        }
        try {
            socketOfClient = new Socket(serveurHost, port);
        } catch (UnknownHostException e) {
            System.out.println("On ne sait rien de cet host");
        }
        System.out.println("init_socketClientTCP : Connexion etabli avec :" + serveurHost + " au port : " + port);
        return socketOfClient;
    }

    public synchronized void printMessage(Messages message){
        message.toString();
    }
}

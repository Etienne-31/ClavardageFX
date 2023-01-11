package project.application.Manager;

import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ClosedByInterruptException;

public class udpManager {

    private static final int TIMEOUT_RECEPTION_REPONSE = 5000;
    public DatagramSocket dgramSocket = null;
    public byte[] myBuffer = null;
    public DatagramPacket inPacket = null;
    public String msg;
    public Utilisateur user;
    public int port;
    public InetAddress broadcastAdress;

    public udpManager(Utilisateur user, int port, InetAddress broadcastAdress) throws SocketException {
        this.user=user;
        this.port=port;
        this.broadcastAdress =broadcastAdress;
        this.dgramSocket = new DatagramSocket(port);
    }

    public void broadcastPseudo() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.myBuffer = new byte[1024];         // On initialise le buffer pour envoyer le pseudo
        this.msg ="pseudo:"+this.user.userPseudo+";";    // On créé le message contenant le pseudo
        DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), this.broadcastAdress, this.port);  // On construit le paquet
        this.dgramSocket.send(packet);   // On envoie le paquet

        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void broadcastConfirmationPseudo() throws IOException  //Pas différente de broadcast pseudo juste on confirme que le pseudo est libre et qu'on le prend à l'ensemble du réseau
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.myBuffer = new byte[1024];         // On initialise le buffer pour envoyer le pseudo
        this.msg ="resonse:ok/pseudo:"+this.user.userPseudo+";";    // On créé le message contenant le pseudo
        DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), this.broadcastAdress, this.port);  // On construit le paquet
        this.dgramSocket.send(packet);   // On envoie le paquet

        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    // il faut que les ports soient differents parce que pas sur le meme pc

    public void attendreRequetePseudo() {  //Cette fonction est voué à disparaitre et est remplace par Attendre message    // a changer pour ne plus avoir le traitement dans la fonction

        String msgrecu = "";
        Boolean response = null;
        DatagramPacket paquetRecu;
        int debutPseudo;
        int finPseudo;

        try{
            // On essaye de créer notre socket serveur UDP
            this.dgramSocket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);
        }
        catch (SocketException se){
            se.printStackTrace();
        }


        byte[] receiveData = new byte[1024];   // On initialise les trames qui vont servir à recevoir et envoyer les paquets


        // Tant qu'on est connecté, on attend une requête et on y répond

        try{
            paquetRecu = new DatagramPacket(receiveData, receiveData.length);
            this.dgramSocket.receive(paquetRecu);
            msgrecu= new String(paquetRecu.getData());

            debutPseudo = msgrecu.indexOf("pseudo:")+"pseudo:".length(); //On détermine l'Indexe du début de pseudo dans le String recu
            finPseudo = msgrecu.indexOf(";");                           //On sait que le dernier élément envoyé dans le broadcast est un ";"


            //traitement de msgrecu
            if(msgrecu.substring(debutPseudo,finPseudo).equals(this.user.userPseudo)){
                response = false;
            }
            else{
                response = true;
            }
            envoyerResponse(paquetRecu.getAddress(),paquetRecu.getPort(),response,InetAddress.getLocalHost().toString()); // Le newPortCommunication est le port ou on ira discuter en tcp ( peut etre à changer à voir )

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public String attendreMessage(){

        DatagramPacket receivedDatagram;
        byte[] receiveData = new byte[1024];   //On initialise le buffer de reception
        String msgrecu;

        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);   //On set le timoutout si aucun message n'est recu alors on lève la socketTimeoutException
            this.dgramSocket.receive(receivedDatagram);                 // On attend la réception d'un message
        }
        catch (SocketException se){
            se.printStackTrace();              //Exception en cas d'erreur avec le socket
        }
        catch(SocketTimeoutException e){
            msgrecu = null;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        msgrecu = new String(receivedDatagram.getData());   //On transforme la data recu en String
        return msgrecu;
    }

    public void envoyerResponse(InetAddress adrDest, int portDest, boolean resp, String MyAddr) throws IOException {
        String msg;
        DatagramPacket packet;
        if (resp){   //Si le pseudo n'est pas pris par l'utilisateur qui envoie la response après récèption du broadcast alors resp = true
            msg = "/Pseudo:"+this.user.userPseudo+"/Adresse:"+MyAddr+""+"/fin";       // On construit le message
            packet = new DatagramPacket(msg.getBytes(), msg.length(), adrDest, portDest);   // On crée le paquet datagram
        }
        else{
            msg = "non";
            packet = new DatagramPacket(msg.getBytes(), msg.length(), adrDest, portDest);
        }
        this.dgramSocket.send(packet);      // On envoie le message
    }





}

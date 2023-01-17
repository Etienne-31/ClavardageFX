package project.application.Manager;

import project.application.App.App;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class udpManager extends  Thread{

    private static final int TIMEOUT_RECEPTION_REPONSE = 5000;
    public DatagramSocket dgramSocket = null;
    public byte[] myBuffer = null;
    public DatagramPacket inPacket = null;
    public String msg;
    public Utilisateur user;
    public int port;




    public udpManager(Utilisateur user, int port) throws SocketException {
        this.user=user;
        this.port=port;
        this.dgramSocket = new DatagramSocket(port);
    }

    @Override
    public void run(){
        MessageManager messageManager;
        DatagramPacket paquet;


        while(App.connected){
            paquet = attendreMessage();
            if(paquet != null){
                messageManager = new MessageManager(paquet);
                messageManager.start();
            }
        }
    }

    public void broadcastPseudo() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
            System.out.println("-------------Socket broadcast activé");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.myBuffer = new byte[1024];         // On initialise le buffer pour envoyer le pseudo
        this.msg ="objet:demandePseudo/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On créé le message contenant le pseudo

        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){

            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
            System.out.println("-------------"+"User : "+this.user.getIdUser()+" Je vais Broadcast à l'adresse : "+broadcastAdress.toString());
            this.dgramSocket.send(packet);   // On envoie le paquet
            System.out.println("-------------"+"User : "+this.user.getIdUser()+"Broadcast de demandee envoyé à l'adresse : "+broadcastAdress.toString());
        }


        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
            System.out.println("-------------Socket broadcast desactivé");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void broadcastDeconnexion() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
            System.out.println("-------------Socket broadcast activé");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.myBuffer = new byte[1024];         // On initialise le buffer pour envoyer le pseudo
        this.msg ="objet:deconnexion/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On créé le message prévenant de notre deconnexion
        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){
            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
            this.dgramSocket.send(packet);
            System.out.println("-------------"+"User : "+this.user.getIdUser()+"Broadcast de demandee envoyé à l'adresse : "+broadcastAdress.toString());
        }



        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
            System.out.println("-------------Socket broadcast desactivé");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void broadcastConfirmationPseudo() throws IOException  //Pas différente de broadcast pseudo juste on confirme que le pseudo est libre et qu'on le prend à l'ensemble du réseau
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
            System.out.println("-------------Socket broadcast activé");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.myBuffer = new byte[1024];         // On initialise le buffer pour envoyer le pseudo
        this.msg ="objet:confirmationPseudo/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On créé le message confirmant
        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){
            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
            this.dgramSocket.send(packet);   // On envoie le paquet
            System.out.println("-------------"+"User : "+this.user.getIdUser()+"Broadcast de demandee envoyé à l'adresse : "+broadcastAdress.toString());
        }



        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
            System.out.println("-------------Socket broadcast desactivé");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }


    public DatagramPacket attendreMessage(){

        DatagramPacket receivedDatagram;
        byte[] receiveData = new byte[1024];   //On initialise le buffer de reception


        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);   //On set le timoutout si aucun message n'est recu alors on lève la socketTimeoutException
            this.dgramSocket.receive(receivedDatagram);                 // On attend la réception d'un message
        }
        catch (SocketException se){
            se.printStackTrace();              //Exception en cas d'erreur avec le socket
        }
        catch(SocketTimeoutException e){
            return null;                //Si cette exception est levée alors cette méthode renverra null
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return receivedDatagram;   //Si le datagram n'est pas null, on le renvoie tout simplement
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

    public List<InetAddress> getBroadcastAddresses() {
        List<InetAddress> broadcastAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isLoopback() && ni.isUp()) {
                    for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                        InetAddress broadcast = ia.getBroadcast();
                        if (broadcast != null) {
                            broadcastAddresses.add(broadcast);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return broadcastAddresses;
    }






}

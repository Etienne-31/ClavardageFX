package project.application.Manager;

import org.hibernate.procedure.internal.Util;
import project.application.App.App;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class udpManager extends  Thread{


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

    public DatagramSocket getDgramSocket(){return this.dgramSocket;}

    @Override
    public void run(){
        MessageManager messageManager;
        DatagramPacket paquet = null;
        try {
            this.dgramSocket.setSoTimeout(0);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while(App.connected){
            try {
                synchronized (this.dgramSocket){
                    paquet = attendreMessage();
                }
                if(!(paquet.getAddress() == null)){
                    boolean sameIP = App.udpManager.checkIP(paquet.getAddress());
                    if(sameIP){paquet = null;}
                }
                else{
                    paquet = null;
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if(paquet != null){
                String msg = new String(paquet.getData());
                System.out.println("From udpManager run() Nouveau messages udp / Le message : "+msg);
                messageManager = new MessageManager(paquet);
                messageManager.start();
            }
        }

        //System.out.println("Fin du thread de gestion des messages udp");
    }

    public void broadcastPseudo() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
            //System.out.println("-------------Socket broadcast activ??");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }


        this.msg ="objet:demandePseudo/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On cr???? le message contenant le pseudo
        this.myBuffer = new byte[this.msg.length()];         // On initialise le buffer pour envoyer le pseudo
        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){

            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
           // System.out.println("-------------"+"User : "+this.user.getIdUser()+" Je vais Broadcast la demande de pseudo ?? l'adresse : "+broadcastAdress.toString()+" avec le message "+this.msg);
            this.dgramSocket.send(packet);   // On envoie le paquet
           // System.out.println("-------------"+"User : "+this.user.getIdUser()+" Broadcast de demande de pseudo envoy?? ?? l'adresse : "+broadcastAdress.toString());

        }


        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
           // System.out.println("-------------Socket broadcast desactiv??");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void broadcastDeconnexion() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
            //System.out.println("-------------Socket broadcast activ??");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }


        this.msg ="objet:deconnexion/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On cr???? le message pr??venant de notre deconnexion
        this.myBuffer = new byte[this.msg.length()];         // On initialise le buffer pour envoyer le pseudo
        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){
            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
            this.dgramSocket.send(packet);
           // System.out.println("-------------"+"User : "+this.user.getIdUser()+"Broadcast de demande de d??connexion envoy?? ?? l'adresse : "+broadcastAdress.toString());
        }



        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
            //System.out.println("-------------Socket broadcast desactiv??");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void broadcastConfirmationPseudo() throws IOException  //Pas diff??rente de broadcast pseudo juste on confirme que le pseudo est libre et qu'on le prend ?? l'ensemble du r??seau
    {
        try{
            dgramSocket.setBroadcast(true);// On active le broadcast
           // System.out.println("-------------Socket broadcast activ??");
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e); //On affiche l'erreur en cas d'exception
        }

        this.msg ="objet:confirmationPseudo/finObjet/"+"pseudo:"+this.user.userPseudo+";";    // On cr???? le message
        this.myBuffer = new byte[this.msg.length()];         // On initialise le buffer pour envoyer le pseudo
        for(InetAddress broadcastAdress : this.getBroadcastAddresses()){
            DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), broadcastAdress, this.port);  // On construit le paquet
            this.dgramSocket.send(packet);   // On envoie le paquet
           // System.out.println("-------------"+"User : "+this.user.getIdUser()+" Broadcast de confirmation envoy?? ?? l'adresse : "+broadcastAdress.toString());
        }



        try{
            dgramSocket.setBroadcast(false); // Desactive le broadcast
           // System.out.println("-------------Socket broadcast desactiv??");
        }
        catch(SocketException e){
            e.printStackTrace();
        }
    }

    public DatagramPacket attendreMessage() throws UnknownHostException {
        Boolean sameIP;
        DatagramPacket receivedDatagram;
        byte[] receiveData = new byte[500];   //On initialise le buffer de reception


        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.receive(receivedDatagram);                 // On attend la r??ception d'un message
        }
        catch (SocketException se){
            se.printStackTrace();              //Exception en cas d'erreur avec le socket
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return receivedDatagram;   //Si le datagram n'est pas null, on le renvoie tout simplement
    }


    public DatagramPacket attendreMessageTO() throws UnknownHostException {
        Boolean sameIP;
        DatagramPacket receivedDatagram;
        int timeout = 5000;
        byte[] receiveData = new byte[500];   //On initialise le buffer de reception


        receivedDatagram = new DatagramPacket(receiveData,receiveData.length);

        try{
            this.dgramSocket.setSoTimeout(timeout);   //On set le timoutout si aucun message n'est recu alors on l??ve la socketTimeoutException
            this.dgramSocket.receive(receivedDatagram);                 // On attend la r??ception d'un message
        }
        catch (SocketException se){
            se.printStackTrace();              //Exception en cas d'erreur avec le socket
        }
        catch(SocketTimeoutException e){
            System.out.println("From udpManager attendreMessage() : le timeout a expir?? ");
            return null;                //Si cette exception est lev??e alors cette m??thode renverra null
        }
        catch(IOException e){
            e.printStackTrace();
        }
        String msg = new String(receivedDatagram.getData());
       // System.out.println("From attendreMessageTO() : Valeur du datagram :"+msg);
        return receivedDatagram;   //Si le datagram n'est pas null, on le renvoie tout simplement
    }



    public void envoyerResponse(InetAddress adrDest, int portDest, boolean resp, String MyAddr) throws IOException {
        String msg;
        DatagramPacket packet;
        if (resp){   //Si le pseudo n'est pas pris par l'utilisateur qui envoie la response apr??s r??c??ption du broadcast alors resp = true
            msg = "objet:AcceptationPseudo/finObjet/Response:oui/finResponse"+"/Pseudo:"+this.user.userPseudo+"/Adresse:"+MyAddr+""+"/finAdresse";       // On construit le messag
        }
        else{
            msg = "objet:AcceptationPseudo/finObjet/Response:non/finResponse/fin";

        }
        packet = new DatagramPacket(msg.getBytes(), msg.length(), adrDest, portDest);   // On cr??e le paquet datagram
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

    public  boolean checkIP(InetAddress address) {
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

package project.application.Manager;

import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
public class udpManager {

    private static final int TIMEOUT_RECEPTION_REPONSE = 5000;
    public DatagramSocket dgramSocket = null;
    public byte[] myBuffer = null;
    public DatagramPacket inPacket = null;
    public String msg = "";
    public Utilisateur user;
    public int port;
    public InetAddress broadcastAdress;

    public udpManager(Utilisateur user, int port, InetAddress broadcastAdress) throws SocketException {
        this.user=user;
        this.port=port;
        this.broadcastAdress =broadcastAdress;
        this.dgramSocket = new DatagramSocket(port);
    }

    public void envoyerBoradcastUDP() throws IOException
    {
        try{
            dgramSocket.setBroadcast(true);
        }
        catch(SocketException e){
            System.out.println("Erreur lors de l'initialisation du serveur UDP : "+e);
        }
        this.myBuffer = new byte[1024];
        this.msg += this.user.userPseudo;
        DatagramPacket packet = new DatagramPacket(this.msg.getBytes(), this.msg.length(), this.broadcastAdress, this.port);
        this.dgramSocket.send(packet);
    }

    // il faut que les ports soient differents parce que pas sur le meme pc

    public void attendreRequete(int newPortCommunication) {

        String msgrecu = "";
        Boolean response = null;
        DatagramPacket paquetRecu;

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


            //traitement de msgrecu
            if(msgrecu.substring(0,msgrecu.indexOf(";")).equals(this.user.userPseudo)){
                response = false;
            }
            else{
                response = true;
            }
            envoyerResponse(paquetRecu.getAddress(),paquetRecu.getPort(),response,newPortCommunication,InetAddress.getLocalHost().toString()); // Le newPortCommunication est le port ou on ira discuter en tcp ( peut etre à changer à voir )


            //pour recuperer l'adresse du paquet au lieu de la separer du paquet
            //InetAddress IPAddress = paquetRecu.getAddress();

            /*int port = paquetRecu.getPort();*/
            // Si on reçoit un "ping", on répond "pong" à celui qui nous l'a envoyé
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void envoyerResponse(InetAddress adrDest, int portDest, boolean resp, int myPort, String MyAddr) throws IOException {
        String msg;
        DatagramPacket packet;


        if (resp){
            msg = "oui;"+MyAddr+";"+String.valueOf(myPort);
            packet = new DatagramPacket(msg.getBytes(), msg.length(), adrDest, portDest);
        }
        else{
            msg = "non";
            packet = new DatagramPacket(msg.getBytes(), msg.length(), adrDest, portDest);
        }
        this.dgramSocket.send(packet);
    }





}

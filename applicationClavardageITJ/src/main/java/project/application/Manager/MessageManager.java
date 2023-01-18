package project.application.Manager;

import org.hibernate.procedure.internal.Util;
import project.application.App.App;
import project.application.Models.Utilisateur;

import java.io.IOException;
import java.net.DatagramPacket;

public class MessageManager extends Thread{   // On lance un thread pour gérer la gestion des datagram recu dans la boucle d'attente lancé par le thread de udpManager

    private final DatagramPacket paquet;

    public MessageManager(DatagramPacket paquetAtraiter){
        this.paquet = paquetAtraiter;

    }

    public void run(){
        String message = new String(this.paquet.getData());
        String objet = null;
        int debutObjet = message.indexOf("objet:")+"objet:".length();
        int finObjet = message.indexOf("/finObjet/");
        objet = message.substring(debutObjet,finObjet);                // On vient chercher l'objet du message et en fonction de cet objet on aura un traitement différent
        int debutPseudo = message.indexOf("pseudo:")+"pseudo:".length();

        int finPseudo = message.indexOf(";");
        String pseudo = message.substring(debutPseudo,finPseudo);
        System.out.println("L'objet du message est "+objet);



        if(objet.equals("demandePseudo")){
            Boolean response;

            if(App.user.userPseudo.equals(pseudo)){
                response = false;
            }
            else{
                response = true;
            }
            System.out.println("La response que je vais envoyer est :"+response);
            try {
                App.udpManager.envoyerResponse(this.paquet.getAddress(),this.paquet.getPort(),response,App.user.getUserIpAdress().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else if(objet.equals("confirmationPseudo")){System.out.println("Le message est sur la confirmation de pseudo");
            Utilisateur nouvelAjout = new Utilisateur(pseudo,paquet.getAddress());
            App.userAnnuaire.getAnnuaire().put(pseudo,nouvelAjout);


        }
        else if(objet.equals("deconnexion")){
            App.userAnnuaire.getAnnuaire().remove(pseudo);

        }




    }


}

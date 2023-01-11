package project.application.Models;
import java.net.*;

public class Utilisateur {
    private String idUser;

    public String userPseudo;
    private InetAddress ipUser;




    //Constructeur pour créer un utilisateur sans attribut ( utile au lancement de l'app )

    public Utilisateur() throws UnknownHostException {
        this.ipUser = InetAddress.getLocalHost();

    }



    //Constructeur pour construire l autre utilisateur
    public Utilisateur(String pseudoOtherUser,InetAddress adress){

        this.ipUser = adress;
        this.idUser = null;
        this.userPseudo = pseudoOtherUser;

    }


    //Constructeur pour construire l'utilisateur

    public Utilisateur(String idUser,String pseudo){

        try {
            this.idUser = idUser;
            this.userPseudo = pseudo;
            this.ipUser = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e){
            System.out.println("Erreur venant du constructeur Utilisateur"+e);

        }
    }

    public String getIdUser() {
        return this.idUser;
    }


    public InetAddress getUserIpAdress() {
        return this.ipUser;
    }

    public void setPseudo(String newPseudo) {
        this.userPseudo = newPseudo;
    }

    public void setId(String idUser){this.idUser = idUser;}
}

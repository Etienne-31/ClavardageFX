package project.application.Models;
import java.net.*;
public class Utilisateur {
    private String idUser;
    private String password;
    public String userPseudo;
    private InetAddress ipUser;



    //Constructeur pour construire l autre utilisateur
    public Utilisateur(String idOtherUser,String pseudoOtherUser){
        this.password = null;
        this.ipUser = null;
        this.idUser = idOtherUser;
        this.userPseudo = pseudoOtherUser;

    }


    //Constructeur pour construire l'utilisateur
    public Utilisateur(String idUser,String password,String pseudo){

        try {
            this.idUser = idUser;
            this.password = password;
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

    public String getPassword() {
        return this.password;
    }

    public InetAddress getUserIpAdress() {
        return this.ipUser;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setPseudo(String newPseudo) {
        this.userPseudo = newPseudo;
    }
}

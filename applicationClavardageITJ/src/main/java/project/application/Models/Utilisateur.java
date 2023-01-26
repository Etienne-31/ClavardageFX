package project.application.Models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.net.*;




public class Utilisateur {

    private String idUser;

    private String password;

    public String userPseudo;


    private InetAddress ipUser;

    private Integer portOuContacter;




    //Constructeur pour créer un utilisateur sans attribut ( utile au lancement de l'app )
    public Utilisateur() throws UnknownHostException {
        this.ipUser = InetAddress.getLocalHost();
        this.userPseudo = null;
        this.portOuContacter = null;

    }



    //Constructeur pour construire l autre utilisateur
    public Utilisateur(String pseudoOtherUser,InetAddress adress){

        this.ipUser = adress;
        this.idUser = null;
        this.password = null;
        this.userPseudo = pseudoOtherUser;
        this.portOuContacter = null;

    }


    //Constructeur pour construire l'utilisateur

    public Utilisateur(String idUser,String pseudo){

        try {
            this.idUser = idUser;
            this.userPseudo = pseudo;
            this.ipUser = InetAddress.getLocalHost();
            this.password = null;
            this.portOuContacter = null;
        }
        catch(UnknownHostException e){
            System.out.println("Erreur venant du constructeur Utilisateur"+e);

        }
    }

    public int getPortOuContacter(){
        return this.portOuContacter;
    }

    public void setPortOuContacter(Integer portOuContacter) {
        this.portOuContacter = portOuContacter;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public InetAddress getIpUser() {
        return this.ipUser;
    }
    public void setIpUser(InetAddress ip){this.ipUser = ip;}

    public String getUserPseudo() {
        return userPseudo;
    }

    public void setUserPseudo(String newPseudo) {
        this.userPseudo = newPseudo;
    }

    public void setId(String idUser){this.idUser = idUser;}


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void erazeInfo(){
        this.setPassword("");
        this.setUserPseudo("");
    }
}

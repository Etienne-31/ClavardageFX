package project.application.Models;
import java.net.*;


public class Utilisateur {

    private String idUser;

    private String password;

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
        this.password = null;
        this.userPseudo = pseudoOtherUser;

    }


    //Constructeur pour construire l'utilisateur

    public Utilisateur(String idUser,String pseudo){

        try {
            this.idUser = idUser;
            this.userPseudo = pseudo;
            this.ipUser = InetAddress.getLocalHost();
            this.password = null;
        }
        catch(UnknownHostException e){
            System.out.println("Erreur venant du constructeur Utilisateur"+e);

        }
    }

    public String getIdUser() {
        return this.idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public InetAddress getUserIpAdress() {
        return this.ipUser;
    }

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

package project.application.Models;
import java.util.HashMap;

public class Annuaire {
    private HashMap<String,Utilisateur> annuaire;

    public Annuaire(){
        annuaire = new HashMap<String,Utilisateur>();
    }

    public HashMap<String, Utilisateur> getAnnuaire() {
        return annuaire;
    }
}

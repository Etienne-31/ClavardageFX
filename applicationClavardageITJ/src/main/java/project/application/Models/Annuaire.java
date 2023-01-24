package project.application.Models;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.collections.FXCollections;
import java.util.*;

public class Annuaire {

    private ObservableMap<String, Utilisateur> annuaire;

    public Annuaire() {
        this.annuaire = FXCollections.observableHashMap();
    }

    public ObservableMap<String, Utilisateur> getAnnuaire() {
        return this.annuaire;
    }

    public Set<String> getSetPSeudo() {
        return annuaire.keySet();
    }

    public Utilisateur getUserFromAnnuaire(String pseudo) {
        return annuaire.get(pseudo);
    }

    public void addAnnuaire(String pseudo, Utilisateur newUser) {
        Platform.runLater(() -> {
            annuaire.put(pseudo, newUser);
        });
    }

    public void deleteFromAnnuaire(String pseudo) {
        Platform.runLater(() -> {
            annuaire.remove(pseudo);
        });
    }

    public void clearAnnuaire() {
        Platform.runLater(() -> { annuaire.clear();
        });
    }

    public int getNumberOfConnected() {
        return annuaire.size();
    }

    public ArrayList<String> getListPseudos() {
        ArrayList<String> pseudoList = new ArrayList<>();
        for (String pseudo : annuaire.keySet()) {
            pseudoList.add(pseudo);
        }
        return pseudoList;
    }

    public ArrayList<Utilisateur> getListUsers() {
        ArrayList<Utilisateur> userList = new ArrayList<>();
        for (String pseudo : annuaire.keySet()) {
            userList.add(annuaire.get(pseudo));
        }
        return userList;
    }



}

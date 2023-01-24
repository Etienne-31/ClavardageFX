package project.application.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="Utilisateur")
public class UserLogin {

    private String idUser;
    private String password;


    public UserLogin(){}
    public UserLogin(String id,String password){
        this.idUser = id;
        this.password = password;
    }

    @Id
    @Column(name="idUser")
    public String getIdUser() {
        return this.idUser;
    }
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Id
    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

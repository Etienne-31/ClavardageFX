package project.application.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;




@Entity
@Table(name="Messages")
public class Messages {
    private String idSender;

    private String idReceiver;

    private Timestamp timestamp;


    private String Data;




    public Messages(){}

    public Messages(Utilisateur idSender, Utilisateur idReceiver, String Data){
        this.idSender = idSender.getIdUser();
        this.idReceiver = idReceiver.getIdUser();
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.Data = Data;
    }


    @Id
    @Column(name = "idSender")
    public String getIdSender() {
        return idSender;
    }
    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }




    @Id
    @Column(name = "idReceiver")
    public String getIdReceiver() {
        return idReceiver;
    }
    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }


    @Id
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp){this.timestamp = timestamp;}


    public void setTimestampFromLocalDateTime(LocalDateTime timestamp) {
        this.timestamp = Timestamp.valueOf(timestamp);
    }


    @Id
    @Column(name="data")
    public String getData(){
        return this.Data;
    }
    public void setData(String data) {
        Data = data;
    }

    public String toString(){
        return "Message recu / Id sender :"+getIdSender()+" / Id receiver :"+getIdReceiver()+" / Timestamp : "+getTimestamp()+" \n"+" Message : "+getData();

    }

}

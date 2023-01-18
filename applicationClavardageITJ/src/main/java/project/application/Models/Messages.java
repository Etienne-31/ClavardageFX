package project.application.Models;

import java.time.LocalDateTime;





public class Messages {
    private String idSender;

    private String idReceiver;

    private LocalDateTime timestamp;


    private String Data;

    public Messages(){}
    public Messages(Utilisateur idSender, Utilisateur idReceiver, String Data){
        this.idSender = idSender.getIdUser();
        this.idReceiver = idReceiver.getIdUser();
        this.timestamp = LocalDateTime.now();
        this.Data = Data;
    }
    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }
    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
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

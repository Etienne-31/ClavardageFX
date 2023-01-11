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
    public String getidSender(){
        return this.idSender;
    }
    public String getidReceiver(){
        return this.idReceiver;
    }
    public LocalDateTime gettimestamp(){
        return this.timestamp;
    }
    public String getData(){
        return this.Data;
    }

    public String toString(){
        return "Message recu / Id sender :"+getidSender()+" / Id receiver :"+getidReceiver()+" / Timestamp : "+gettimestamp()+" \n"+" Message : "+getData();

    }

}

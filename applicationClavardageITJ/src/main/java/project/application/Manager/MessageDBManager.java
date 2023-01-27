package project.application.Manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import project.application.Models.Hibernate;
import project.application.Models.Messages;
import project.application.Models.UserLogin;

import java.util.List;

public class MessageDBManager {

    public static void InsertDetached(Messages messagesToStore){
        SessionFactory factory = Hibernate.getSessionFactory();
        Session session2 = factory.openSession();

        try{

            session2.getTransaction().begin();
            session2.save(messagesToStore);

            session2.flush();
            session2.getTransaction().commit();
            Hibernate.shutdown();

        }
        catch(Exception e){
            e.printStackTrace();
            session2.getTransaction().rollback();
        }


    }

    public static List<Messages> getListMessageConv(){
        List<Messages> listMessages;
        Session session = Hibernate.getSessionFactory().openSession();



        session.close();
        return listMessages;
    }





}

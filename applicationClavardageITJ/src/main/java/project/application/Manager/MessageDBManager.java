package project.application.Manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

    public static List<Messages> getListMessageConv(String idSender ,String idReceiver){
        List<Messages> listMessages = null;
        Session session = Hibernate.getSessionFactory().openSession();
        String hql = " FROM Messages WHERE idSender = :sender OR idReceiver = :receiver ORDER BY timestamp";
        try {
            session.getTransaction().begin();
            Query query = session.createQuery(hql);

            query.setParameter("sender", idSender);
            query.setParameter("receiver", idReceiver);

            listMessages = query.list();
            session.getTransaction().commit();
            Hibernate.shutdown();
        }
        catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }


        session.close();
        return listMessages;
    }





}

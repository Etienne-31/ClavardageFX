package project.application.Manager;

import java.net.UnknownHostException;
import java.util.List;
import project.application.Models.Utilisateur;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import project.application.Models.Hibernate;
import org.hibernate.*;
import project.application.Models.Utilisateur;

public class UserDBManager {

    public static void UpdtateDetached(Utilisateur utilisateur){
        SessionFactory factory = Hibernate.getSessionFactory();
        Session session2 = factory.openSession();

        try{

            session2.getTransaction().begin();
            session2.merge(utilisateur);

            session2.flush();
            session2.getTransaction().commit();
            Hibernate.shutdown();

        }
        catch(Exception e){
            e.printStackTrace();
            session2.getTransaction().rollback();
        }


    }

   public static void InsertDetached(Utilisateur utilisateur){
        SessionFactory factory = Hibernate.getSessionFactory();
        Session session2 = factory.openSession();

        try{

            session2.getTransaction().begin();
            session2.save(utilisateur);

            session2.flush();
            session2.getTransaction().commit();
            Hibernate.shutdown();

        }
        catch(Exception e){
            e.printStackTrace();
            session2.getTransaction().rollback();
        }


    }

   public static List<Utilisateur> getListUser(String  idUser){
        SessionFactory factory = Hibernate.getSessionFactory();
        Session session = factory.openSession();
        List<Utilisateur> userList = null;
        try{

            String sql = "SELECT user.password, user.idUser from "+Utilisateur.class.getName()+" user"+" where user.idUser="+idUser;

            Query query = session.createQuery(sql);
            userList = query.getResultList();
            session.getTransaction().commit();
            Hibernate.shutdown();

        }catch(Exception exception){
            exception.printStackTrace();
            session.getTransaction().rollback();
        }

        return userList;

    }

    public static Utilisateur getUtilisateur(String idUser) throws UnknownHostException {
        SessionFactory factory = Hibernate.getSessionFactory();
        System.out.println("Factory crée");
        Session session = factory.openSession();
        System.out.println("Session crée");
        Utilisateur userFromDB = new Utilisateur();
        List<Utilisateur> list = null;

        String hql = "SELECT user FROM "+Utilisateur.class.getName()+" user"+"WHERE user.idUser="+idUser;
        try {
            session.getTransaction().begin();
            Query query = session.createQuery(hql);

            list = query.getResultList();
            session.getTransaction().commit();
            Hibernate.shutdown();
        }
        catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        for(Utilisateur ite : list){
            userFromDB.setId(ite.getIdUser());
            userFromDB.setPassword(ite.getPassword());
        }

        return  userFromDB;
    }

}

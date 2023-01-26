package project.application.Manager;

import java.net.UnknownHostException;
import java.util.List;

import project.application.Models.UserLogin;
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

   public static void InsertDetached(UserLogin utilisateur){
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

  public static List<String> getListUser() {
      Session session = Hibernate.getSessionFactory().openSession();
       String hql = "SELECT idUser FROM UserLogin ";

     // String hql = "SELECT u FROM "+Utilisateur.class.getName()+ " u WHERE u.idUser = :id";
      Query query = session.createQuery(hql);

      List<String> utilisateurs = query.list();
      session.close();
      return utilisateurs;
  }

    public static UserLogin getUtilisateur(String idUser) throws UnknownHostException {
        SessionFactory factory = Hibernate.getSessionFactory();
        //System.out.println("Factory crée");
        Session session = factory.openSession();
       // System.out.println("Session crée");
        UserLogin userFromDB = new UserLogin();
        List<UserLogin> list = null;

        String hql = " FROM  UserLogin  WHERE idUser = :id";

        try {
            session.getTransaction().begin();
            Query query = session.createQuery(hql);
            query.setParameter("id", idUser);
            list = query.list();
            session.getTransaction().commit();
            Hibernate.shutdown();
        }
        catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        for(UserLogin ite : list){
            if(ite.getIdUser().equals(idUser)){
                userFromDB.setIdUser(ite.getIdUser());
                userFromDB.setPassword(ite.getPassword());
            }
        }
        return  userFromDB;
    }

}

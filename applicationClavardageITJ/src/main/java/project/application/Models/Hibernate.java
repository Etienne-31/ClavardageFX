package project.application.Models;
import org.hibernate.SessionFactory;

import org.hibernate.MappingException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate {

    private static SessionFactory setUp(){
        SessionFactory sessionFactory = null;
        ServiceRegistry registery = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try{
            System.out.println("Building sessionsFactory");
            sessionFactory = new MetadataSources(registery)
                    .buildMetadata()
                    .buildSessionFactory();

        }
        catch (Exception e){
            StandardServiceRegistryBuilder.destroy(registery);
            e.printStackTrace();

        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(){
        return setUp();
    }

    public static void shutdown(){
        getSessionFactory().close();
    }
}

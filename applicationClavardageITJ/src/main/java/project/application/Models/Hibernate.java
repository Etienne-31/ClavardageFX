package project.application.Models;
import org.hibernate.SessionFactory;

import org.hibernate.MappingException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class Hibernate {
    public static void innit() throws Exception {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("/project/application/ressources/hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private static SessionFactory buildSessionFactory(){

        try{
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch(Throwable Exception){
            System.err.println("La création de la sessionFactory a échoué : "+Exception);
            throw new ExceptionInInitializerError(Exception);
        }

    }

    public static SessionFactory getSessionFactory(){
        return buildSessionFactory();
    }

    public static void shutdown(){
        getSessionFactory().close();
    }
}

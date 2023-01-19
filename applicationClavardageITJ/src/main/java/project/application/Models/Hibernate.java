package project.application.Models;
import org.hibernate.SessionFactory;

import org.hibernate.MappingException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate {
    public static void innit() throws Exception {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private static SessionFactory buildSessionFactory(){

        /*try{
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("applicationClavardageITJ/src/main/resources/hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch(Throwable Exception){
            System.err.println("La création de la sessionFactory a échoué : "+Exception);
            throw new ExceptionInInitializerError(Exception);
        }*/
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  sessionFactory;
    }



    public static SessionFactory getSessionFactory(){
        return buildSessionFactory();
    }

    public static void shutdown(){
        getSessionFactory().close();
    }
}

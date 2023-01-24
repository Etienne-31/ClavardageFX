module project.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires  org.hibernate.orm.core;
    requires  java.naming;
    requires java.sql;
    requires mysql.connector.java;
    requires jakarta.persistence;

    opens project.application to javafx.fxml;
    exports project.application.App;
    opens project.application.App to javafx.fxml;
    exports project.application.Controlers;
    opens project.application.Controlers to javafx.fxml;
    exports project.application.Manager;
    opens project.application.Manager to javafx.fxml;
    exports project.application.Models;
    opens project.application.Models to javafx.fxml;

    
}
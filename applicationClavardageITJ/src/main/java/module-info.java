module project.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires  org.hibernate.orm.core;

    opens project.application to javafx.fxml;
    exports project.application.App;
    opens project.application.App to javafx.fxml;
    exports project.application.Controlers;
    opens project.application.Controlers to javafx.fxml;
    exports project.application.Manager;
    opens project.application.Manager to javafx.fxml;

    
}
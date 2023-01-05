module project.application {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.application to javafx.fxml;
    exports project.application;
    exports project.application.App;
    opens project.application.App to javafx.fxml;
    exports project.application.Controlers;
    opens project.application.Controlers to javafx.fxml;
}
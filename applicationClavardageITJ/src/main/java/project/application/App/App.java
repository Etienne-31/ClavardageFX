package project.application.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;
        System.out.println("Debut");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/project/application/welcomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        System.out.println("milieu");

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
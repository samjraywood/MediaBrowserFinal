package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UI/MediaBrowser.fxml"));
        primaryStage.setTitle("Media Browser");
        primaryStage.setMaxHeight(500);
        primaryStage.setMaxWidth(850);
        Scene scene = new Scene(root, 850, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

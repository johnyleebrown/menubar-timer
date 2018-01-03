package app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        setPrimaryStage(primaryStage);

        Pane pane = FXMLLoader.load(getClass().getResource("app.fxml"));
        Scene scene = new Scene(pane, 534, 148);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

package main;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    private static final String trayIcon =
            "http://icons.iconarchive.com/icons/iconsmind/outline/16/Clock-icon.png";

    String secondIcon = "http://icons.iconarchive.com/icons/icons8/ios7/16/Time-And-Date-Clock-icon.png";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(false);

        javax.swing.SwingUtilities.invokeLater(this::addTrayIcon);

        Pane pane = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(pane, 534, 148);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        setPrimaryStage(primaryStage);
    }

    private void addTrayIcon() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(new URL(trayIcon));
            TrayIcon trayIcon = new TrayIcon(image);
            final PopupMenu popup = new PopupMenu();

            MenuItem prefItem = new MenuItem("Preferences");
            prefItem.addActionListener(event -> Platform.runLater(this::showPrimaryStage));
            popup.add(prefItem);

            popup.addSeparator();

            MenuItem exitItem = new MenuItem("Quit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    private void showPrimaryStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    private void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    static public Stage getPrimaryStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

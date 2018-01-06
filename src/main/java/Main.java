
import java.awt.*;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

    @FXML
    private PaneController paneController;

    private static Stage stage;

    private static TrayIcon trayIcon = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pane.fxml"));
        Pane pane = loader.load();
        paneController = loader.getController();

        Platform.setImplicitExit(false);

        javax.swing.SwingUtilities.invokeLater(this::addTrayIcon);

        Scene scene = new Scene(pane, 534, 148);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(this::onClose);

        setPrimaryStage(primaryStage);
    }

    private void addTrayIcon() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            final Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon2.png"));
            trayIcon = new TrayIcon(image);

            final PopupMenu popupStart = new PopupMenu();
            final PopupMenu popupStop = new PopupMenu();

            MenuItem pauseItem = new MenuItem("Pause");
            pauseItem.addActionListener(event ->
                    System.out.println("Pause")
            );

            MenuItem stopItem = new MenuItem("Stop");
            stopItem.addActionListener(event -> {
                System.out.println("Stop");
                Helper.printDate();
                FxTimer.getInstance().stopTimer();
            });

            MenuItem prefItem = new MenuItem("Preferences");
            prefItem.addActionListener(event -> {
                Platform.runLater(this::showPrimaryStage);
            });

            MenuItem exitItem = new MenuItem("Quit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            MenuItem startItem = new MenuItem("Start");
            startItem.addActionListener(event -> {
                System.out.println("Start");


                tray.remove(trayIcon);
                popupStop.add(pauseItem);
                popupStop.add(stopItem);
                popupStop.add(prefItem);
                popupStop.addSeparator();
                popupStop.add(exitItem);
                trayIcon.setPopupMenu(popupStop);
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });

            popupStart.add(startItem);
            popupStart.add(prefItem);
            popupStart.addSeparator();
            popupStart.add(exitItem);
            trayIcon.setPopupMenu(popupStart);
            tray.add(trayIcon);
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

    private void onClose(WindowEvent event) {
        paneController.setCurrentTimer();
    }

    private void setPrimaryStage(Stage stage) {
        Main.stage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

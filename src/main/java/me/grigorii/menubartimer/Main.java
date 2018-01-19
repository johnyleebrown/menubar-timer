package me.grigorii.menubartimer;

import java.awt.*;

import javax.swing.*;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.events.TileEvent;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.grigorii.menubartimer.notification.NotificationFactory;

/**
 * Created by github.com/johnyleebrown
 */
public class Main extends Application {

    // Nodes
    private Stage stage;
    private FlowGridPane pane;
    private TrayIcon trayIcon;
    private Tile sliderWorkTimeTile;
    private Tile sliderRestTimeTile;
    private Tile plusMinusCyclesTile;
    private Tile startTile;
    private Tile pauseTile;
    private Tile stopTile;
    private Tile resumeTile;

    // Timer
    private boolean isRunning = false;
    private Integer workMinutes;
    private Integer restMinutes;
    private Integer cycles;

    // Icon
    private final Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/Icon-16.png"));
    private PopupMenu popupMenu;
    private MenuItem startItem;
    private MenuItem stopItem;
    private MenuItem pauseItem;
    private MenuItem resumeItem;
    private MenuItem exitItem;
    private MenuItem prefItem;

    @Override
    public void init() {
        // Create tiles
        sliderWorkTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(170, 170)
                .description("Work Time")
                .unit(" min.")
                .decimals(0)
                .minValue(0)
                .maxValue(130)
                .value(1)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        workMinutes = (int) Math.round(sliderWorkTimeTile.getValue());

        sliderRestTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(170, 170)
                .description("Rest Time")
                .unit(" min.")
                .decimals(0)
                .minValue(0)
                .maxValue(60)
                .value(0)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        restMinutes = (int) Math.round(sliderRestTimeTile.getValue());

        plusMinusCyclesTile = TileBuilder.create()
                .skinType(Tile.SkinType.PLUS_MINUS)
                .prefSize(170, 170)
                .decimals(0)
                .maxValue(30)
                .minValue(0)
                .description("Cycles")
                .build();

        cycles = (int) plusMinusCyclesTile.getValue();

        Text startText = new Text("Start");
        startText.setFont(Fonts.latoRegular(24));

        startTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_GREEN.color)
                .graphic(startText)
                .build();

        Text pauseText = new Text("Pause");
        pauseText.setFont(Fonts.latoRegular(24));

        pauseTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.ORANGE.color)
                .graphic(pauseText)
                .build();

        Text resumeText = new Text("Resume");
        resumeText.setFont(Fonts.latoRegular(24));

        resumeTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_GREEN.color)
                .graphic(resumeText)
                .build();

        Text stopText = new Text("Stop");
        stopText.setFont(Fonts.latoRegular(24));

        stopTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(340, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_RED.color)
                .graphic(stopText)
                .build();

        // Handle tile events
        sliderWorkTimeTile.setOnTileEvent(e -> {
            TileEvent.EventType type = e.getEventType();
            if (TileEvent.EventType.VALUE_CHANGED == type) {
                workMinutes = (int) Math.round(sliderWorkTimeTile.getValue());
            }
        });

        sliderRestTimeTile.setOnTileEvent(e -> {
            TileEvent.EventType type = e.getEventType();
            if (TileEvent.EventType.VALUE_CHANGED == type) {
                restMinutes = (int) Math.round(sliderRestTimeTile.getValue());
            }
        });

        plusMinusCyclesTile.setOnTileEvent(e -> {
            TileEvent.EventType type = e.getEventType();
            if (TileEvent.EventType.VALUE == type) {
                cycles = (int) plusMinusCyclesTile.getValue();
            }
        });

        startTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> actionOnStart());
        pauseTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> actionOnPause());
        resumeTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> actionOnResume());
        stopTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> actionOnStop());

        // Tray icon items
        pauseItem = new MenuItem("Pause");
        pauseItem.addActionListener(event -> {
            System.out.println("Pause from menu bar");
            Platform.runLater(this::actionOnPause);
        });

        resumeItem = new MenuItem("Resume");
        resumeItem.addActionListener(event -> {
            System.out.println("Resume from menu bar");
            Platform.runLater(this::actionOnResume);
        });

        stopItem = new MenuItem("Stop");
        stopItem.addActionListener(event -> {
            System.out.println("Stop from menu bar");
            Platform.runLater(this::actionOnStop);
        });

        prefItem = new MenuItem("Preferences");
        prefItem.addActionListener(event -> Platform.runLater(() -> {
            System.out.println("Preferences from menu bar");
            popupMenu.remove(prefItem);
            showPrimaryStage();
        }));

        exitItem = new MenuItem("Quit");
        exitItem.addActionListener(event -> {
            System.out.println("Quit from menu bar");
            Platform.exit();
            SystemTray.getSystemTray().remove(trayIcon);
        });

        startItem = new MenuItem("Start");
        startItem.addActionListener(event -> Platform.runLater(() -> {
            System.out.println("Start from menu bar");
            actionOnStart();
        }));
    }

    private boolean actionOnStart() {
        if (((workMinutes == null || workMinutes == 0)
                && (restMinutes == null || restMinutes == 0)) || cycles == null || cycles == 0) return false;
        if (!isRunning) {
            disableTiles(true);
            isRunning = true;

            pane.getChildren().remove(startTile);
            pane.add(pauseTile, 0, 1);
            pane.add(stopTile, 1, 1);
            pane.setColumnSpan(stopTile, 2);

            FxTimer.getInstance().setTimer(getTotalTime(), getCycles());
            FxTimer.getInstance().setOnFinished(event2 -> {
                if (isRunning) {
                    System.out.println("The timer finished");
                    disableTiles(false);
                    isRunning = false;
                    pane.getChildren().remove(3, 5);
                    pane.add(startTile, 0, 1);
                    pane.setColumnSpan(startTile, 3);
                    SwingUtilities.invokeLater(this::setPopupMenuOnStop);
                }
            });
            FxTimer.getInstance().startTimer();
            SwingUtilities.invokeLater(this::setPopupMenuOnStart);
            NotificationFactory.showNotification("Timer", "Here", 1000);
        }
        return true;
    }

    private void actionOnStop() {
        disableTiles(false);
        isRunning = false;
        FxTimer.getInstance().stopTimer();
        pane.getChildren().remove(3, 5);
        pane.add(startTile, 0, 1);
        pane.setColumnSpan(startTile, 3);
        SwingUtilities.invokeLater(this::setPopupMenuOnStop);
    }

    private void actionOnPause() {
        if (isRunning) {
            FxTimer.getInstance().pauseTimer();
            pane.getChildren().remove(3, 5);
            pane.add(resumeTile, 0, 1);
            pane.add(stopTile, 1, 1);
            SwingUtilities.invokeLater(this::setPopupMenuOnPause);
        }
    }

    private void actionOnResume() {
        if (isRunning) {
            FxTimer.getInstance().resumeTimer();
            pane.getChildren().remove(3, 5);
            pane.add(pauseTile, 0, 1);
            pane.add(stopTile, 1, 1);
            SwingUtilities.invokeLater(this::setPopupMenuOnResume);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        SwingUtilities.invokeLater(this::addTrayIcon);
        primaryStage.setScene(new Scene(paneSetUp()));
        primaryStage.setTitle("Timer");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(this::onClose);
        primaryStage.show();
        stage = primaryStage;
    }

    private void addTrayIcon() {
        trayIcon = new TrayIcon(image);
        popupMenu = new PopupMenu();
        popupMenu.add(startItem);
        popupMenu.add(prefItem);
        popupMenu.add(exitItem);
        trayIcon.setPopupMenu(popupMenu);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void setPopupMenuOnStop() {
        popupMenu.remove(1);
        popupMenu.remove(0);
        popupMenu.insert(startItem, 0);
    }

    private void setPopupMenuOnStart() {
        popupMenu.remove(0);
        popupMenu.insert(pauseItem, 0);
        popupMenu.insert(stopItem, 1);
    }

    private void setPopupMenuOnPause() {
        popupMenu.remove(0);
        popupMenu.insert(resumeItem, 0);
    }

    private void setPopupMenuOnResume() {
        popupMenu.remove(0);
        popupMenu.insert(pauseItem, 0);
    }

    private Pane paneSetUp() {
        pane = new FlowGridPane(3, 2,
                sliderWorkTimeTile, sliderRestTimeTile, plusMinusCyclesTile, startTile);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setColumnSpan(startTile, 3);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setCenterShape(true);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(5));
        return pane;
    }

    private void onClose(WindowEvent windowEvent) {
        int count = popupMenu.getItemCount();
        if (stage.isShowing() && count > 1 && popupMenu.getItem(count - 2) != prefItem) {
            popupMenu.insert(prefItem, count - 1);
        }
    }

    private void showPrimaryStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    private void disableTiles(boolean value) {
        sliderWorkTimeTile.setDisable(value);
        sliderRestTimeTile.setDisable(value);
        plusMinusCyclesTile.setDisable(value);
    }

    private int getTotalTime() {
        int work = workMinutes == null ? (int) Math.round(sliderWorkTimeTile.getValue()) : workMinutes;
        int rest = restMinutes == null ? (int) Math.round(sliderRestTimeTile.getValue()) : restMinutes;
        return work + rest;
    }

    private int getCycles() {
        return cycles == null ? (int) plusMinusCyclesTile.getValue() : cycles;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.awt.AppContext;

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
    private Tile countdownTile;
    private Tile resumeTile;

    // Parameters
    private static final int SIZE_TILE_WIDTH = 170;
    private static final int SIZE_TILE_HEIGHT = 170;
    private static final int SIZE_TILE_START_WIDTH = 510;

    // Timer
    private boolean isRunning = false;
    private Integer workMinutes;
    private Integer restMinutes;
    private Integer cycles;

    // Icon
    private final Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon2.png"));
    private PopupMenu popupStop;
    private PopupMenu popupStart;

    @Override
    public void init() {
        // Create tiles
        sliderWorkTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
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
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
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
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
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
                .prefSize(SIZE_TILE_START_WIDTH, SIZE_TILE_HEIGHT)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_GREEN.color)
                .graphic(startText)
                .build();

        countdownTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
                .roundedCorners(true)
                .build();

        Text pauseText = new Text("Pause");
        pauseText.setFont(Fonts.latoRegular(24));

        pauseTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.ORANGE.color)
                .graphic(pauseText)
                .build();

        Text resumeText = new Text("Resume");
        resumeText.setFont(Fonts.latoRegular(24));

        resumeTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.GREEN.color)
                .graphic(resumeText)
                .build();

        Text stopText = new Text("Stop");
        stopText.setFont(Fonts.latoRegular(24));

        stopTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(340, SIZE_TILE_HEIGHT)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_RED.color)
                .graphic(stopText)
                .build();

        // Handle events
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

        startTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            actionOnStart();
        });

        pauseTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isRunning) {
                FxTimer.getInstance().pauseTimer();
                pane.getChildren().remove(3, 5);
                pane.add(resumeTile, 0, 1);
                pane.add(stopTile, 1, 1);
            }
        });

        resumeTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isRunning) {
                FxTimer.getInstance().resumeTimer();
                pane.getChildren().remove(3, 5);
                pane.add(pauseTile, 0, 1);
                pane.add(stopTile, 1, 1);
            }
        });

        stopTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            actionOnStop();
        });
    }

    private void actionOnStop() {
        if (isRunning) {
            disableTiles(false);
            isRunning = false;
            FxTimer.getInstance().stopTimer();
            pane.getChildren().remove(3, 5);
            pane.add(startTile, 0, 1);
            pane.setColumnSpan(startTile, 3);
            javax.swing.SwingUtilities.invokeLater(() -> addTrayIcon(Actions.STOP));
        }
    }

    private boolean actionOnStart() {
        if (((workMinutes == null || workMinutes == 0) && (restMinutes == null || restMinutes == 0)) || cycles == null || cycles == 0) return false;
        if (!isRunning) {
            disableTiles(true);
            isRunning = true;

            pane.getChildren().remove(startTile);
            pane.add(pauseTile, 0, 1);
            pane.add(stopTile, 1, 1);
            pane.setColumnSpan(stopTile, 2);

            FxTimer.getInstance().setTimer(getTotalTime(), getCycles(), isRunning);
            FxTimer.getInstance().setOnFinished(event2 -> {
                System.out.println("The timer finished");
                disableTiles(false);
                isRunning = false;
                pane.getChildren().remove(3, 5);
                pane.add(startTile, 0, 1);
                pane.setColumnSpan(startTile, 3);
                javax.swing.SwingUtilities.invokeLater(() -> addTrayIcon(Actions.STOP));
            });
            FxTimer.getInstance().startTimer();
            javax.swing.SwingUtilities.invokeLater(() -> addTrayIcon(Actions.START));
        }
        return true;
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(() -> addTrayIcon(Actions.STOP));

        pane = new FlowGridPane(3, 2,
                sliderWorkTimeTile, sliderRestTimeTile, plusMinusCyclesTile, startTile);

        pane.setColumnSpan(startTile, 3);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setCenterShape(true);
        pane.setPadding(new javafx.geometry.Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"),
                CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Timer");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(this::onClose);
        primaryStage.show();
        stage = primaryStage;
    }

    private void addTrayIcon(Actions action) {
        SystemTray tray = SystemTray.getSystemTray();

//        if (action == Actions.START) {
//            popupStop.remove(0);
//            return;
//        }

        trayIcon = new TrayIcon(image);

        // Menu items
        MenuItem pauseItem = new MenuItem("Pause");
        pauseItem.addActionListener(event -> {
            System.out.println("Pause from menu bar");
        });

        MenuItem stopItem = new MenuItem("Stop");
        stopItem.addActionListener(event -> Platform.runLater(() -> {
            System.out.println("Start from menu bar");
            actionOnStop();
        }));

        MenuItem prefItem = new MenuItem("Preferences");
        prefItem.addActionListener(event -> Platform.runLater(() -> {
            System.out.println("Preferences from menu bar");
            showPrimaryStage();
        }));

        MenuItem exitItem = new MenuItem("Quit");
        exitItem.addActionListener(event -> {
            System.out.println("Quit from menu bar");
            Platform.exit();
            tray.remove(trayIcon);
        });

        MenuItem startItem = new MenuItem("Start");
        startItem.addActionListener(event -> Platform.runLater(() -> {
            System.out.println("Start from menu bar");
            actionOnStart();
        }));

        try {
            switch (action) {
                case START:
                    popupStop = new PopupMenu();
                    popupStop.add(pauseItem);
                    popupStop.add(stopItem);
                    popupStop.add(prefItem);
                    popupStop.addSeparator();
                    popupStop.add(exitItem);
                    tray.remove(trayIcon);
                    trayIcon.setPopupMenu(popupStop);
                    tray.add(trayIcon);
                case STOP:
                    popupStart = new PopupMenu();
                    popupStart.add(startItem);
                    popupStart.add(prefItem);
                    popupStart.addSeparator();
                    popupStart.add(exitItem);
                    tray.remove(trayIcon);
                    trayIcon.setPopupMenu(popupStart);
                    tray.add(trayIcon);
            }
        } catch (AWTException e) {
//            e.printStackTrace();
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

    private void onClose(WindowEvent event) {
        System.out.println("Closing");
    }

    private int getTotalTime() {
        int work = workMinutes == null ? (int) Math.round(sliderWorkTimeTile.getValue()) : workMinutes;
        int rest = restMinutes == null ? (int) Math.round(sliderRestTimeTile.getValue()) : restMinutes;
        return work + rest;
    }

    private int getCycles() {
        return cycles == null ? (int) plusMinusCyclesTile.getValue() : cycles;
    }

    private enum Actions {
        START, STOP, PAUSE, RESUME
    }

    public static void main(String[] args) {
        launch(args);
    }
}


import java.awt.*;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.events.TileEvent;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    // Nodes
    private static Stage stage;
    private static TrayIcon trayIcon = null;
    private Tile sliderWorkTimeTile;
    private Tile sliderRestTimeTile;
    private Tile plusMinusCyclesTile;
    private Tile startTile;
    private Tile pauseTile;
    private Tile stopTile;
    private Tile countdownTile;

    // Parameters
    private static final int SIZE_TILE_WIDTH = 170;
    private static final int SIZE_TILE_HEIGHT = 170;
    private static final int SIZE_TILE_START_WIDTH = 510;

    // Timer
    private boolean isRunning = false;
    private int workMinutes;
    private int restMinutes;
    private int cycles;

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
                .value(45)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        sliderRestTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
                .description("Rest Time")
                .unit(" min.")
                .decimals(0)
                .minValue(0)
                .maxValue(60)
                .value(10)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        plusMinusCyclesTile = TileBuilder.create()
                .skinType(Tile.SkinType.PLUS_MINUS)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
                .decimals(0)
                .maxValue(30)
                .minValue(0)
                .description("Cycles")
                .build();

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

        Text stopText = new Text("Stop");
        stopText.setFont(Fonts.latoRegular(24));

        stopTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(SIZE_TILE_WIDTH, SIZE_TILE_HEIGHT)
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
            if (!isRunning) {
                disableTiles(true);
                isRunning = true;

                pane.getChildren().remove(startTile);
                pane.add(countdownTile, 0, 1);
                pane.add(pauseTile, 1, 1);
                pane.add(stopTile, 2, 1);

//                FxTimer.getInstance().setTimer(workMinutes + restMinutes, cycles, isRunning);
//                FxTimer.getInstance().startTimer();
            }
        });

        stopTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isRunning) {
                disableTiles(false);
                isRunning = false;

                pane.getChildren().remove(3, 6);
                pane.add(startTile, 0, 1);
                pane.setColumnSpan(startTile, 3);

//                FxTimer.getInstance().stopTimer();
            }
        });
    }
    FlowGridPane pane;
    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addTrayIcon);

        pane = new FlowGridPane(3, 2,
                sliderWorkTimeTile, sliderRestTimeTile, plusMinusCyclesTile, startTile);

        pane.setColumnSpan(startTile, 3);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setCenterShape(true);
        pane.setPadding(new javafx.geometry.Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Timer");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(this::onClose);
        primaryStage.show();
        stage = primaryStage;
    }

    private void addTrayIcon() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            final Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/icon2.png"));
            trayIcon = new TrayIcon(image);

            final PopupMenu popupStart = new PopupMenu();
            final PopupMenu popupStop = new PopupMenu();

            MenuItem pauseItem = new MenuItem("Pause");
            pauseItem.addActionListener(event -> {
                System.out.println("Pause");
            });

            MenuItem stopItem = new MenuItem("Stop");
            stopItem.addActionListener(event -> {
                System.out.println("Stop");
                FxTimer.getInstance().stopTimer();
            });

            MenuItem prefItem = new MenuItem("Preferences");
            prefItem.addActionListener(event -> {
                System.out.println("Preferences");
                Platform.runLater(this::showPrimaryStage);
            });

            MenuItem exitItem = new MenuItem("Quit");
            exitItem.addActionListener(event -> {
                System.out.println("Quit");
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

    private void disableTiles(boolean value) {
        sliderWorkTimeTile.setDisable(value);
        sliderRestTimeTile.setDisable(value);
        plusMinusCyclesTile.setDisable(value);
    }

    private void onClose(WindowEvent event) {
        System.out.println("closing");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

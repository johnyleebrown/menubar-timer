package me.grigorii.menubartimer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static me.grigorii.menubartimer.Helper.printDate;

import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.events.TileEvent;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
 * This file is part of Menu bar Timer.
 *
 * Menu bar Timer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Menu bar Timer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Menu bar Timer. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Created by github.com/johnyleebrown
 *
 */
public class Main extends Application {

    final static Logger logger = Logger.getLogger(Main.class);

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
    private PopupMenu popupMenu;
    private MenuItem startItem;
    private MenuItem stopItem;
    private MenuItem pauseItem;
    private MenuItem resumeItem;
    private MenuItem exitItem;
    private MenuItem prefItem;

    // Strings
    private final String PATH_LIGHT_MODE_ICON = "icons/i16x16@3x.png";
    private final String PATH_DARK_MODE_ICON = "icons/i16x16@3x_dm.png";

    // Preferences struct
    private int[] storedPref;

    @Override
    public void init() {
        // Retrieving stored preferences
        storedPref = getPref();
        int workTimerFromXML = (storedPref[0] == 0) ? 1 : storedPref[0];
        int restTimerFromXML = (storedPref[1] == 0) ? 1 : storedPref[1];
        int cyclesFromXML = (storedPref[2] == 0) ? 1 : storedPref[2];

        // Create tiles
        //// Work tile
        sliderWorkTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(170, 170)
                .description("Work Time")
                .unit(" min.")
                .decimals(0)
                .minValue(0)
                .maxValue(130)
                .value(workTimerFromXML)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        workMinutes = (int) Math.round(sliderWorkTimeTile.getValue());

        //// Rest tile
        sliderRestTimeTile = TileBuilder.create()
                .skinType(Tile.SkinType.SLIDER)
                .prefSize(170, 170)
                .description("Rest Time")
                .unit(" min.")
                .decimals(0)
                .minValue(0)
                .maxValue(60)
                .value(restTimerFromXML)
                .barBackgroundColor(Tile.FOREGROUND)
                .build();

        restMinutes = (int) Math.round(sliderRestTimeTile.getValue());

        //// Cycles tile
        plusMinusCyclesTile = TileBuilder.create()
                .skinType(Tile.SkinType.PLUS_MINUS)
                .prefSize(170, 170)
                .decimals(0)
                .minValue(0)
                .maxValue(100)
                .value(cyclesFromXML)
                .description("Cycles")
                .build();

        cycles = (int) plusMinusCyclesTile.getValue();

        //// Start button tile
        Text startText = new Text("Start");
        startText.setFont(Fonts.latoRegular(24));

        startTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_GREEN.color)
                .graphic(startText)
                .build();

        //// Pause button tile
        Text pauseText = new Text("Pause");
        pauseText.setFont(Fonts.latoRegular(24));

        pauseTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.ORANGE.color)
                .graphic(pauseText)
                .build();

        //// Resume button tile
        Text resumeText = new Text("Resume");
        resumeText.setFont(Fonts.latoRegular(24));

        resumeTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(170, 170)
                .roundedCorners(true)
                .backgroundColor(Tile.TileColor.LIGHT_GREEN.color)
                .graphic(resumeText)
                .build();

        //// Stop button tile
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
            Platform.runLater(this::actionOnPause);
        });

        resumeItem = new MenuItem("Resume");
        resumeItem.addActionListener(event -> {
            Platform.runLater(this::actionOnResume);
        });

        stopItem = new MenuItem("Stop");
        stopItem.addActionListener(event -> {
            Platform.runLater(this::actionOnStop);
        });

        prefItem = new MenuItem("Preferences");
        prefItem.addActionListener(event -> Platform.runLater(() -> {
            popupMenu.remove(prefItem);
            showPrimaryStage();
        }));

        exitItem = new MenuItem("Quit");
        exitItem.addActionListener(event -> {
            Platform.exit();
            SystemTray.getSystemTray().remove(trayIcon);
        });

        startItem = new MenuItem("Start");
        startItem.addActionListener(event -> Platform.runLater(() -> {
            actionOnStart();
        }));
    }

    /**
     * Getting preferences obj from xml
     */
    private int[] getPref() {
        FileInputStream in = null;
        int[] pref = new int[3];
        try {
            in = new FileInputStream(".TimerSettings.xml");
            XMLDecoder decoder = new XMLDecoder(in);
            pref = (int[]) decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            return pref;
        }
    }

    private boolean actionOnStart() {
        if (((workMinutes == null || workMinutes == 0) && (restMinutes == null || restMinutes == 0))
                || cycles == null || cycles == 0) return false;
        if (!isRunning) {
            isRunning = true;
            onStartTiles();
            FxTimer.getInstance().setTimer(getWorkTime(), getRestTime(), getCycles());
            FxTimer.getInstance().setOnFinished(this::onFinishedTimer);
            FxTimer.getInstance().startTimer();
            SwingUtilities.invokeLater(this::setPopupMenuOnStart);
        }
        return true;
    }

    private void onStartTiles() {
        disableTiles(true);
        pane.getChildren().remove(startTile);
        pane.add(pauseTile, 0, 1);
        pane.add(stopTile, 1, 1);
        pane.setColumnSpan(stopTile, 2);
    }

    private void onFinishedTimer(ActionEvent actionEvent) {
        if (isRunning) {
            disableTiles(false);
            isRunning = false;
            pane.getChildren().remove(3, 5);
            pane.add(startTile, 0, 1);
            pane.setColumnSpan(startTile, 3);
            SwingUtilities.invokeLater(this::setPopupMenuOnStop);
            sendNotification(workMinutes * cycles + restMinutes * (cycles - 1));
            logger.debug(printDate());
        }
    }

    private void sendNotification(int total) {
        String subtitle_base = "Elapsed time: " + total + " minute";
        String MSG_TITLE_FINISHED = total == 1 ? subtitle_base : subtitle_base + "s";
        String MSG_SUBTITLE_FINISHED = "Timer is up! " + MSG_TITLE_FINISHED;
        String MSG_MESSAGE_FINISHED = "Started at " + getTimeMinus(total);
        NotificationFactory.showNotification(MSG_SUBTITLE_FINISHED);
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
//        System.setProperty("apple.awt.UIElement", "true");
        SwingUtilities.invokeLater(this::addTrayIcon);
        stage = primaryStage;
        setStage();
        Platform.setImplicitExit(false);
    }

    private void setStage() {
        stage.setScene(new Scene(paneSetUp()));
        stage.setTitle("Timer");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::onClose);
        stage.show();
    }

    private void addTrayIcon() {
        java.awt.Toolkit.getDefaultToolkit();
        Image img = getTrayIconImage();
        if (img == null) {
            logger.error("Icon doesn't exist");
            Platform.exit();
        }
        trayIcon = new TrayIcon(img);
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

    private Image getTrayIconImage() {
        String path = isMacMenuBarDarkMode() ? PATH_DARK_MODE_ICON : PATH_LIGHT_MODE_ICON;
        if (Main.class.getResource("/" + path) == null) {
            URL url = getClass().getResource("");
            if (url != null && url.toString().startsWith("jar:")) {
                String s = url.toExternalForm() + path;
                return Toolkit.getDefaultToolkit().getImage(s);
            }
        } else {
            return Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/" + path));
        }
        return null;
    }

    private static boolean isMacMenuBarDarkMode() {
        try {
            final Process proc = Runtime.getRuntime().exec(new String[] {"defaults", "read", "-g", "AppleInterfaceStyle"});
            proc.waitFor(100, MILLISECONDS);
            return proc.exitValue() == 0;
        } catch (IOException | InterruptedException | IllegalThreadStateException ex) {
            logger.error("Could not determine, whether 'dark mode' is being used. Falling back to default (light) mode.");
            return false;
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
        pane = new FlowGridPane(3, 2, sliderWorkTimeTile, sliderRestTimeTile, plusMinusCyclesTile, startTile);
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
            stage.requestFocus();
        }
    }

    private void disableTiles(boolean value) {
        sliderWorkTimeTile.setDisable(value);
        sliderRestTimeTile.setDisable(value);
        plusMinusCyclesTile.setDisable(value);
    }

    private int getWorkTime() {
        return workMinutes == null ? (int) Math.round(sliderWorkTimeTile.getValue()) : workMinutes;
    }

    private int getRestTime() {
        return restMinutes == null ? (int) Math.round(sliderRestTimeTile.getValue()) : restMinutes;
    }

    private int getCycles() {
        return cycles == null ? (int) plusMinusCyclesTile.getValue() : cycles;
    }

    @Override
    public void stop() {
        savePref();
    }

    /**
     * Saving preferences obj to xml
     */
    private void savePref() {
        int work = (int) Math.round(sliderWorkTimeTile.getValue());
        int rest = (int) Math.round(sliderRestTimeTile.getValue());
        int cycles = (int) plusMinusCyclesTile.getValue();
        int[] pref = new int[]{work, rest, cycles};
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(".TimerSettings.xml");
            XMLEncoder encoder = new XMLEncoder(out);
            encoder.writeObject(pref);
            encoder.close();
        } catch (Exception e) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static String getTimeMinus(int minutes) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(minutes);
        String formatDateTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        return formatDateTime;
    }

    /**
     * The application will remain running
     * until the user selects the Exit menu option from the tray icon.
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch(args);
    }
}

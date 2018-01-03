package app;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller implements Initializable {

    private final Timer currentTimer = new Timer();

    private final ObservableList<Timer> timers = FXCollections.observableArrayList();

    @FXML
    private Button workMinus;

    @FXML
    private Label workHundreds;

    @FXML
    private Label workTens;

    @FXML
    private Button workPlus;

    @FXML
    private Label workOnes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workTens.setText("4");
        workOnes.setText("5");
    }

    @FXML
    public void workPlusClicked(ActionEvent actionEvent) {

    }

    private void setCurrentTimer(Timer timer) {
        if (timer != null) {
            currentTimer.workMinutes = timer.workMinutes;
            currentTimer.restMinutes = timer.restMinutes;
            currentTimer.cycles = timer.cycles;
        }
    }
}
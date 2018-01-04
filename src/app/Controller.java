package app;

import static app.Helper.calculateNextTime;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import app.Helper;

public class Controller implements Initializable {

    private Timer currentTimer;

    private static SimpleIntegerProperty overallWork;

    private final ObservableIntegerArray workTime = FXCollections.observableIntegerArray();

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

        overallWork = new SimpleIntegerProperty(Integer.valueOf(workHundreds.getText() + workTens.getText() + workOnes.getText()));

        this.currentTimer = new Timer(overallWork, new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
    }

    @FXML
    public void workPlusClicked(ActionEvent actionEvent) {
        int[] time = calculateNextTime(workHundreds.getText(), workTens.getText(), workOnes.getText(), true);
        if (time == null) return;
        if (workTime.size() == 0) {
            workTime.addAll(time, 0, 3);
        } else {
            workTime.setAll(time, 0, 3);
        }

        setNextTime();
    }

    @FXML
    public void workMinusClicked(ActionEvent actionEvent) {
        int[] time = calculateNextTime(workHundreds.getText(), workTens.getText(), workOnes.getText(), false);
        if (time == null) return;
        if (workTime.size() == 0) {
            workTime.addAll(time, 0, 3);
        } else {
            workTime.setAll(time, 0, 3);
        }

        setNextTime();
    }

    private void setNextTime() {
        workHundreds.setText(String.valueOf(workTime.get(0)));
        workTens.setText(String.valueOf(workTime.get(1)));
        workOnes.setText(String.valueOf(workTime.get(2)));
        overallWork = new SimpleIntegerProperty(Integer.valueOf(workHundreds.getText() + workTens.getText() + workOnes.getText()));
    }


    private void setCurrentTimer(Timer timer) {
        if (timer != null) {
            currentTimer.workMinutes = timer.workMinutes;
            currentTimer.restMinutes = timer.restMinutes;
            currentTimer.cycles = timer.cycles;
        }
    }


//        System.out.println(workHundreds.getText() + " " + workTens.getText() + " " + workOnes.getText());
//        System.out.println(workTime.get(0) + " " + workTime.get(1) + " " + workTime.get(2));
}

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PaneController implements Initializable {

    private Timers currentTimer;

    private static SimpleIntegerProperty overallWork;

    private final ObservableIntegerArray workTime = FXCollections.observableIntegerArray();

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

    @FXML
    private Button startBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("here");
        workTens.setText("4");
        workOnes.setText("5");

        overallWork = new SimpleIntegerProperty(Integer.valueOf(workHundreds.getText() + workTens.getText() + workOnes.getText()));

        this.currentTimer = new Timers(overallWork, new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
    }

    @FXML
    public void workPlusClicked(ActionEvent actionEvent) {
        int[] time = Helper.calculateNextTime(workHundreds.getText(), workTens.getText(), workOnes.getText(), true);
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
        int[] time = Helper.calculateNextTime(workHundreds.getText(), workTens.getText(), workOnes.getText(), false);
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

    public void setCurrentTimer() {
        if (currentTimer != null) {
            currentTimer.workMinutes = overallWork;
            currentTimer.restMinutes = new SimpleIntegerProperty(0);
            currentTimer.cycles = new SimpleIntegerProperty(1);
        } else {
            this.currentTimer = new Timers(overallWork, new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        }
    }

    @FXML
    public void startBtnClicked(ActionEvent actionEvent) {

        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.hide();

        Helper.printDate();

        FxTimer.getInstance().startTimer();
    }

}
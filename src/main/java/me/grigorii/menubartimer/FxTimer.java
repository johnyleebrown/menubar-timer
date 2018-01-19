package me.grigorii.menubartimer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Created by github.com/johnyleebrown
 */
public class FxTimer {

    public Timeline timeline;
    private int minutes;
    private int cycles;

    private FxTimer() {
        System.out.println("FxTimer constructor");
        timeline = new Timeline();
    }

    private static class Helper {
        private static final FxTimer INSTANCE = new FxTimer();
    }

    public static FxTimer getInstance() {
        return Helper.INSTANCE;
    }

    public void setTimer(int workMinutes, int cycles) {
        this.minutes = workMinutes;
        this.cycles = cycles;
    }

    public void startTimer() {
        timeline.getKeyFrames().add(new KeyFrame(Duration.minutes(minutes)));
        timeline.setCycleCount(cycles);
        System.out.println(timeline.getTotalDuration().toMinutes());
        timeline.play();
        System.out.println("The timer started");
        printDate();
    }

    public void pauseTimer() {
        timeline.pause();
        System.out.println(timeline.getCurrentTime());
        System.out.println("The timer paused");
    }

    public void resumeTimer() {
        timeline.play();
        System.out.println("The timer resumed");
    }

    public void stopTimer() {
        System.out.println(timeline.getCurrentTime());
        timeline.stop();
        System.out.println("The timer stopped");
        printDate();
    }

    public static void printDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String strDate = simpleDateFormat.format(calendar.getTime());
        System.out.println(strDate);
    }

    public void setOnFinished(EventHandler<ActionEvent> eventHandler) {
        timeline.setOnFinished(eventHandler);
    }
}

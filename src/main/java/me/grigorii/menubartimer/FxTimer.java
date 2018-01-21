package me.grigorii.menubartimer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
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
public class FxTimer {

    private Timeline workTimeline;
    private Timeline lastTimeline;
    private Timeline restTimeline;
    private int cycles;

    private FxTimer() {
        workTimeline = new Timeline();
        restTimeline = new Timeline();
        lastTimeline = new Timeline();
    }

    private static class Helper {
        private static final FxTimer INSTANCE = new FxTimer();
    }

    public static FxTimer getInstance() {
        return Helper.INSTANCE;
    }

    public void setTimer(int workTime, int restTime, int cycles) {
        this.cycles = cycles;

        workTimeline.setCycleCount(cycles - 1);
        workTimeline.getKeyFrames().add(new KeyFrame(Duration.minutes(workTime), this::onFinishedWorkTimeline));
        workTimeline.setOnFinished(this::onStartLastTimeline);

        restTimeline.getKeyFrames().add(new KeyFrame(Duration.minutes(restTime)));
        restTimeline.setOnFinished(this::onFinishedRestTimeline);

        lastTimeline.getKeyFrames().add(new KeyFrame(Duration.minutes(workTime)));
        lastTimeline.setOnFinished(event -> System.out.println("work finished completely"));
    }

    private void onFinishedWorkTimeline(ActionEvent actionEvent) {
        workTimeline.pause();
        System.out.println(workTimeline.getCurrentTime());
        System.out.println("work is done");
        NotificationFactory.showNotification("Timer", "Time to take a break!", "", 1000);
        onStartRestTimeline();
    }

    private void onFinishedRestTimeline(ActionEvent actionEvent) {
        System.out.println("rest is done");
        workTimeline.play();
        NotificationFactory.showNotification("Timer", "It's time to work!", "", 1000);
        System.out.println("continuing");
    }

    private void onStartLastTimeline(ActionEvent actionEvent) {
        lastTimeline.play();
        System.out.println("last round has started");
    }

    private void onStartRestTimeline() {
        restTimeline.play();
        System.out.println("rest started");
    }

    public void startTimer() {
        if (cycles == 1) {
            lastTimeline.play();
            System.out.println(lastTimeline.getTotalDuration().toMinutes());
        }
        else {
            workTimeline.play();
            System.out.println(workTimeline.getTotalDuration().toMinutes());
        }
        System.out.println("The timer started");
    }

    public void pauseTimer() {
        Timeline t = getRunningTimeline();
        if (t == null) return;
        t.pause();
        System.out.println("The timer paused");
        System.out.println(t.getCurrentTime().toMinutes());
    }

    private Timeline getRunningTimeline() {
        if (workTimeline.getStatus() == Animation.Status.RUNNING) return workTimeline;
        else if (restTimeline.getStatus() == Animation.Status.RUNNING) return restTimeline;
        else if (lastTimeline.getStatus() == Animation.Status.RUNNING) return lastTimeline;
        return null;
    }

    public void resumeTimer() {
        Timeline t = getPausedTimeline();
        t.play();
        System.out.println("The timer resumed");
    }

    private Timeline getPausedTimeline() {
        if (restTimeline.getStatus() == Animation.Status.PAUSED) return restTimeline;
        else if (workTimeline.getStatus() == Animation.Status.PAUSED) return workTimeline;
        else return lastTimeline;
    }

    public void stopTimer() {
        workTimeline.stop();
        System.out.println("The timer stopped");
        System.out.println(workTimeline.getCurrentTime().toMinutes());
    }

    public void setOnFinished(EventHandler<ActionEvent> eventHandler) {
        lastTimeline.setOnFinished(eventHandler);
    }
}

package me.grigorii.menubartimer;

import org.apache.log4j.Logger;

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

    final static Logger logger = Logger.getLogger(FxTimer.class);
    private Timeline workTimeline;
    private Timeline lastTimeline;
    private Timeline currentTimeline;
    private int cycles;

    private FxTimer() {
    }

    private static class Helper {
        private static final FxTimer INSTANCE = new FxTimer();
    }

    public static FxTimer getInstance() {
        return Helper.INSTANCE;
    }

    public void setTimer(int workTime, int restTime, int cycles) {
        this.cycles = cycles;
        workTimeline = new Timeline(
            new KeyFrame(Duration.minutes(workTime), e -> {
                NotificationFactory.showNotification("Time to take a break!");
                logger.debug("Work is done");
            }),
            new KeyFrame(Duration.minutes(workTime + restTime), e -> {
                NotificationFactory.showNotification("It's time to work!");
                logger.debug("Break is finished");
            }));
        workTimeline.setCycleCount(cycles - 1);
        workTimeline.setOnFinished(e -> startTimer(lastTimeline));
        lastTimeline = new Timeline(new KeyFrame(Duration.minutes(workTime)));
    }

    public void startTimer() {
        if (cycles == 1) startTimer(lastTimeline);
        else startTimer(workTimeline);
        logger.debug("The timer started");
    }

    public void pauseTimer() {
        currentTimeline.pause();
        logger.debug("The timer paused " + currentTimeline.getCurrentTime().toMinutes());
    }

    public void resumeTimer() {
        currentTimeline.play();
        logger.debug("The timer resumed");
    }

    public void stopTimer() {
        currentTimeline.stop();
        logger.debug("The timer stopped " + workTimeline.getCurrentTime().toMinutes());
    }

    private void startTimer(Timeline t) {
        currentTimeline = t;
        currentTimeline.playFromStart();
    }

    public void setOnFinished(EventHandler<ActionEvent> eventHandler) {
        lastTimeline.setOnFinished(eventHandler);
    }
}

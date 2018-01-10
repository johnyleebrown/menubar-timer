import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 * TODO: builder
 *
 * Created by Greg on 1/4/18.
 */
public class FxTimer {

    private Timeline timeline;
    private int minutes;
    private int cycles;
    private boolean isRunning;

    private FxTimer() {
    }

    private static class Helper {
        private static final FxTimer INSTANCE = new FxTimer();
    }

    public static FxTimer getInstance() {
        return Helper.INSTANCE;
    }

    public void setTimer(int workMinutes, int cycles, boolean isRunning) {
        this.minutes = workMinutes;
        this.cycles = cycles;
        this.isRunning = isRunning;
    }

    public void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(10)));
        timeline.setCycleCount(5);
        timeline.play();
        System.out.println("The timer has started");
    }

    public void pauseTimer() {
        timeline.pause();
    }

    public void resumeTimer() {
        timeline.pause();
    }

    public void stopTimer() {
        timeline.stop();
        System.out.println("The timer stopped");
    }

}

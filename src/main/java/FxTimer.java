import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Created by Greg on 1/4/18.
 */
public class FxTimer {

    private Timeline timeline;

    private FxTimer() {
    }

    private static class Helper {
        private static final FxTimer INSTANCE = new FxTimer();
    }

    public static FxTimer getInstance() {
        return Helper.INSTANCE;
    }

    public void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(10)));
        timeline.setCycleCount(5);
        timeline.play();
        System.out.println("the timer has started");
    }

    public void stopTimer() {
        System.out.println(timeline.getCurrentTime());
        System.out.println(timeline.getStatus());
        timeline.stop();
        System.out.println("the timer stopped");
    }

    public void pauseTimer() {
        timeline.pause();
    }

}

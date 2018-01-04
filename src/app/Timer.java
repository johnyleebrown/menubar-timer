package app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Greg on 1/3/18.
 */
public class Timer {

    IntegerProperty workMinutes = new SimpleIntegerProperty();

    IntegerProperty restMinutes = new SimpleIntegerProperty();

    IntegerProperty cycles = new SimpleIntegerProperty();

    public int getWorkMinutes() {
        return workMinutes.get();
    }

    public IntegerProperty workMinutesProperty() {
        return workMinutes;
    }

    public void setWorkMinutes(int workMinutes) {
        this.workMinutes.set(workMinutes);
    }

    public int getRestMinutes() {
        return restMinutes.get();
    }

    public IntegerProperty restMinutesProperty() {
        return restMinutes;
    }

    public void setRestMinutes(int restMinutes) {
        this.restMinutes.set(restMinutes);
    }

    public int getCycles() {
        return cycles.get();
    }

    public IntegerProperty cyclesProperty() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles.set(cycles);
    }

    public Timer(IntegerProperty workMinutes, IntegerProperty restMinutes, IntegerProperty cycles) {
        this.workMinutes = workMinutes;
        this.restMinutes = restMinutes;
        this.cycles = cycles;
    }
}

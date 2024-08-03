package utils;

public class Timer {
    private double seconds;
    private double timer = 0;
    private boolean done = false;

    public Timer(double seconds) {
        this.seconds = seconds;
    }

    public double getSeconds() {
        return this.seconds;
    }

    public void update() {
        this.timer -= Time.deltaT();
    }

    public void restart() {
        this.timer = seconds;
    }

    public boolean isDone() {
        return this.timer <= 0;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }
}

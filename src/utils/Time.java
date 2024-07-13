package utils;

public class Time {
    public static final long timeStarted = System.nanoTime();
    private static long beginTime, endTime = System.nanoTime();
    private static double deltaTime = 0;

    public static long getTime() {
        return ((System.nanoTime() - timeStarted));
    }

    public static void updateTime() {
        endTime = getTime();
        deltaTime = (endTime - beginTime) * 1e-9;
        beginTime = endTime;
    }

    public static double deltaT() {
        return deltaTime;
    }
}

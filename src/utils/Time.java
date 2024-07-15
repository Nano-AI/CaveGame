/**
 * Time manager class
 */
package utils;

public class Time {
    // start time
    public static final long timeStarted = System.nanoTime();
    // begin and end time
    private static long beginTime, endTime = System.nanoTime();
    // deltaT
    private static double deltaTime = 0;

    public static long getTime() {
        return ((System.nanoTime() - timeStarted));
    }

    public static void updateTime() {
        endTime = getTime();
        deltaTime = (endTime - beginTime) * 1e-9;
        beginTime = endTime;
    }

    /**
     * @return Time since last frame
     */
    public static double deltaT() {
        return deltaTime;
    }
}

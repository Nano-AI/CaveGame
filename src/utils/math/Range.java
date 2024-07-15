/**
 * Range utility class
 */
package utils.math;

public class Range {
    // min & max
    private int min, max;
    // increment tracker
    private int i;
    // if the range has already exceeded the max or min value
    private boolean hasCapped;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @param min Min range to set to
     * @param max Max range to set to
     */
    public void setRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @param val Value to clamp
     * @return Value within range of [min, max] inclusive.
     */
    public int clamp(int val) {
        if (val < min) {
            return min;
        }
        return Math.min(val, max);
    }

    /**
     * Increment value by amount & clamp if it exceedes
     * @param amount Amount to add by
     * @return The interval tracker value after the cap
     */
    public int increment(int amount) {
        // if it's under
        if (i + amount < min) {
            // set to min
            i = min;
            // set capped
            hasCapped = true;
        }
        // if it's over
        else if (i + amount >= max) {
            // set to min
            i = min - 1;
            // set capped
            hasCapped = true;
        } else {
            // add by amount
            i += amount;
        }
        // return i
        return i;
    }

    public int increment() {
        return increment(1);
    }

    public void reset() {
        this.hasCapped = false;
        this.i = min;
    }

    public boolean hasCapped() {
        return this.hasCapped;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public void decrement() {
        this.i--;
    }

    public int getValue() {
        return i;
    }

    public void setValue(int i) {
        this.i = i;
    }
}

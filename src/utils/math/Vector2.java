/**
 * Vector2 utility class
 */
package utils.math;

public class Vector2 {
    // x and y position
    public double x, y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Setter
     * @param x Set x value
     * @param y Set y value
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Setter
     * @param v Vector to set to
     */
    public void set(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    /**
     * Add to the current vector
     * @param v Thing to add to current vector
     * @return Current vector
     */
    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    /**
     * Add to the current vector
     * @param a X value to add by
     * @param b Y value to add by
     * @return Current vector
     */
    public Vector2 add(double a, double b) {
        this.x += a;
        this.y += b;
        return this;
    }

    /**
     * Return new Vector after adding v
     * @param v Vector to add by
     * @return New vector that uses current and passed vector to a new vector
     */
    public Vector2 plus(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    /**
     * Return new Vector after adding V
     * @param a X value to add by
     * @param b Y Value to add by
     * @return New vector that uses current and passed x & y value to the vector
     */
    public Vector2 plus(double a, double b) {
        return new Vector2(this.x + a, this.y + b);
    }

    /**
     * Multiply current vector by value
     * @param v Value to mult by
     * @return Current vector
     */
    public Vector2 multiply(double v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    /**
     * Return new vector that's mult by value
     * @param v Value to mult by
     * @return New vector that uses v and current vector
     */
    public Vector2 times(double v) {
        return new Vector2(this.x * v, this.y * v);
    }

    /**
     * Subtract current vector by v
     * @param v Vector to subtract by
     * @return Current vector
     */
    public Vector2 subtract(Vector2 v) {
        return add(-v.x, -v.y);
    }

    /**
     * Subtract current vector by x & y
     * @param x X to change by
     * @param y Y to change by
     * @return New vector that uses double x and y
     */
    public Vector2 subtract(double x, double y) {
        return add(-x, -y);
    }

    /**
     * @return Unit vector
     */
    public Vector2 unitVector() {
        return (new Vector2(x, y)).div(magnitude());
    }

    /**
     * Divide current vector by float amount
     * @param v float to divide by
     * @return New vector2 divided by v
     */
    public Vector2 div(float v) {
        if (v == 0) return new Vector2(0, 0);
        this.x /= v;
        this.y /= v;
        return this;
    }

    /**
     * @return Magnitude of vector
     */
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vector2 [x=" + x + ", y=" + y + "]";
    }

    /**
     * Distance to another vector
     * @param position Vector to calculate distance to
     * @return Distance as a vector2
     */
    public Vector2 distanceTo(Vector2 position) {
        return new Vector2(position.x - x, position.y - y);
    }

    /**
     * @return If vector is 0
     */
    public boolean isZero() {
        return this.x == 0 && this.y == 0;
    }

    /**
     * Check if it's the same direction
     * @param dir Direction
     * @return If directions are the same
     */
    public boolean inSameDirection(Vector2 dir) {
        if (isZero() && dir.isZero()) return true;
        if ((x < 0 && dir.x < 0) || (x > 0 && dir.x > 0)) {
            return (dir.y < 0 && y < 0) || (dir.y > 0 && y > 0);
        }
        return false;
    }
}
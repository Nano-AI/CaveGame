package utils.math;

public class Vector2 {
    public double x, y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 add(float a, float b) {
        this.x += a;
        this.y += b;
        return this;
    }

    public Vector2 plus(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public Vector2 plus(float a, float b) {
        return new Vector2(this.x + a, this.y + b);
    }

    public Vector2 mul(double v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    public Vector2 subtract(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 unitVector() {
        return (new Vector2(x, y)).div(magnitude());
    }

    public Vector2 div(float v) {
        if (v == 0) return new Vector2(0, 0);
        this.x /= v;
        this.y /= v;
        return this;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vector2 [x=" + x + ", y=" + y + "]";
    }

}
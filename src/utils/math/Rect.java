package utils.math;

import utils.DirectionType;

import static utils.DirectionType.*;

public class Rect {
    public Vector2 position, size;

    public Rect(double x, double y, double w, double h) {
        this(new Vector2(x, y), new Vector2(w, h));
    }

    public Rect(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    public boolean isInside(Vector2 pos) {
        return isInside(pos.x, pos.y);
    }

    public boolean isInside(double x, double y) {
        return x >= position.x && x <= position.x + size.x && y >= position.y && y <= position.y + size.y;
    }

    // relative to current rect, where are we intersecting?
    public boolean isIntersecting(Rect r) {
        return position.x < r.position.x + r.size.x && position.x + size.x > r.position.x
                && position.y < r.position.y + r.size.y && position.y + size.y > r.position.y;
    }

    public static boolean isIntersecting(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        return pos1.x < pos2.x + size2.x && pos1.x + size1.x > pos2.x && pos1.y < pos2.y + size2.y && pos1.y + size1.y > pos2.y;
    }
}

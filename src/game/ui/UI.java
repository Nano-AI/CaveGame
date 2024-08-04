package game.ui;

import game.entities.Camera;
import utils.math.Vector2;

public abstract class UI {
    protected Camera camera;
    protected Vector2 position;

    public UI(Camera cam) {
        this.camera = cam;
    }
    public abstract void render();
    public abstract void init();
    public abstract void update();
    public void move(double dx, double dy) {
        this.position.x += dx;
        this.position.y += dy;
    }
}

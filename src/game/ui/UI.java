package display;

import game.entities.Camera;

public abstract class UI {
    protected Camera camera;
    public UI(Camera cam) {
        this.camera = cam;
    }
    public abstract void render();
    public abstract void init();
    public abstract void update();
}

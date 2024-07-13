package game.scenes;

import game.entities.Camera;
import game.entities.Entity;

import java.util.LinkedList;
import java.util.List;

public abstract class Scene {
    protected List<Entity> entities;
    protected Camera camera;

    public Scene() {
        this.entities = new LinkedList<Entity>();
    }

    public abstract void init();
    public abstract void update();
    public abstract void render();

    public List<Entity> getEntities() {
        return this.entities;
    }
}

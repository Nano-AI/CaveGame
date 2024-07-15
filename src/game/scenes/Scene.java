/**
 * Scene abstract class template
 */
package game.scenes;

import game.entities.Camera;
import game.entities.Entity;
import game.entities.Player;

import java.util.LinkedList;
import java.util.List;

public abstract class Scene {
    // entities in the scene
    protected List<Entity> entities;
    // camera in the scene
    protected Camera camera;
    // player in the scene
    protected Player player;

    public Scene() {
        this.entities = new LinkedList<Entity>();
    }

    // init, update, render functions
    public abstract void init();
    public abstract void update();
    public abstract void render();

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

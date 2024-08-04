/**
 * Camera class
 */

package game.entities;

import display.Window;
import input.KeyInput;
import utils.math.Vector2;
import utils.tile.TileSheet;

import java.awt.event.KeyEvent;

public class Camera extends Entity {
    // focus on
    private Entity focus;
    // camera size
    private Vector2 cameraSize;
    // world size
    private Vector2 worldSize;

    public Camera(Vector2 position, Vector2 cameraSize, Vector2 worldSize) {
        super(position);
        this.cameraSize = cameraSize;
        this.worldSize = worldSize;
    }

    /**
     * Move camera dx and dy amount on x and y axis
     * @param dx Change x by dx
     * @param dy Change y by dy
     */
    public void move(int dx, int dy) {
        this.position.add(dx, dy);
    }

    @Override
    public void update() {
        // get player's center position
        if (focus == null) return;
        Vector2 focusCenter;
        if (focus.image != null) {
            focusCenter = focus.getCenter();
        } else {
            focusCenter = focus.getPosition();
        }
        // calculate dx and dy
        //              player in the center location           center pos
        int dx = (int) ((focusCenter.x - cameraSize.x / 2) - position.x);
        //              player in the center                    center pos
        int dy = (int) ((focusCenter.y - cameraSize.y / 2) - position.y);

        // clamp the dimensions to ensure it doesn't go out of bounds
        if (position.x + dx <= 0) {
            dx = (int) (-position.x);
        } else if (position.x + dx > worldSize.x) {
            dx = (int) (worldSize.x - position.x);
        }

        // clamp the y axis
        if (position.y + dy <= 0) {
            dy = (int) (-position.y);
        } else if (position.y + dy > worldSize.y) {
            dy = (int) (worldSize.y - position.y);
        }

        // add the position
        position.x += dx;
        position.y += dy;
        // translate the buffer by dx and dy
        // TODO: ADD THIS BACK
//        if (KeyInput.isPressed(KeyEvent.VK_SPACE))
            Window.getBuffer().translate(-dx, -dy);
    }

    @Override
    public void render() {
    }

    @Override
    public void init() {
        // setup window width + height
        int windowWidth = Window.get().getWidth();
        int windowHeight = Window.get().getHeight();
        // calculate camera scale
        double dx = (windowWidth / cameraSize.x);
        double dy = (windowHeight / cameraSize.y);
        // set camera scale
        Window.getBuffer().scale(dx, dy);
    }

    /**
     * Set the entity the camera will focus on
     * @param e Entity to focus on
     */
    public void setFocus(Entity e) {
        focus = e;
    }

    public Vector2 getCameraSize() {
        return this.cameraSize;
    }

    public Entity getFocus() {
        return this.focus;
    }
}

/**
 * Camera class
 */

package game.entities;

import display.Window;
import utils.math.Vector2;
import utils.tile.TileSheet;

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
        Vector2 focusCenter = focus.getCenter();
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
}

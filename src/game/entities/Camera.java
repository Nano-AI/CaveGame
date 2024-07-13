package game.entities;

import display.Window;
import utils.math.Vector2;
import utils.tile.TileSheet;

public class Camera extends Entity {
    private Entity focus;
    private Vector2 cameraSize;
    private Vector2 worldSize;

    public Camera(Vector2 position, Vector2 cameraSize, Vector2 worldSize) {
        super(position);
        this.cameraSize = cameraSize;
        this.worldSize = worldSize;
    }

    public void move(int dx, int dy) {
        this.position.add(dx, dy);
    }

    @Override
    public void update() {
        Vector2 focusCenter = focus.getCenter();
        //              player in the center location           center pos
        int dx = (int) ((focusCenter.x - cameraSize.x / 2) - position.x);
        //              player in the center                    center pos
        int dy = (int) ((focusCenter.y - cameraSize.y / 2) - position.y);

        if (position.x + dx <= 0) {
            dx = (int) (-position.x);
        } else if (position.x + dx > worldSize.x) {
            dx = (int) (worldSize.x - position.x);
        }

        if (position.y + dy <= 0) {
            dy = (int) (-position.y);
        } else if (position.y + dy > worldSize.y) {
            dy = (int) (worldSize.y - position.y);
        }

        position.x += dx;
        position.y += dy;
        Window.getBuffer().translate(-dx, -dy);
    }

    @Override
    public void render() {
    }

    @Override
    public void init() {
        int windowWidth = Window.get().getWidth();
        int windowHeight = Window.get().getHeight();
        double dx = (windowWidth / cameraSize.x);
        double dy = (windowHeight / cameraSize.y);
        System.out.println(dx + " " + dy);
        Window.getBuffer().scale(dx, dy);
    }

    public void setFocus(Entity e) {
        focus = e;
    }
}

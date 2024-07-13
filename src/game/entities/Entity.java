package game.entities;

import game.scenes.Scene;
import utils.math.Rect;
import utils.resources.Images;
import utils.tile.TileImage;
import utils.tile.TileSheet;
import utils.math.Vector2;

import java.awt.image.BufferedImage;

public abstract class Entity {
    protected Vector2 position;
    protected Vector2 scale;
    protected Images image;
    protected int frameIndex = 0;
    protected Rect hitbox;
    protected Scene scene;

    public Entity(Vector2 position) {
        this.position = position;
    }

    public Entity(Vector2 position, Vector2 scale, TileSheet image) {
        this.position = position;
        this.scale = scale;
        this.image = image;
    }

    public abstract void update();

    public abstract void render();

    public abstract void init();

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale.set(scale);
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public TileImage getImage() {
        return image.getImage(getFrameIndex());
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + scale.x * getImage().normal().getWidth() / 2, position.y + scale.y * getImage().normal().getHeight() / 2);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public Rect getHitbox() {
        return this.hitbox;
    }
}

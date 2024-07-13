package game.entities;

import display.Window;
import input.KeyInput;
import utils.DirectionType;
import utils.math.Rect;
import utils.tile.TileSheet;
import utils.Time;
import utils.math.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public Player(Vector2 position, Vector2 scale, TileSheet image) {
        super(position, scale, image);
    }

    @Override
    public void update() {
        Vector2 direction = new Vector2();

        if (KeyInput.isPressed(KeyEvent.VK_W)) {
            direction.y = -1;
        } else if (KeyInput.isPressed(KeyEvent.VK_S)) {
            direction.y = 1;
        }

        if (KeyInput.isPressed(KeyEvent.VK_A)) {
            direction.x = -1;
        } else if (KeyInput.isPressed(KeyEvent.VK_D)) {
            direction.x = 1;
        }

        Vector2 dp = direction.unitVector().mul(Time.deltaT()).mul(100);

        this.hitbox.position.add(dp);

        boolean canMove = true;
        for (Entity e : scene.getEntities()) {
            if (e.hitbox.isIntersecting(this.hitbox) && e != this) {
                canMove = false;
                break;
            }
        }

        if (canMove) {
            this.position.add(dp);
        } else {
            this.hitbox.position.subtract(dp);
        }
    }

    @Override
    public void render() {
        Graphics2D graphics = Window.getBuffer();

//        AffineTransform old = graphics.getTransform();

//        graphics.rotate(Math.toRadians(angle), position.x, position.y);
        BufferedImage image = getImage().normal();
        double scaleX = scale.x * image.getWidth();
        double scaleY = scale.y * image.getHeight();
        graphics.drawImage(
                image,
                (int) (position.x - scaleX / 2),
                (int) (position.y - scaleY / 2),
                (int) scaleX,
                (int) scaleY,
                null
        );
    }

    @Override
    public void init() {
        BufferedImage image = getImage().normal();
        double scaleX = scale.x * image.getWidth();
        double scaleY = scale.y * image.getHeight();
        this.hitbox = new Rect(
                (int) (position.x - scaleY / 2 + scaleY * 0.1),
                (int) position.y + scaleY / 2 - scaleY * 0.2,
                scaleX * 0.8,
                scaleY * 0.2
        );
    }
}

/**
 * Goblin class
 */
package game.entities;

import display.Window;
import utils.Time;
import utils.math.Rect;
import utils.math.Vector2;

import java.awt.image.BufferedImage;

public class Goblin extends Entity {
    public Goblin(Vector2 position, Vector2 scale) {
        super(position, scale);
        this.hp = 100;
    }

    @Override
    public void update() {
        Vector2 distance = this.position.distanceTo(scene.getPlayer().position);
        if (distance.magnitude() < 200) {
            movePosition(distance.unitVector().times(Time.deltaT() * 50));
        }

        Vector2 flying = distance.times(-1 * Time.deltaT() * 1000);

        if (scene.getPlayer().isAttacking(this)) {
            movePosition(flying);
            this.hp -= scene.getPlayer().getEquippedItem().getDamage();
            System.out.println(this.hp);
        }
    }

    @Override
    public void render() {
        BufferedImage image = getImage().normal();
        double scaleX = scale.x * image.getWidth();
        double scaleY = scale.y * image.getHeight();
        Window.getBuffer().drawImage(
                image,
                (int) (position.x - scaleX / 2),
                (int) (position.y - scaleY / 2),
                (int) (scaleX),
                (int) (scaleY),
                null
        );
        Window.getBuffer().drawRect(
                (int) (hitbox.position.x),
                (int) (hitbox.position.y),
                (int) hitbox.size.x,
                (int) hitbox.size.y
        );
    }

    @Override
    public void init() {
        double scaleX = scale.x * getImage().normal().getWidth();
        double scaleY = scale.y * getImage().normal().getHeight();
        this.hitbox = new Rect(
                (int) (position.x - scaleX / 2 + scaleX * 0.1),
                (int) position.y + scaleY / 2 - scaleY * 0.2,
                scaleX * 0.8,
                scaleY * 0.2
        );
    }
}

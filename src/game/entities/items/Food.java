package game.entities.items;

import display.Window;
import game.entities.Entity;
import game.scenes.Scene;
import utils.Time;
import utils.math.Vector2;
import utils.resources.Assets;

import javax.swing.*;
import java.io.File;
import java.util.Vector;

public class Food extends Item {
    private Vector2 position;
    private Vector2 size;
    private Scene scene;
    private boolean hide = false;
    String[] randomWords = {
            yummers,
            bet that was good, huh?,
            how did a zombie drop food?,
            that looked scrumptious,
            wowza,
            eating on the job is prohibited,
            can you maybe close your mouth and chew,
            why do you eat so loud
    };
    /**
     * @param e Owner of item
     */
    public Food(Entity e) {
        super(e);
    }

    public Food(Scene s, Entity e, Vector2 pos, Vector2 size) {
        super(e);
        this.position = pos;
        this.size = size;
        File f = Assets.getRandomFile("assets/images/foods");
        this.image = Assets.getImage(f.getAbsolutePath());
        this.scene = s;
    }

    @Override
    public void render() {
        if (hide) return;
        Window.getBuffer().drawImage(
                this.image,
                (int) (this.position.x),
                (int) (this.position.y),
                (int) (this.size.x),
                (int) (this.size.y),
                null
        );
    }

    @Override
    public void update() {
        if (hide) return;
        Vector2 distanceTo = scene.getPlayer().getPosition().distanceTo(position);
        float mag = distanceTo.magnitude();
        if (!scene.getPlayer().getInventory().inventoryFull && mag <= 32) {
            hide = true;
            scene.getPlayer().getInventory().addToInventory(this);
            Assets.getSound("assets/sounds/01_chest_open_1_converted.wav").play();
        } else if (!scene.getPlayer().getInventory().inventoryFull && mag <= 100) {
            this.position.subtract(distanceTo.times(Time.deltaT()));
        }
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public void playUseSound() {

    }

    @Override
    public void use() {
        File soundFile = Assets.getRandomFile("assets/sounds/eat");
        Assets.getSound(soundFile.getAbsolutePath()).play();
    }
}

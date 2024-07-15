/**
 * Goblin class
 */
package game.entities;

import display.Window;
import utils.Time;
import utils.Timer;
import utils.math.Range;
import utils.math.Rect;
import utils.math.Vector2;
import utils.resources.Assets;
import utils.resources.Sound;
import utils.tile.TileSheet;

import java.awt.image.BufferedImage;

public class Goblin extends Entity {
    private Sound[] walkSounds;
    private Sound[] damageSounds;
    private Sound deadSound;
    private Timer walkAnimationTimer = new Timer(0.1);
    private Timer deadAnimationTimer = new Timer(0.2);
    private Timer walkSoundTimer = new Timer(0.2);
    private Timer animationTimer;
    private Range animationFrames;
    private TileSheet walk;
    private TileSheet attack;
    private TileSheet damage;
    private TileSheet die;
    private boolean dead = false;
    private boolean hide = false;

    public Goblin(Vector2 position, Vector2 scale) {
        super(position, scale);
        this.hp = 100;
//        idle = Assets.getTileSheet("assets/tiles/Characters/Human/Idle.png", 32, 32);
        this.walkSounds = new Sound[] {
            Assets.getSound("assets/sounds/25_orc_walk_stone_1_converted.wav"),
                Assets.getSound("assets/sounds/25_orc_walk_stone_2_converted.wav"),
                Assets.getSound("assets/sounds/25_orc_walk_stone_3_converted.wav")
        };
        this.damageSounds = new Sound[] {
                Assets.getSound("assets/sounds/21_orc_damage_1_converted.wav"),
                Assets.getSound("assets/sounds/21_orc_damage_2_converted.wav"),
                Assets.getSound("assets/sounds/21_orc_damage_3_converted.wav")
        };
        this.deadSound = new Sound("assets/sounds/24_orc_death_spin_converted.wav");
        this.walk = Assets.getTileSheet("assets/tiles/Characters/Orc/Walk.png", 32, 32);
        this.attack = Assets.getTileSheet("assets/tiles/Characters/Orc/Attack.png", 32, 32);
        this.damage = Assets.getTileSheet("assets/tiles/Characters/Orc/Dmg.png", 32, 32);
        this.die = Assets.getTileSheet("assets/tiles/Characters/Orc/Die.png", 32, 32);
        this.animationFrames = new Range(0, 0);
        this.animationTimer = walkAnimationTimer;
        this.image = walk;
    }

    @Override
    public void update() {
        if (hide) return;
        animationTimer.update();

        Vector2 distance = this.position.distanceTo(scene.getPlayer().position);

        if (hp <= 0 && !dead) {
            deadSound.play();
            this.image = die;
            this.dead = true;
            this.hitbox.position.set(0, 0);
            this.hitbox.size.set(0, 0);
            animationFrames.setRange(0, 11);
            animationTimer = deadAnimationTimer;
            animationFrames.reset();
        }

        if (image == die && dead && !animationFrames.hasCapped()) {
            if (animationTimer.isDone()) {
                animationFrames.increment();
                animationTimer.restart();
            }
            if (animationFrames.hasCapped()) {
                this.hide = true;
                this.remove = true;
            }
            return;
        }

        if (image == damage && animationFrames.hasCapped()) {
            image = walk;
            animationFrames.reset();
        }

        if (distance.magnitude() < 200) {
            Vector2 move = distance.unitVector().times(Time.deltaT() * 50);
//            this.animationTimer.update();
            if (!dead) {
                movePosition(move);

                int prevVal = animationFrames.getValue() - animationFrames.getMin();
                if (move.y < 0 && move.x < 0) {
                    animationFrames.setRange(12, 15);
                } else if (move.y < 0) {
                    animationFrames.setRange(8, 11);
                } else if (move.x < 0) {
                    animationFrames.setRange(4, 7);
                } else if (move.x > 0 || move.y > 0) {
                    animationFrames.setRange(0, 3);
                }

                animationFrames.setValue(prevVal + animationFrames.getMin());

                if (!move.isZero() && animationTimer.isDone()) {
                    animationFrames.increment();
                    getRandomAudio(walkSounds).play();
                    animationTimer.restart();
                } else if (move.isZero()) {
                    animationFrames.reset();
                }
            }
        }

        Vector2 flying = distance.times(-1 * Time.deltaT() * 100);

        if (scene.getPlayer().isAttacking(this)) {
            movePosition(flying);
            this.hp -= scene.getPlayer().getEquippedItem().getDamage();
            this.image = damage;
            this.animationFrames.reset();
            getRandomAudio(damageSounds).play();
        }
    }

    @Override
    public void render() {
        if (hide) return;
        BufferedImage image = this.image.getImage(animationFrames.getValue()).normal();
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
        double scaleX = scale.x * 8;
        double scaleY = scale.y * 8;
        // setup custom hitbox
        this.hitbox = new Rect(
                (int) (position.x - scaleX / 2 + scaleX * 0.2),
                (int) position.y + scaleY / 2 - scaleY * 0.3,
                scaleX * 0.6,
                scaleY * 0.2
        );

    }

    private Sound getRandomAudio(Sound[] sounds) {
        return sounds[(int) (Math.random() * sounds.length)];
    }
}

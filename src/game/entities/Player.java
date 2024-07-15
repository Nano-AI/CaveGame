/**
 * Player class
 */
package game.entities;

import display.Window;
import game.entities.items.Item;
import game.entities.items.Sword;
import input.KeyInput;
import input.MouseInput;
import utils.DirectionType;
import utils.math.Range;
import utils.math.Rect;
import utils.resources.Assets;
import utils.resources.Sound;
import utils.tile.TileSheet;
import utils.Time;
import utils.math.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    // different tilesheets for different behaviors
    private TileSheet idle;
    private TileSheet walk;
    private TileSheet attack;
    // TODO: Create timer class to simplify this
    // time between each walk animation update
    private final double walkTimeToSwapAnimation = 0.1;
    // general time to swap the animation
    private double timeToSwapAnimation = walkTimeToSwapAnimation;
    // timer for swapping animation
    private double timeLeftToSwapAnimation;
    // time between each sound
    private final double walkTimeToPlaySound = 0.2;
    // timer for time between each sound
    private double timeLeftToPlaySound = walkTimeToPlaySound;
    // range of frameIndexes for the player
    private Range animationFrames;
    // sword
    private Sword sword;

    // different walk sounds
    private Sound walk1;
    private Sound walk2;
    private Sound walk3;

    public Player(Vector2 position, Vector2 scale) {
        super(position, scale);
        // init walk sounds
        walk1 = Assets.getSound("assets/sounds/16_human_walk_stone_1_converted.wav");
        walk2 = Assets.getSound("assets/sounds/16_human_walk_stone_2_converted.wav");
        walk3 = Assets.getSound("assets/sounds/16_human_walk_stone_3_converted.wav");
        // int different tilesheets
        idle = Assets.getTileSheet("assets/tiles/Characters/Human/Idle.png", 32, 32);
        walk = Assets.getTileSheet("assets/tiles/Characters/Human/Walk.png", 32, 32);
        attack = Assets.getTileSheet("assets/tiles/Characters/Human/Attack.png", 32, 32);
        // setup inventory
        this.inventory = new Item[this.inventorySize];
        // setup image
        this.image = this.walk;
        // setup animation frames
        this.animationFrames = new Range(0, 0);
        // setup swords
        this.sword = new Sword(this);

        // add the sword to inventory
        inventory[0] = sword;
        inventory[0].setEquipped(true);
    }

    @Override
    public void update() {
        // the direction player wants to move in
        Vector2 direction = new Vector2();
        // wasd booleans
        boolean w = false, a = false, s = false, d = false;

        // process input keys
        if (KeyInput.isPressed(KeyEvent.VK_W)) {
            direction.y = -1;
            w = true;
        } else if (KeyInput.isPressed(KeyEvent.VK_S)) {
            direction.y = 1;
            s = true;
        }

        if (KeyInput.isPressed(KeyEvent.VK_A)) {
            direction.x = -1;
            a = true;
        } else if (KeyInput.isPressed(KeyEvent.VK_D)) {
            direction.x = 1;
            d = true;
        }

        // What relative frame index are we on? regardless of what direction, what frame # are we on
        int prevVal = animationFrames.getValue() - animationFrames.getMin();
        // set range of animation frames depending on key press. check out the walk player tile in assets
        if (w && a) {
            animationFrames.setRange(12, 15);
        } else if (w) {
            animationFrames.setRange(8, 11);
        } else if (a) {
            animationFrames.setRange(4, 7);
        } else if (d || s) {
            animationFrames.setRange(0, 3);
        }

        // Set the animation frame to whatever the previous relative index + new offset
        // this means that they will look the same, direction will change though
        animationFrames.setValue(prevVal + animationFrames.getMin());

        // update timer variables
        timeLeftToSwapAnimation -= Time.deltaT();
        timeLeftToPlaySound -= Time.deltaT();
        // if animatin is set to walking, we're moving, and it's time to play a sound
        if (image == walk && !direction.isZero() && timeLeftToPlaySound <= 0) {
            // randomly play a soudn
            int random = (int) (Math.random() * 100);
            if (random <= 33) {
                walk1.play();
            } else if (random <= 66) {
                walk2.play();
            } else {
                walk3.play();
            }
            // reset the timer
            timeLeftToPlaySound = walkTimeToPlaySound;
        }
        // check if it's time to swap animation
        if (timeLeftToSwapAnimation <= 0) {
            // are we supposed to animate rn?
            boolean canAnimate = false;
            int i = 0;
            // if we're walking
            if (image == walk) {
                // increment animation frame index
                i = animationFrames.increment();
                // if we're moving then yes, animate
                if (!direction.isZero()) {
                    canAnimate = true;
                } else {
                    // otherwise reset to default
                    setFrameIndex(animationFrames.getMin() - 1);
                }
            }
            // if we're attacking, we want to animate regardless
            else if (image == attack) {
                canAnimate = true;
                i = animationFrames.increment();
            }

            // if we can animate, update the frame index and reset timer
            if (canAnimate) {
                setFrameIndex(i);
                timeLeftToSwapAnimation = timeToSwapAnimation;
            }
        }

        // turn the direction into a unit vector (that way diagonals don't move faster)
        // then mult by deltaT to ensure same speed regardless of frame rate
        // then mult by a constant, speed
        Vector2 dp = direction.unitVector().multiply(Time.deltaT()).multiply(100);
        // if we're not attacking, move
        if (!attacking && !inventory[equippedSlot].isUsing()) {
            movePosition(dp);
        }

        // iterate through every item
        for (Item item : inventory) {
            // update item for cooldowns
            if (item != null) {
                item.update();
            }
        }

        boolean mouseDown = MouseInput.isPressed(MouseEvent.BUTTON1);

        // if mouse is down
        if (mouseDown) {
            // make sure equipped item isn't null
            if (inventory[equippedSlot] != null) {
                // if it's a sword
                if (inventory[equippedSlot] == sword) {
                    // use it
                    inventory[equippedSlot].use();
                    // we havnet reset, we need to reset
                    this.reset = false;
                }
            }
        }

        // if we're attacking and we haven't reset
        if (this.attacking && !reset) {
            // reset
            reset = true;
            // update attack
            this.image = attack;
            // reset frames so index = 0
            this.animationFrames.reset();
            // calculate speed of animation depending on the item speed
            this.timeToSwapAnimation = this.sword.getAttackSpeed() / (attack.getSheetWidth());
            // reset timer
            this.timeLeftToSwapAnimation = this.timeToSwapAnimation;
        }
        // if the attack animation is done
        else if (!this.attacking && !reset && this.animationFrames.hasCapped()) {
            // reset
            reset = true;
            // update image
            this.image = walk;
            // reset frames
            this.animationFrames.reset();
            // reset timer
            this.timeToSwapAnimation = walkTimeToSwapAnimation;
        }
    }

    @Override
    public void render() {
        Graphics2D graphics = Window.getBuffer();

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
        graphics.drawRect(
                (int) (hitbox.position.x),
                (int) (hitbox.position.y),
                (int) hitbox.size.x,
                (int) hitbox.size.y
        );
        for (Item item : inventory) {
            if (item != null) {
                item.render();
            }
        }
    }

    @Override
    public void init() {
        BufferedImage image = getImage().normal();
        double scaleX = scale.x * 8;
        double scaleY = scale.y * 8;
        // setup custom hitbox
        this.hitbox = new Rect(
                (int) (position.x - scaleX / 2 + scaleX * 0.2),
                (int) position.y + scaleY / 2 - scaleY * 0.4,
                scaleX * 0.6,
                scaleY * 0.2
        );
    }
}

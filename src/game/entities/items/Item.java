/**
 * Item class for weapons, tools, etc
 */
package game.entities.items;

import game.entities.Entity;
import utils.Timer;
import utils.math.Vector2;
import utils.resources.Sound;

import java.awt.image.BufferedImage;

public abstract class Item {
    // item stats
    protected int baseDamage = 0;
    protected double critChance = 0.0;
    protected double critMultiplier = 0;
    protected double attackSpeed = 0;
    protected double timeLeftToAttack = 0;
    protected Timer pickUpTimer = new Timer(3);
    protected Vector2 position;
    // image
    protected BufferedImage image = null;
    // image offset
    protected Vector2 offset;
    // item owner
    protected Entity owner;
    // is equipped
    protected boolean equipped = false;
    // audio clip
    protected Sound audioClip = null;
    // how long item is used for
    protected double useTime = 0.05;
    // timer for item use
    protected double useTimeTimer = 0;
    // if using
    protected boolean using = false;

    public boolean hide = false;

    /**
     * @param e Owner of item
     */
    public Item(Entity e) {
        this.owner = e;
    }

    /**
     * Render function
     */
    public abstract void render();

    /**
     * Update function
     */
    public abstract void update();

    /**
     * @return If the item can be used
     */
    public abstract boolean canUse();

    /**
     * @return If the item is equipped
     */
    public boolean isEquipped() {
        return equipped;
    }

    /**
     * @param equipped Is item equipped
     */
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    /**
     * @return Get the item image
     */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Play item sound
     */
    public abstract void playUseSound();

    /**
     * Use the item
     */
    public abstract void use();

    /**
     * @return If the item is being used
     */
    public boolean isUsing() {
        return this.using;
    }

    /**
     * @return Get damage of weapon
     */
    public double getDamage() {
        if (using) {
            return baseDamage * (Math.random() <= critChance ? critMultiplier : 1);
        }
        return 0;
    }

    /**
     * @return Weapon attack speed
     */
    public double getAttackSpeed() {
        return this.attackSpeed;
    }

    public void setPosition(Vector2 position) {
        if (this.position == null) {
            this.position = new Vector2(position);
        }
        this.position.set(position);
    }

    public void resetTimer() {
        this.pickUpTimer.restart();
    }
}

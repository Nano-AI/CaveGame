/**
 * Entty class
 */
package game.entities;

import game.entities.items.Item;
import game.scenes.Scene;
import utils.math.Rect;
import utils.resources.Images;
import utils.tile.TileImage;
import utils.tile.TileSheet;
import utils.math.Vector2;

// implemented comparable to sort by y-axis to change z-index render order
public abstract class Entity implements Comparable<Entity> {
    // position
    protected Vector2 position;
    // scale
    protected Vector2 scale;
    // images
    protected Images image;
    // index of frame (check tilesheets. left to right first, then top down)
    protected int frameIndex = 0;
    // hitbox of entity
    protected Rect hitbox;
    // scene that entity is in
    protected Scene scene;
    // hp of entity
    protected double hp;
    // if entity is attacking
    protected boolean attacking = false;
    // the attack range of entity
    protected double attackRange = 40.0;
//    protected double attackRange = 10000.0;
    // inventory size of entity
    protected int inventorySize = 8;
    // items in inventory
    protected Item[] inventory;
    // what slot they have equipped
    protected int equippedSlot = 0;
    // if they have to reset animation
    protected boolean reset = true;
    protected boolean remove = false;

    public Entity(Vector2 position) {
        this.position = position;
    }

    public Entity(Vector2 position, Vector2 scale) {
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

    /**
     * Get center of the player using the position and scale.
     * @return Vector of the center of the player;
     */
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

    /**
     * Used for sorting the entity list by y-axis
     * @param o the object to be compared.
     * @return Greater y axis
     */
    @Override
    public int compareTo(Entity o) {
        return Double.compare(position.y, o.position.y);
    }

    /**
     * Move position by dx and dy
     * @param dx Change x by dx
     * @param dy Change y by dy
     */
    public void movePosition(double dx, double dy) {
        // add to the hitbox
        hitbox.position.add(dx, dy);
        // can move by default
        boolean canMove = true;
        // iterate through every entity
        for (Entity e : scene.getEntities()) {
            // if entity is colliding with another entity
            if (e.hitbox != null && e.hitbox.isIntersecting(this.hitbox) && e != this && !e.getClass().equals(this.getClass())) {
                // cannot move
                canMove = false;
                break;
            }
        }

        // if the changed hitbox doesn't collide w anything just move it
        if (canMove) {
            this.position.add(dx, dy);
        } else {
            // otherwise reset the hitbox
            this.hitbox.position.subtract(dx, dy);
        }
    }

    public void movePosition(Vector2 dp) {
        movePosition(dp.x, dp.y);
    }

    /**
     * Update the position of the entity
     * @param x Change in x
     * @param y Change in y
     */
    public void setPosition(double x, double y) {
        // update the position
        this.position.x = x;
        this.position.y = y;
        // change the hitbox and ensure it's centered
        this.hitbox.position.x = x - this.getImage().normal().getWidth() * 0.5;
        this.hitbox.position.y = y - this.getImage().normal().getHeight() * 0.5;
    }

    public void setPosition(Vector2 p) {
        setPosition(p.x, p.y);
    }

    public void damage(double amount) {
        this.hp -= amount;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getHp() {
        return this.hp;
    }

    public boolean isAttacking(Entity e) {
        if (inventory[equippedSlot] == null) {
            return false;
        }
        return this.attacking && inAttackRange(e);
    }

    public boolean inAttackRange(Entity e) {
        return e.position.distanceTo(this.position).magnitude() < attackRange;
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public void setAttacking(boolean isAttacking) {
        this.attacking = isAttacking;
    }

    public Item getEquippedItem() {
        return inventory[equippedSlot];
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean shouldRemove() {
        return this.remove;
    }
}

/**
 * Sword
 */
package game.entities.items;

import game.entities.Entity;
import utils.Time;
import utils.math.Vector2;
import utils.resources.Assets;
import utils.resources.Sound;

public class Sword extends Item {
    // three random sounds
    private Sound audio1;
    private Sound audio2;
    private Sound audio3;

    public Sword(Entity owner) {
        super(owner);
        // setup sounds
        audio1 = Assets.getSound("assets/sounds/07_human_atk_sword_1_converted.wav");
        audio2 = Assets.getSound("assets/sounds/07_human_atk_sword_2_converted.wav");
        audio3 = Assets.getSound("assets/sounds/07_human_atk_sword_3_converted.wav");
        // setup base vars
        this.baseDamage = 1000;
        this.critChance = .10;
        this.critMultiplier = 2;
        this.attackSpeed = 0.75;
        this.image = Assets.getTileSheet("assets/tiles/Tileset/fullswords.png", 16, 16).getImage(1).normal();
        this.offset = new Vector2(8, -8);
        this.audioClip = audio1;
    }

    public void update() {
        // if it's being used, reduce the use timer
        if (using && useTimeTimer > 0) {
            useTimeTimer -= Time.deltaT();
        } else if (using && useTimeTimer <= 0) {
            // once it's done, stop using it
            using = false;
            owner.setAttacking(false);
            // ask owner to reset animation
            owner.setReset(false);
        }
        // reduce the cooldown
        this.timeLeftToAttack -= Time.deltaT();
    }


    @Override
    public void render() {
    }

    @Override
    public boolean canUse() {
        return timeLeftToAttack <= 0;
    }

    @Override
    public void playUseSound() {
        if (canUse()) {
            int random = (int) (Math.random() * 100);
            if (random <= 33) {
                audio1.play();
            } else if (random <= 66) {
                audio2.play();
            } else {
                audio3.play();
            }
        }
    }

    @Override
    public void use() {
        if (!canUse()) return;
        playUseSound();
        using = true;
        owner.setAttacking(true);
        useTimeTimer = useTime;
        timeLeftToAttack = attackSpeed;
    }
}

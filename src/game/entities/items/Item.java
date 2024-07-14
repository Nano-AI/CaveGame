package game.entities.weapons;

import game.entities.Entity;

import java.awt.image.BufferedImage;

public abstract class Item {
    public int baseDamage;
    public double critChance;
    public double critMultiplier ;
    public double attackSpeed;
    public double timeLeftToAttack;
    public BufferedImage image;
}

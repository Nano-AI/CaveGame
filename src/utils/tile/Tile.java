package utils.tile;

import java.awt.image.BufferedImage;

public class Tile {
    public int id = 0;
    public boolean horizontalFlip = false;
    public boolean verticalFlip = false;
    public boolean diagonalFlip = false;

    public Tile(int id) {
        this.id = id;
    }

    public Tile(int id, boolean horizontalFlip, boolean verticalFlip, boolean diagonalFlip) {
        this.id = id;
        this.horizontalFlip = horizontalFlip;
        this.verticalFlip = verticalFlip;
        this.diagonalFlip = diagonalFlip;
    }
}

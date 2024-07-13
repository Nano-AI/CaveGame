package utils.tile;

import utils.resources.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileImage {
    private BufferedImage normal, diagonal;

    public TileImage(BufferedImage normal) {
        this.normal = normal;
        this.diagonal = Image.diagonallyFlip(normal);
    }

    public BufferedImage normal() {
        return normal;
    }

    public BufferedImage diagonal() {
        return diagonal;
    }
}

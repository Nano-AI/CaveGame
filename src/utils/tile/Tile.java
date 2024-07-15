/**
 * A specific tile
 */
package utils.tile;

import utils.math.Rect;

public class Tile {
    // id of tile
    public int id = 0;
    // flip flags
    public boolean horizontalFlip = false;
    public boolean verticalFlip = false;
    public boolean diagonalFlip = false;
    public boolean renderOnTop = false;
    // sheet
    public TileSheet sheet;
    // image
    public TileImage image;

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

/**
 * Map class that allows to render the TileSheets and TileMaps
 */
package game.scenes;

import display.Window;
import game.entities.Empty;
import game.entities.Entity;
import utils.resources.Assets;
import utils.tile.*;
import utils.math.*;

import java.awt.*;
import java.util.List;

public class Map {
    // tilemap
    private TileMap map;
    // scale and position
    private Vector2 tileScale, position;
    // hitboxes
    private List<Rect> hitboxes;

    public Map() {
        this(new Vector2(1, 1), new Vector2());
    }

    public Map(Vector2 position, Vector2 mapScale) {
        this.position = position;
        this.tileScale = mapScale;
    }

    /**
     * Set map based off Tiled file
     * @param map Location of the TileMap
     */
    public void setMap(String map) {
//        this.map = Assets.getTileMap(map);
        this.map = TileMap.fromTiled(map);
    }

    /**
     * Render the map
     */
    public void renderMap() {
        // error checking
        if (this.map == null) {
            throw new RuntimeException("TileMap not initialized");
        }
        if (this.map.getSheets().isEmpty()) {
            throw new RuntimeException("TileSheet not initialized");
        }

        // get the layers of the maps
        TileMapLayer[] layers = this.map.getLayers();
        // get the window buffer graphics
        Graphics2D g = Window.getBuffer();

        // get the tile width and tile height
        int tileWidth = (int) (this.map.getTileWidth() * tileScale.x);
        int tileHeight = (int) (this.map.getTileHeight() * tileScale.y);
        // iterate through layers
        for (TileMapLayer layer : layers) {
            // iterate through each tile
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    // get the tile
                    Tile t = layer.at(y, x);
                    // make sure tile != 0 b/c we don't need to render that
                    // and make sure it has a sheet
                    if (t.id > 0 && t.sheet != null) {
                        // get the tile image by the id
                        TileImage image = t.sheet.getImage(t.id - 1);
                        // if image is null, just continue
                        if (image == null)  {
                            continue;
                        }
                        // get the x & y postiion of the tile
                        int xPos = (int) position.x + tileWidth * x;
                        int yPos = (int) position.y + tileHeight * y;

                        // flip the image based off it's flip flags
                        if (t.diagonalFlip) {
                            // Draw diagonally flipped image
                            g.drawImage(image.diagonal(), xPos + (tileWidth / 2), yPos + (tileHeight / 2), -tileWidth, -tileHeight, null);
                        } else if (t.horizontalFlip) {
                            // Draw horizontally flipped image
                            g.drawImage(image.normal(), xPos + (tileWidth / 2), yPos - (tileHeight / 2), -tileWidth, tileHeight, null);
                        } else if (t.verticalFlip) {
                            // Draw vertically flipped image
                            g.drawImage(image.normal(), xPos - (tileWidth / 2), yPos + (tileHeight / 2), tileWidth, -tileHeight, null);
                        } else {
                            // Draw normally
                            g.drawImage(image.normal(), xPos - (tileWidth / 2), yPos - (tileHeight / 2), tileWidth, tileHeight, null);
                        }
                    }
                }
            }
        }
    }

    public boolean collides(Rect r) {
        for (Rect rect : hitboxes) {
            if (rect.isIntersecting(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add entities to a passed array based off of it's name
     * @param layerName Name of the layer to add to the entities list
     * @param entities List where entities will be added
     */
    public void addHitboxes(String layerName, List<Entity> entities) {
        // get tile size
        int tileWidth = (int) (this.map.getTileWidth() * tileScale.x);
        int tileHeight = (int) (this.map.getTileHeight() * tileScale.y);
        // get the player
        TileMapLayer layer = map.getLayer(layerName);
        // error check
        if (layer == null) {
            throw new RuntimeException("Unknown layer " + layerName);
        }
        // iterate through every tile
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                // make sure it's not empty, and it's supposed to be in that layer
                if (layer.at(y, x).id != 0 && layer.at(y, x).id - layer.at(y, x).sheet.firstGId > 0) {
                    // create a rect
                    Rect r = new Rect(
                            x * tileWidth - tileWidth / 2,
                            y * tileHeight - tileHeight / 2,
                            map.getTileWidth() * tileScale.x,
                            map.getTileHeight() * tileScale.y
                    );
                    // create an entity
                    Entity e = new Empty(r.position);
                    // set the hitbox
                    e.setHitbox(r);
                    // add it to the list
                    entities.add(e);
                }
            }
        }
    }

    public int getWidth() {
        return this.map.getWidth() * this.map.getTileWidth() * (int) (tileScale.x / 2);
    }

    public int getHeight() {
        return this.map.getHeight() * this.map.getTileHeight() * (int) (tileScale.y / 2);
    }
}

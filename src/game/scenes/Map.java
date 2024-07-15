/**
 * Map class that allows to render the TileSheets and TileMaps
 */
package game.scenes;

import display.Window;
import game.entities.Camera;
import game.entities.Empty;
import game.entities.Entity;
import utils.resources.Assets;
import utils.tile.*;
import utils.math.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Map {
    // tilemap
    private TileMap map;
    // scale and position
    private Vector2 tileScale, position;
    // hitboxes
    private List<Rect> hitboxes;
    public Camera camera;
    private BufferedImage baseMap = null;
    private BufferedImage aboveMap = null;

    public void setupImages() {
        if (this.map == null) {
            throw new RuntimeException("TileMap not initialized");
        }
        if (this.map.getSheets().isEmpty()) {
            throw new RuntimeException("TileSheet not initialized");
        }

        System.out.println(this.map.getTileHeight() + " " + this.map.getTileWidth());
        System.out.println(this.map.getHeight() + " " + this.map.getWidth());
        baseMap = new BufferedImage((int) (this.map.getTileWidth() * this.map.getWidth() * this.tileScale.x),
                (int) (this.map.getTileHeight() * this.map.getHeight() * this.tileScale.y), BufferedImage.TYPE_INT_ARGB);
        aboveMap = new BufferedImage((int) (this.map.getTileWidth() * this.map.getWidth() * this.tileScale.x),
                (int) (this.map.getTileHeight() * this.map.getHeight() * this.tileScale.y), BufferedImage.TYPE_INT_ARGB);

        TileMapLayer[] layers = this.map.getLayers();
        Graphics2D baseGraphics = baseMap.createGraphics();
        Graphics2D aboveGraphics = aboveMap.createGraphics();

        int tileWidth = (int) (this.map.getTileWidth() * tileScale.x);
        int tileHeight = (int) (this.map.getTileHeight() * tileScale.y);

        for (TileMapLayer layer : layers) {
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    Tile t = layer.at(y, x);
                    Graphics2D g;
                    if (t.renderOnTop) {
                        g = aboveGraphics;
                    } else {
                        g = baseGraphics;
                    }

                    int xPos = (int) tileWidth * x;
                    int yPos = (int) tileHeight * y;

                    if (t.id > 0 && t.sheet != null) {
                        TileImage image = t.sheet.getImage(t.id - 1);
                        if (image == null) {
                            continue;
                        }

                        int drawX = xPos - (tileWidth / 2);
                        int drawY = yPos - (tileHeight / 2);
                        int width = tileWidth;
                        int height = tileHeight;

                        if (t.diagonalFlip) {
                            g.drawImage(image.diagonal(), xPos + (tileWidth / 2), yPos + (tileHeight / 2), -tileWidth, -tileHeight, null);
                        } else if (t.horizontalFlip) {
                            g.drawImage(image.normal(), xPos + (tileWidth / 2), yPos - (tileHeight / 2), -tileWidth, tileHeight, null);
                        } else if (t.verticalFlip) {
                            g.drawImage(image.normal(), xPos - (tileWidth / 2), yPos + (tileHeight / 2), tileWidth, -tileHeight, null);
                        } else {
                            g.drawImage(image.normal(), drawX, drawY, width, height, null);
                        }
                    }
                }
            }
        }

        baseGraphics.dispose();
        aboveGraphics.dispose();
    }

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

    public void renderMap() {
        renderMap(false);
    }

    /**
     * Render the map
     */
    public void renderMap(boolean onTop) {
        BufferedImage image = onTop ? aboveMap : baseMap;
        Window.getBuffer().drawImage(
                image,
                (int) (position.x), (int) position.y, null);
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
                Tile t = layer.at(y, x);
                // make sure it's not empty, and it's supposed to be in that layer
                if (t.id != 0 && t.id - t.sheet.firstGId > 0) {
                    // create a rect
                    Rect r = new Rect(
                            x * tileWidth - tileWidth / 2 + (int) position.x,
                            y * tileHeight - tileHeight / 2 + (int) position.y,
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

    public void addRenderOnTop(String name) {
        TileMapLayer layer = map.getLayer(name);
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile t = layer.at(y, x);
                t.renderOnTop = true;
            }
        }
    }
}

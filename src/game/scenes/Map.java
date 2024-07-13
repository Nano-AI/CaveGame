package game.scenes;

import display.Window;
import game.entities.Empty;
import game.entities.Entity;
import utils.DirectionType;
import utils.tile.*;
import utils.math.*;

import java.awt.*;
import java.util.List;

public class Map {
    private TileMap map;
    private Vector2 tileScale, position;
    private List<Rect> hitboxes;

    public Map() {
        this(new Vector2(1, 1), new Vector2());
    }

    public Map(Vector2 position, Vector2 mapScale) {
        this.position = position;
        this.tileScale = mapScale;
    }

    public void setSheet(String sheet) {
        if (this.map != null) {
            this.map.setSheet(TileSheet.fromTiled(sheet));
        } else {
            throw new RuntimeException("TileMap not initialized");
        }
    }

    public void setMap(String map) {
        this.map = TileMap.fromTiled(map);
    }

    public void renderMap() {
        if (this.map == null) {
            throw new RuntimeException("TileMap not initialized");
        }
        if (this.map.getSheet() == null) {
            throw new RuntimeException("TileSheet not initialized");
        }

        TileMapLayer[] layers = this.map.getLayers();
        Graphics2D g = Window.getBuffer();
        TileSheet sheet = this.map.getSheet();

        int tileWidth = (int) (this.map.getTileWidth() * tileScale.x);
        int tileHeight = (int) (this.map.getTileHeight() * tileScale.y);

        for (TileMapLayer layer : layers) {
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    Tile t = layer.at(y, x);
                    if (t.id > 0) {
                        TileImage image = sheet.getImage(t.id - 1);
                        int xPos = (int) position.x + tileWidth * x;
                        int yPos = (int) position.y + tileHeight * y;

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

    public void addHitboxes(String layerName, List<Entity> entities) {
        int tileWidth = (int) (this.map.getTileWidth() * tileScale.x);
        int tileHeight = (int) (this.map.getTileHeight() * tileScale.y);
        TileMapLayer layer = map.getLayer(layerName);
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                if (layer.at(y, x).id > 0) {
                    Rect r = new Rect(
                            x * tileWidth - tileWidth / 2,
                            y * tileHeight - tileHeight / 2,
                            map.getTileWidth() * tileScale.x,
                            map.getTileHeight() * tileScale.y
                    );
                    Entity e = new Empty(r.position);
                    e.setHitbox(r);
                    entities.add(e);
                }
            }
        }
    }

    public int getWidth() {
        return this.map.getWidth() * this.map.getTileWidth();
    }

    public int getHeight() {
        return this.map.getHeight() * this.map.getTileHeight();
    }
}

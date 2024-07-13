package utils.tile;

public class TileMapLayer {
    public final String name;
    private boolean collidable = false;
    private int width, height;
    private Tile[][] layer;

    public TileMapLayer(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Tile[][] getLayer() {
        return layer;
    }

    public void setLayer(Tile[][] layer) {
        this.layer = layer;
    }

    public Tile at(int verticalIndex, int horizontalIndex) {
        return layer[verticalIndex][horizontalIndex];
    }

    public void setCollidable(boolean collide) {
        this.collidable = collide;
    }

    public boolean isCollidable() {
        return this.collidable;
    }
}

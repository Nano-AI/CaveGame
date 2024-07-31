package game.ui;

import display.Window;
import game.entities.Camera;
import game.entities.Player;
import game.entities.items.Item;
import utils.math.Vector2;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Inventory extends UI {
    private Player player;
    public Item[] inventory;
    public int inventorySize = 8;
    private int iterateIndex = 0;
    private Vector2 inventoryTileSize;
    private Vector2 inventoryStartDrawLocation;
    public Inventory(Camera c) {
        super(c);
    }

    public Inventory(Camera c, Player player, Vector2 inventoryTileSize) {
        super(c);
        this.player = player;
        this.inventory = new Item[inventorySize];
        this.inventoryTileSize = inventoryTileSize;
        int windowMiddleX = (int) ((Window.get().getWidth() / 2.0) - (inventorySize / 2.0) * inventoryTileSize.x);
        int windowMiddleY = (int) (Window.get().getHeight() - inventoryTileSize.y / 2.0);
        this.inventoryStartDrawLocation = new Vector2(
            windowMiddleX, windowMiddleY
        );
    }

    @Override
    public void render() {
        int xOffset = 0;
        for (Item item : inventory) {
            Window.getBuffer().drawRect(
                    (int) (inventoryStartDrawLocation.x + xOffset),
                    (int) inventoryStartDrawLocation.y,
                    (int) inventoryTileSize.x,
                    (int) inventoryTileSize.y
            );
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    public void set(int index, Item item) {
        this.inventory[index] = item;
    }

    public Item get(int index) {
        return this.inventory[index];
    }

    public int getInventorySize() {
        return this.inventorySize;
    }
}

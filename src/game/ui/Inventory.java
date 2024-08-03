package game.ui;

import display.Window;
import game.entities.Camera;
import game.entities.Player;
import game.entities.items.Food;
import game.entities.items.Item;
import input.KeyInput;
import utils.math.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
    private BasicStroke stroke = new BasicStroke(3);
    private int inventoryIndex = 0;
    public boolean inventoryFull = false;

    public Inventory(Camera c) {
        super(c);
    }

    public Inventory(Camera c, Player player, Vector2 inventoryTileSize) {
        super(c);
        this.player = player;
        this.inventory = new Item[inventorySize];
        this.inventoryTileSize = inventoryTileSize;
        int windowMiddleX = (int) ((Window.get().getWidth() / 2.0) - (inventorySize) * inventoryTileSize.x);
        int windowMiddleY = (int) (Window.get().getHeight() - inventoryTileSize.y / 2.0);
        this.inventoryStartDrawLocation = new Vector2(
            windowMiddleX, windowMiddleY
        );
    }

    @Override
    public void render() {
        int index = 0;
        double xOffset = 0;
        Stroke baseStroke = Window.getBuffer().getStroke();
        Window.getBuffer().setStroke(stroke);
        double margin = 0.1;
        for (Item item : inventory) {
            index++;
//            item.getImage();
            Window.getBuffer().setColor(Color.DARK_GRAY);
            Window.getBuffer().fillRect(
                (int) camera.getPosition().plus(inventoryStartDrawLocation.x + xOffset, 0).x,
                (int) camera.getPosition().plus(inventoryStartDrawLocation.y, 0).y,
                (int) inventoryTileSize.x,
                (int) inventoryTileSize.y
            );

            Window.getBuffer().setColor(Color.BLACK);
            Window.getBuffer().drawRect(
                (int) camera.getPosition().plus(inventoryStartDrawLocation.x + xOffset, 0).x,
                (int) camera.getPosition().plus(inventoryStartDrawLocation.y, 0).y,
                (int) inventoryTileSize.x,
                (int) inventoryTileSize.y
            );

            if (item != null) {
                Window.getBuffer().drawImage(
                        item.getImage(),
                        (int) camera.getPosition().plus(inventoryStartDrawLocation.x + xOffset, 0).x,
                        (int) camera.getPosition().plus(inventoryStartDrawLocation.y, 0).y,
                        (int) inventoryTileSize.x,
                        (int) inventoryTileSize.y,
                        null
                );
            }
            xOffset += inventoryTileSize.x * (1 + margin);
        }
        Window.getBuffer().setColor(Color.WHITE);
        Window.getBuffer().drawRect(
                (int) camera.getPosition().plus(inventoryStartDrawLocation.x + inventoryTileSize.x * inventoryIndex * (1 + margin), 0).x,
                (int) camera.getPosition().plus(inventoryStartDrawLocation.y, 0).y,
                (int) inventoryTileSize.x,
                (int) inventoryTileSize.y
        );
        Window.getBuffer().setStroke(baseStroke);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        if (KeyInput.isPressed(KeyEvent.VK_1)) {
            inventoryIndex = 0;
        } else if (KeyInput.isPressed(KeyEvent.VK_2)) {
            inventoryIndex = 1;
        } else if (KeyInput.isPressed(KeyEvent.VK_3)) {
            inventoryIndex = 2;
        } else if (KeyInput.isPressed(KeyEvent.VK_4)) {
            inventoryIndex = 3;
        }
        inventoryFull = isInventoryFull();
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

    public void addToInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                break;
            }
        }
    }

    private boolean isInventoryFull() {
        for (Item item : inventory) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    public Item getEquippedItem() {
        return this.inventory[inventoryIndex];
    }

    public void removeEquippedItem() {
        this.inventory[inventoryIndex] = null;
    }
}

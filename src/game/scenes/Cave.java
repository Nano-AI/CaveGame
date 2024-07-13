package game.scenes;

import display.Window;
import game.entities.Camera;
import game.entities.Entity;
import game.entities.Player;
import utils.math.Rect;
import utils.resources.Assets;
import utils.tile.TileSheet;
import utils.math.Vector2;

public class Cave extends Scene {
    public Player player;
    public Map map;

    @Override
    public void init() {
        TileSheet playerSheet = Assets.getTileSheet("assets/tiles/tilemap_packed.png", 16, 16);
        map = new Map(new Vector2(0, 0), new Vector2(2, 2));

        map.setMap("assets/tiles/sampleMap.tmx");
        map.setSheet("assets/tiles/sampleSheet.tsx");
        map.addHitboxes("Collideable", entities);

        player = new Player(new Vector2(200f, 400f), new Vector2(2, 2), playerSheet);
        player.init();
        player.setFrameIndex(96);
        player.setScene(this);

        double widthToHeight = (double) Window.get().getHeight() / Window.get().getWidth();
        camera = new Camera(new Vector2(16, 16),
                new Vector2(500, 500 * widthToHeight),
                new Vector2(map.getWidth() + 10, map.getHeight() + 32));

        camera.init();
        camera.setFocus(player);

        entities.add(player);
    }

    @Override
    public void update() {
        camera.update();
        player.update();
    }

    @Override
    public void render() {
        camera.render();
        map.renderMap();
        player.render();
    }
}

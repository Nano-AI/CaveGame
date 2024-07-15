/**
 * Cave scene
 */
package game.scenes;

import game.ui.FPSLabel;
import display.Window;
import game.entities.*;
import utils.math.Rect;
import utils.math.Vector2;

import java.util.Collections;
import java.util.List;

public class Cave extends Scene {
    // map
    public Map map;
    public FPSLabel fpsLabel;
    @Override
    public void init() {
        // create a map
        map = new Map(new Vector2(-14, -4), new Vector2(4, 4));

        // setup the tilemap based off of Tiled app
        map.setMap("assets/tiles/midMap.tmx");
        // set it so the walls layer is used for collisions
        map.addHitboxes("Walls3", entities);
        map.addHitboxes("Walls2", entities);
        map.addHitboxes("Walls", entities);
        map.addHitboxes("Props", entities);
        map.addHitboxes("Props2", entities);
        map.addHitboxes("Props3", entities);
        map.addRenderOnTop("Roof");

        // init the player @    pos                            scale
        player = new Player(new Vector2(200f, 64f), new Vector2(4, 4));
        // init player
        player.init();
        // set the scene
        player.setScene(this);

        for (int x = 1000; x < 1400; x += (int) (Math.random() * 64) + 100) {
            for (int y = 420; y < 640; y += (int) (Math.random() * 64) + 100) {
                Entity c = new Goblin(new Vector2(x, y), new Vector2(4, 4));
                c.init();
                c.setScene(this);
                entities.add(c);
            }
        }
        for (int x = 110; x < 440; x += (int) (Math.random() * 64) + 100) {
            for (int y = 420; y < 530; y += (int) (Math.random() * 64) + 100) {
                Entity c = new Goblin(new Vector2(x, y), new Vector2(4, 4));
                c.init();
                c.setScene(this);
                entities.add(c);
            }
        }

        // setup camera & make sure ratio matches with the window
        double widthToHeight = (double) Window.get().getHeight() / Window.get().getWidth();
        // setup the camera
        camera = new Camera(new Vector2(16, 16),
                new Vector2(750, 750 * widthToHeight),
                new Vector2(map.getWidth() - 16, map.getHeight() + 16));

        fpsLabel = new FPSLabel(camera);

        // init camera
        camera.init();
        // focus on player
        camera.setFocus(player);
        // add player to entities
        entities.add(player);
        map.camera = camera;
        map.setupImages();
    }

    @Override
    public void update() {
        // update camera
        camera.update();
        // sort by y-axis
        Collections.sort(entities);
        // update entities
        int i = 0;
        while (i < entities.size()) {
            Entity e = entities.get(i);
            e.update();
            if (e.shouldRemove()) {
                entities.remove(i);
                i--;
            }
            i++;
        }
        fpsLabel.update();
    }

    @Override
    public void render() {
        // render tile map
        map.renderMap(false);
        // render camera
        camera.render();
        // iterate through entities
        for (Entity e : entities) {
            e.render(); // render entity
//            if (e instanceof Empty) {
//                Window.getBuffer().drawRect(
//                        (int) e.getPosition().x,
//                        (int) e.getPosition().y,
//                        32, 32
//                );
//            }
        }
        map.renderMap(true);
        fpsLabel.render();
    }
}

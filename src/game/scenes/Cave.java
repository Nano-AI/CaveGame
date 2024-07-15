/**
 * Cave scene
 */
package game.scenes;

import display.Window;
import game.entities.*;
import utils.math.Vector2;

import java.util.Collections;

public class Cave extends Scene {
    // map
    public Map map;
    public Goblin enemy;
    @Override
    public void init() {
        // create a map
        map = new Map(new Vector2(0, 0), new Vector2(4, 4));

        // setup the tilemap based off of Tiled app
        map.setMap("assets/tiles/untitled.tmx");
        // set it so the walls layer is used for collisions
        map.addHitboxes("Walls", entities);
        map.addHitboxes("CProps", entities);
        map.addHitboxes("Cprops2", entities);

        // init the player @    pos                            scale
        player = new Player(new Vector2(200f, 64f), new Vector2(4, 4));
        // init player
        player.init();
        // set the scene
        player.setScene(this);

//        enemy = new Goblin(new Vector2(200f, 500f), new Vector2(2, 2), playerSheet);
//        enemy.init();
//        enemy.setFrameIndex(109);
//        enemy.setScene(this);

        // setup camera & make sure ratio matches with the window
        double widthToHeight = (double) Window.get().getHeight() / Window.get().getWidth();
        // setup the camera
        camera = new Camera(new Vector2(16, 16),
                new Vector2(500, 500 * widthToHeight),
                new Vector2(map.getWidth() - 16, map.getHeight() + 16));

        // init camera
        camera.init();
        // focus on player
        camera.setFocus(player);
        // add player to entities
        entities.add(player);
    }

    @Override
    public void update() {
        // update camera
        camera.update();
        // sort by y-axis
        Collections.sort(entities);
        // update entities
        for (Entity e : entities) {
            e.update();
        }
    }

    @Override
    public void render() {
        // render tile map
        map.renderMap();
        // render camera
        camera.render();
        // iterate through entities
        for (Entity e : entities) {
            e.render(); // render entity
            if (e instanceof Empty) {
                Window.getBuffer().drawRect(
                        (int) e.getPosition().x,
                        (int) e.getPosition().y,
                        32, 32
                );
            }
        }
    }
}

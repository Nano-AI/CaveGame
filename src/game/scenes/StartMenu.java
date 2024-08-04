package game.scenes;

import display.Window;
import game.entities.Camera;
import game.entities.Empty;
import game.entities.Entity;
import game.entities.Player;
import game.ui.Button;
import game.ui.UI;
import utils.Time;
import utils.math.Rect;
import utils.math.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class StartMenu extends Scene {

    private Camera cam;
    private ArrayList<UI> elements = new ArrayList<>();
    private Map backgroundMap;
    private Entity followEntity = new Empty(new Vector2(0, 0), new Vector2(1, 1));

    @Override
    public void init() {
        followEntity.setScale(new Vector2(1, 1));
        followEntity.setHitbox(new Rect(0, 0, 0, 0));
        followEntity.setScene(this);

        backgroundMap = new Map(new Vector2(-14, 4), new Vector2(4, 4));
        backgroundMap.setMap("assets/tiles/midMap.tmx");
        double widthToHeight = (double) Window.get().getHeight() / Window.get().getWidth();
        // setup the camera
        camera = new Camera(new Vector2(16, 16),
                new Vector2(750, 750 * widthToHeight),
                new Vector2(backgroundMap.getWidth() - 16, backgroundMap.getHeight() + 16));

        Button startButton = new Button("Start", new Vector2(50, 100), new Rect(0, 0, 100, 50));
        startButton.onClick = () -> {
            changeScene();
        };
        elements.add(startButton);
// Create the Restart button
        Button restartButton = new Button("Restart", new Vector2(50, 160), new Rect(0, 0, 100, 50));
        restartButton.onClick = () -> {
            restartGame();
        };
        elements.add(restartButton);
        camera.init();
        backgroundMap.camera = camera;
        backgroundMap.setupImages();
        camera.setFocus(followEntity);
    }

    @Override
    public void update() {
        camera.update();
        Vector2 moveBy = new Vector2(Time.deltaT() * 10, 0);
        for (UI element : elements) {
            element.update();
            element.move(moveBy.x, moveBy.y);
        }
        followEntity.movePositionForce(moveBy.x, moveBy.y);
        camera.movePositionForce(-1 * moveBy.x, moveBy.y);
    }

    @Override
    public void render() {
        backgroundMap.renderMap(false);
        camera.render();
        for (UI element : elements) {
            element.render();
        }
    }

    private void changeScene() {
        Window.getBuffer().setTransform(new AffineTransform());
        Cave c = new Cave();
        c.init();
        Window.get().scene = c;
    }

    private void restartGame() {

    }
}

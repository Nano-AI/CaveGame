package game.ui;

import display.Window;
import game.entities.Camera;
import utils.Time;
import utils.Timer;

public class FPSLabel extends UI {
    private final Timer updateTimer = new Timer(0.2);
    private int fps = 0;

    public FPSLabel(Camera cam) {
        super(cam);
    }

    @Override
    public void render() {
        Window.getBuffer().drawString(String.format("" +
                "FPS: %d       Pos: (%d, %d)", fps,
                (int) camera.getFocus().getPosition().x, (int) camera.getFocus().getPosition().y),
                (int) (camera.getPosition().x), (int) (camera.getPosition().y + 10));
        if (updateTimer.isDone()) {
            fps = (int) (1 / Time.deltaT());
            updateTimer.restart();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        updateTimer.update();
    }
}

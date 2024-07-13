package display;

import game.scenes.Scene;
import input.KeyInput;
import utils.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {
    private int width, height;
    private String title;
    private boolean running = false;
    private KeyInput keyboardInput;

    public Scene scene;
    public Color clearColor = Color.WHITE;

    private BufferedImage bufferImage;
    private Graphics2D buffer;

    private static Window ref;

    public static Window get() {
        if (ref == null) {
            ref = new Window();
        }
        return ref;
    }

    public void init(String title, int width, int height) {
        keyboardInput = new KeyInput();

        this.title = title;
        this.width = width;
        this.height = height;
        ref = this;

        bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        buffer = (Graphics2D) bufferImage.getGraphics();
        buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        addKeyListener(keyboardInput);

        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void loop() {
        running = true;
        Graphics2D g = (Graphics2D) getGraphics();
        scene.init();
        while (running) {
            Time.updateTime();
            buffer.clearRect(0, 0, width, height);
            if (scene != null) {
                scene.render();
                g.drawImage(bufferImage, 0, 0, null);
                if (Time.deltaT() > 0) {
                    scene.update();
                }
            }
        }
    }

    public static Graphics2D getBuffer() {
        return get().buffer;
    }
}

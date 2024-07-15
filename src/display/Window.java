/**
 * Window class which handles everything JFrame related
 */
package display;

import game.scenes.Scene;
import input.KeyInput;
import input.MouseInput;
import utils.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {
    // width + height of frame
    private int width, height;
    // title of window
    private String title;
    // running
    private boolean running = false;
    // keyboard + mouse input
    private KeyInput keyboardInput;
    private MouseInput mouseInput;

    // current scene being rendered
    public Scene scene;

    // buffer image
    private BufferedImage bufferImage;
    // buffer graphics
    private Graphics2D buffer;

    // current window ref
    private static Window ref;

    /**
     * Get window reference
     * @return Current window
     */
    public static Window get() {
        if (ref == null) {
            ref = new Window();
        }
        return ref;
    }

    /**
     * Init the window
     * @param title Title of the window
     * @param width Width of the window
     * @param height Height of the window
     */
    public void init(String title, int width, int height) {
        // setup inputs
        keyboardInput = new KeyInput();
        mouseInput = new MouseInput();

        // init class variables
        this.title = title;
        this.width = width;
        this.height = height;
        ref = this;

        // create a buffer image
        bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // get the graphics
        buffer = (Graphics2D) bufferImage.getGraphics();
        // disable anti aliasing
        buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // add listeners
        addKeyListener(keyboardInput);
        addMouseListener(mouseInput);

        // jframe flags
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Game loop
     */
    public void loop() {
        running = true;
        Graphics2D g = (Graphics2D) getGraphics();
        // init scene before loop
        scene.init();
        while (running) {
            // update deltaT
            Time.updateTime();
            // clear window
            buffer.clearRect(0, 0, width, height);
            // render scene, draw buffer image, and update everything
            if (scene != null) {
                scene.render();
                g.drawImage(bufferImage, 0, 0, null);
                if (Time.deltaT() > 0) {
                    scene.update();
                }
            }
        }
    }

    /**
     * Get the buffer graphics
     * @return Buffer graphics
     */
    public static Graphics2D getBuffer() {
        return get().buffer;
    }
}

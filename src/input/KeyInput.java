/**
 * Keyboard input listener
 */
package input;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class KeyInput implements KeyListener {
    // set of pressed keys as hashset for quick read, write, and insertion
    private static Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // add the keycode
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // remove the key code
        pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Returns whether the ky is pressed
     * @param keyCode Key code that has been pressed
     * @return If key has been pressed
     */
    public static boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}

package input;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class KeyInput implements KeyListener {
    private static Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public static boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}

/**
 * Mouse button input
 */
package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class MouseInput implements MouseListener {
    // Mouse pressed buttons
    private static Set<Integer> pressedButtons = new HashSet<>();

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // add if mouse has been pressed
        pressedButtons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // remove if mouse has been removed
        pressedButtons.remove(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Checks if button has been pressed
     * @param button Button that's been pressed
     * @return If button has been pressed
     */
    public static boolean isPressed(int button) {
        return pressedButtons.contains(button);
    }
}

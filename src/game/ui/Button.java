package game.ui;

import display.Window;
import utils.math.Rect;
import utils.math.Vector2;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Button extends UI {
    private boolean pressed = false;
    private boolean hovered = false;
    private Rect borders;
    private String text;
    private Color idleColor = Color.GRAY;
    private Color hoverColor = Color.LIGHT_GRAY;
    private Color pressColor = Color.DARK_GRAY;

    // Using a Runnable for onClick to allow setting it externally
    public Runnable onClick = () -> {};

    public Button(String text, Vector2 pos, Rect borders) {
        super(null);
        this.text = text;
        this.position = pos;
        this.borders = borders;

        // Add a mouse listener to detect clicks
        Window.get().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point transformedPoint = getTransformedMousePosition(e);
                if (isInside(transformedPoint.x, transformedPoint.y)) {
                    pressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point transformedPoint = getTransformedMousePosition(e);
                if (pressed && isInside(transformedPoint.x, transformedPoint.y)) {
                    onClick.run();
                }
                pressed = false;
            }
        });

        // Add a mouse motion listener to detect hover
        Window.get().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point transformedPoint = getTransformedMousePosition(e);
                hovered = isInside(transformedPoint.x, transformedPoint.y);
            }
        });
    }

    private Point getTransformedMousePosition(MouseEvent e) {
        // Get the inverse of the current transform to map mouse coordinates back to original
        AffineTransform transform = ((Graphics2D) Window.getBuffer()).getTransform();
        try {
            AffineTransform inverse = transform.createInverse();
            Point2D originalPoint = inverse.transform(new Point2D.Double(e.getX(), e.getY()), null);
            return new Point((int) originalPoint.getX(), (int) originalPoint.getY());
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
            return e.getPoint(); // Fallback to the untransformed point if error occurs
        }
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= position.x && mouseX <= position.x + borders.size.x &&
                mouseY >= position.y && mouseY <= position.y + borders.size.y;
    }

    @Override
    public void render() {
        Graphics2D g2d = Window.getBuffer();

        if (pressed) {
            g2d.setColor(pressColor);
        } else if (hovered) {
            g2d.setColor(hoverColor);
        } else {
            g2d.setColor(idleColor);
        }

        // Draw the button rectangle
        g2d.fillRect((int) position.x, (int) position.y, (int) borders.size.x, (int) borders.size.y);

        // Draw the button text
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        g2d.drawString(text,
                (int) (position.x + (borders.size.x - textWidth) / 2),
                (int) (position.y + (borders.size.y + textHeight) / 2) - fm.getDescent());
    }

    @Override
    public void init() {
        // Initialization code if needed
    }

    @Override
    public void update() {
        // Update code if needed
    }
}

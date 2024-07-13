package utils.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;

public class Image {
    public static BufferedImage loadImage(String fileName) {
        BufferedImage img;
        File imageFile = new File(fileName);
        try {
            img = ImageIO.read(imageFile);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Error reading file '%s'\n", fileName);
        }
        return null;
    }


    public static BufferedImage diagonallyFlip(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flipped.setRGB(y, x, image.getRGB(x, height - 1 - y));
            }
        }

        return flipped;
    }
}

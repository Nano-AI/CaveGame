/**
 * Image utility class
 */
package utils.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    /**
     * Load image frompath
     * @param fileName Path of image
     * @return Image
     */
    public static BufferedImage loadImage(String fileName) {
        // create buffered image
        BufferedImage img;
        // load file
        File imageFile = new File(fileName);
        try {
            // read image
            img = ImageIO.read(imageFile);
            // if it can't be read, throw an error
            if (img == null) {
                throw new RuntimeException("Unable to read file " + imageFile.getAbsolutePath());
            }
            // return image
            return img;
        } catch (IOException e) {
            // error trace
            e.printStackTrace();
            throw new RuntimeException(String.format("Error reading file '%s'\n", fileName));
        }
    }

    /**
     * Diagonally flip an image
     * @param image Image to be flipped
     * @return Flipped image
     */
    public static BufferedImage diagonallyFlip(BufferedImage image) {
        // iamge width + height
        int width = image.getWidth();
        int height = image.getHeight();
        // create flipped buffered image
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        // iterate throgh every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // set the color of pixel to the rotated one
                flipped.setRGB(y, x, image.getRGB(x, height - 1 - y));
            }
        }
        return flipped;
    }
}

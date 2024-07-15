/**
 * Assets class to ensure low memory and reuse resources
 */
package utils.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import utils.tile.TileMap;
import utils.tile.TileSheet;

import javax.sound.sampled.*;

public class Assets {
    // images hashmap
    private static Map<String, BufferedImage> images = new HashMap<>();
    // tilesheet hashmap
    private static Map<String, TileSheet> tilesheets = new HashMap<>();
    // xml hashmap
    private static Map<String, Document> xmls = new HashMap<>();
    // tilemap hashmap
    private static Map<String, TileMap> tilemaps = new HashMap<>();
    // clip hashmap
    private static Map<String, Clip> clips = new HashMap<>();
    // sound hashmap
    private static Map<String, Sound> sounds = new HashMap<>();

    /**
     * Load image if it hasn't, otherwise return loaded image
     * @param path Path of the image
     * @return Image
     */
    public static BufferedImage addImage(String path) {
        File f = new File(path);
        // check if images doesn't have the absolute
        if (!images.containsKey(f.getAbsolutePath())) {
            // load image
            BufferedImage img = Image.loadImage(path);
            // put it in images
            images.put(f.getAbsolutePath(), img);
            // return images
            return img;
        }
        // return the image
        return images.get(f.getAbsolutePath());
    }

    /**
     * Get an image based off of path
     * @param path Path of image
     * @return Image
     */
    public static BufferedImage getImage(String path) {
        // load file
        File file = new File(path);
        // check if it contains
        if (images.containsKey(file.getAbsolutePath())) {
            // return if it contains
            return images.get(file.getAbsolutePath());
        }
        // otherwise load it
        return addImage(path);
    }

    /**
     * Get an XML file
     * @param path Path of XML file
     * @return XML file
     */
    public static Document getXML(String path) {
        // create the document
        Document d;
        // get file path
        File f = new File(path);
        // if it doens't exist
        if (!xmls.containsKey(f.getAbsolutePath())) {
            // read the xml file
            d = XMLReader.readXML(f.getAbsolutePath());
            // put the xml file
            xmls.put(f.getAbsolutePath(), d);
        } else {
            // otherwise set it to exisiting xml file
            d = xmls.get(f.getAbsolutePath());
        }
        // return xml file
        return d;
    }

    /**
     * Get a tilesheet with spacing of 0
     * @param path String path of tilesheet
     * @param tileWidth Width of each tile, not width of image
     * @param tileHeight Height of each tile, not height of image
     * @return Corresponding tilesheet
     */
    public static TileSheet getTileSheet(String path, int tileWidth, int tileHeight) {
        return getTileSheet(path, tileWidth, tileHeight, 0);
    }

    /**
     * Get a tilesheet with spacing of 0
     * @param path String path of tilesheet
     * @param tileWidth Width of each tile, not width of image
     * @param tileHeight Height of each tile, not height of image
     * @param spacing Spacing between each image
     * @return Corresponding tilesheet
     */
    public static TileSheet getTileSheet(String path, int tileWidth, int tileHeight, int spacing) {
        // file of image
        File f = new File(path);
        // check if it's already been loaded, return it
        if (tilesheets.containsKey(f.getAbsolutePath())) {
            return tilesheets.get(f.getAbsolutePath());
        }
        // load the tilesheet
        TileSheet ts = new TileSheet(f.getAbsolutePath(), tileWidth, tileHeight, spacing);
        // put it
        tilesheets.put(f.getAbsolutePath(), ts);
        // return it
        return ts;
    }

    /**
     * Load tile map
     * @param path Path of tilemap
     * @return Corresponding tilemap
     */
    public static TileMap getTileMap(String path) {
        // get file
        File f = new File(path);
        // create tile map
        TileMap map;
        // if doesn't exist, create it and set it
        if (!tilemaps.containsKey(f.getAbsolutePath())) {
            map = TileMap.fromTiled(f.getAbsolutePath());
            tilemaps.put(f.getAbsolutePath(), map);
        } else {
            // return if it already exists
            map = tilemaps.get(f.getAbsolutePath());
        }
        // return map
        return map;
    }

    /**
     * Get audio clip
     * @param path Path of audio
     * @return Return audio file
     */
    public static Clip getClip(String path) {
        // get the file
        File f = new File(path);
        // create the clip
        Clip c = null;
        // if it doesn't exist
        if (!clips.containsKey(f.getAbsolutePath())) {
            try {
                // create audio in
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
                // get the clip
                c = AudioSystem.getClip();
                // open the clip using audio in
                c.open(audioIn);
                // add the clip
                clips.put(f.getAbsolutePath(), c);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        } else {
            // return loaded
            c = clips.get(f.getAbsolutePath());
        }
        return c;
    }

    /**
     * Get sound (custom class) compared to clip
     * @param path Path of sound
     * @return Loaded sound
     */
    public static Sound getSound(String path) {
        File f = new File(path);
        Sound s = null;
        if (!sounds.containsKey(f.getAbsolutePath())) {
            s = new Sound(path);
            sounds.put(f.getAbsolutePath(), s);
        } else {
            s = sounds.get(f.getAbsolutePath());
        }
        return s;
    }
}

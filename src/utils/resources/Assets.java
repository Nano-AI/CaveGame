package utils.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import utils.tile.TileSheet;

public class Assets {
    private static Map<String, BufferedImage> textures = new HashMap<>();
    private static Map<String, TileSheet> tilesheets = new HashMap<>();
    private static Map<String, Document> xmls = new HashMap<>();

    public static BufferedImage addTexture(String path) {
        if (!textures.containsKey(path)) {
            File file = new File(path);
            BufferedImage img = Image.loadImage(path);
            textures.put(file.getAbsolutePath(), img);
            return img;
        }
        return textures.get(path);
    }

    public static BufferedImage getTexture(String path) {
        File file = new File(path);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.getOrDefault(file.getAbsolutePath(), null);
        }
        return addTexture(path);
    }

    public static Document getXML(String path) {
        Document d;
        File f = new File(path);
        if (!xmls.containsKey(f.getAbsolutePath())) {
            d = XMLReader.readXML(f.getAbsolutePath());
            xmls.put(f.getAbsolutePath(), d);
        } else {
            d = xmls.get(f.getAbsolutePath());
        }
        return d;
    }

    public static TileSheet getTileSheet(String path, int tileWidth, int tileHeight) {
        return getTileSheet(path, tileWidth, tileHeight, 0);
    }

    public static TileSheet getTileSheet(String path, int tileWidth, int tileHeight, int spacing) {
        File f = new File(path);
        if (tilesheets.containsKey(f.getAbsolutePath())) {
            return tilesheets.get(f.getAbsolutePath());
        }
        TileSheet ts = new TileSheet(f.getAbsolutePath(), tileWidth, tileHeight, spacing);
        tilesheets.put(f.getAbsolutePath(), ts);
        return ts;
    }
}

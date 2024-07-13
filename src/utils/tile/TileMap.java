package utils.tile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.resources.Assets;

import java.io.File;
import java.util.Collections;

public class TileMap {
    // TODO: Rework to account for multiple tile sets
    private TileMapLayer[] layers;
    private int width, height, tileWidth, tileHeight;
    private TileSheet sheet;

    private static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000; // 2147483648
    private static final int FLIPPED_VERTICALLY_FLAG = 0x40000000;   // 1073741824
    private static final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;    // 536870912
    private static final int FLIP_FLAGS_MASK = FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG;

    public TileMap() {

    }

    public void setSheet(TileSheet sheet) {
        this.sheet = sheet;
    }

    public static TileMap fromTiled(String source) {
        File file = new File(source);

        Document doc = Assets.getXML(source);
        Element root = doc.getDocumentElement();

        if (!root.getNodeName().equals("map")) {
            throw new RuntimeException(String.format("'%s' is not a map", root.getNodeName()));
        }

        int width = Integer.parseInt(root.getAttribute("width"));
        int height = Integer.parseInt(root.getAttribute("height"));
        int tileWidth = Integer.parseInt(root.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(root.getAttribute("tileheight"));

        Element tileset = (Element) doc.getElementsByTagName("tileset").item(0);
        String tilesetSource = file.getParent() + "/" + tileset.getAttribute("source");

        TileSheet sheet = TileSheet.fromTiled(tilesetSource);

        NodeList layers = doc.getElementsByTagName("layer");
        int layerCount = layers.getLength();

        TileMap out = new TileMap();
        out.layers = new TileMapLayer[layerCount];
        out.width = width;
        out.height = height;
        out.tileWidth = tileWidth;
        out.tileHeight = tileHeight;
        out.sheet = sheet;

        for (int i = 0; i < layerCount; i++) {
            Element layer = (Element) layers.item(i);
            int layerHeight = Integer.parseInt(layer.getAttribute("height"));
            int layerWidth = Integer.parseInt(layer.getAttribute("width"));
            String layerName = layer.getAttribute("name");

            Element data = (Element) layer.getElementsByTagName("data").item(0);
            String encoding = data.getAttribute("encoding");

            if (!encoding.equals("csv")) {
                throw new RuntimeException(String.format("'%s' has unknown encoding '%s'", data.getNodeName(), encoding));
            }

            String csv = data.getTextContent().trim();

            TileMapLayer newLayer = new TileMapLayer(layerName, layerWidth, layerHeight);
            newLayer.setLayer(parseCSVToIntArray(csv, layerHeight, layerWidth, sheet.getTileSize(), sheet));
            out.layers[i] = newLayer;
        }

        return out;
    }

    private static Tile[][] parseCSVToIntArray(String csv, int rows, int cols, int maxSize, TileSheet sheet) {
        String[] lines = csv.split("\\n");
        Tile[][] out = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            String[] stringValues = lines[i].split(",");
            for (int j = 0; j < cols; j++) {
                long value = Long.parseLong(stringValues[j].trim());
                if (value < 0 || value > maxSize) {
                    out[i][j] = new Tile(
                            (int) (value & ~FLIP_FLAGS_MASK),
                            (value & FLIPPED_HORIZONTALLY_FLAG) != 0,
                            (value & FLIPPED_VERTICALLY_FLAG) != 0,
                            (value & FLIPPED_DIAGONALLY_FLAG) != 0
                    );
                } else {
                    out[i][j] = new Tile((int) value);
                }
            }
        }
        return out;
    }

    public TileMapLayer getLayer(String name) {
        for (TileMapLayer layer : layers) {
            if (layer.name.equals(name)) {
                return layer;
            }
        }
        return null;
    }

    public TileMapLayer[] getLayers() {
        return layers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public TileSheet getSheet() {
        return sheet;
    }
}

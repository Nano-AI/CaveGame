/**
 * Load tile map from Tiled
 */
package utils.tile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.resources.Assets;

import java.io.File;
import java.util.*;

public class TileMap {
    // TODO: Rework to account for multiple tile sets
    // layers
    private TileMapLayer[] layers;
    // width, height, tilewidth, tileheight
    private int width, height, tileWidth, tileHeight;
    // tilesheets
    private Map<String, TileSheet> sheets;

    // flip flags to check
    private static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000; // 2147483648
    private static final int FLIPPED_VERTICALLY_FLAG = 0x40000000;   // 1073741824
    private static final int FLIPPED_DIAGONALLY_FLAG = 0x20000000;    // 536870912
    private static final int FLIP_FLAGS_MASK = FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG;

    public TileMap() {

    }

    /**
     * Load from tiled file
     * @param source Path of source file
     * @return Return tilemap
     */
    public static TileMap fromTiled(String source) {
        File file = new File(source);

        // load the XML file
        Document doc = Assets.getXML(source);
        Element root = doc.getDocumentElement();

        // make sure it's a map
        if (!root.getNodeName().equals("map")) {
            throw new RuntimeException(String.format("'%s' is not a map", root.getNodeName()));
        }

        // get params
        int width = Integer.parseInt(root.getAttribute("width"));
        int height = Integer.parseInt(root.getAttribute("height"));
        int tileWidth = Integer.parseInt(root.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(root.getAttribute("tileheight"));

        // get layers
        NodeList layers = doc.getElementsByTagName("layer");
        // get # of layers
        int layerCount = layers.getLength();

        // create new tilemap
        TileMap out = new TileMap();
        // setup layers, width, ehight, tilewidth, tileheight, and sheets
        out.layers = new TileMapLayer[layerCount];
        out.width = width;
        out.height = height;
        out.tileWidth = tileWidth;
        out.tileHeight = tileHeight;
        out.sheets = new HashMap<>();

        // load the tilesets
        NodeList tiles = doc.getElementsByTagName("tileset");
        // store the previous sheet
        TileSheet prevSheet = null;
        // current sheet
        TileSheet sheet = null;
        // iterate through every sheet
        for (int i = 0; i < tiles.getLength(); i++) {
            // get the sheet
            Element e = (Element) tiles.item(i);
            // store the source
            String tilesetSource = file.getParent() + "/" + e.getAttribute("source");
            // Load the tilesheet
//            sheet = Assets.getTileSheet(tilesetSource, tileWidth, tileHeight);
            sheet = TileSheet.fromTiled(tilesetSource);
            // store the firstGId
            sheet.firstGId = Integer.parseInt(e.getAttribute("firstgid"));
            // add the sheet
            out.addSheet(tilesetSource, sheet);
            // add the lastGId to the prev one
            if (prevSheet != null) {
                prevSheet.lastGId = sheet.firstGId - 1;
            }
            prevSheet = sheet;
        }
        // if we're on the last one, just set the last g id to the greatest value possibler
        if (sheet != null) {
            sheet.lastGId = Integer.MAX_VALUE;
        }

        // iterate through every layer
        for (int i = 0; i < layerCount; i++) {
            // get the layer
            Element layer = (Element) layers.item(i);
            // get the layer height and layer width
            int layerHeight = Integer.parseInt(layer.getAttribute("height"));
            int layerWidth = Integer.parseInt(layer.getAttribute("width"));
            // layer name
            String layerName = layer.getAttribute("name");
            // get the data (csv) split
            Element data = (Element) layer.getElementsByTagName("data").item(0);
            // get the encoding
            String encoding = data.getAttribute("encoding");
            // right now only setup for csv, so if it's not csv throw an error
            if (!encoding.equals("csv")) {
                throw new RuntimeException(String.format("'%s' has unknown encoding '%s'", data.getNodeName(), encoding));
            }
            // get the csv file
            String csv = data.getTextContent().trim();
            // create a new layer
            TileMapLayer newLayer = new TileMapLayer(layerName, layerWidth, layerHeight);
            // parse the CSV and set the layer
            newLayer.setLayer(parseCSVToIntArray(csv, layerHeight, layerWidth, out.getSheets()));
            // add the layer
            out.layers[i] = newLayer;
        }
        // return the new TileMap
        return out;
    }

    /**
     * Read a CSV file
     * @param csv CSV to read
     * @param rows Rows in tilemap
     * @param cols Cols in tilemap
     * @param sheets Map of the sheets
     * @return
     */
    private static Tile[][] parseCSVToIntArray(String csv, int rows, int cols, Map<String, TileSheet> sheets) {
        // split by line
        String[] lines = csv.split("\\n");
        // array of tiles
        Tile[][] out = new Tile[rows][cols];
        // iterate through rows
        for (int i = 0; i < rows; i++) {
            // split to iterate through tiles
            String[] stringValues = lines[i].split(",");
            for (int j = 0; j < cols; j++) {
                // get the value
                long value = Long.parseLong(stringValues[j].trim());
                // check if it's flippedj
                boolean isFlipped = (value & FLIP_FLAGS_MASK) != 0;
                if (isFlipped) {
                    // if it's flipped then add it to the tile
                    boolean horizontalFlip = (value & FLIPPED_HORIZONTALLY_FLAG) != 0;
                    boolean verticalFlip = (value & FLIPPED_VERTICALLY_FLAG) != 0;
                    boolean diagonalFlip = (value & FLIPPED_DIAGONALLY_FLAG) != 0;
                    // get the id
                    value = (value & ~FLIPPED_HORIZONTALLY_FLAG);
                    // setup tile
                    out[i][j] = new Tile(
                            (int) value,
                            horizontalFlip, verticalFlip, diagonalFlip
                    );
                } else {
                    // setup tile
                    out[i][j] = new Tile((int) value);
                }
                // iterate through every sheetj
                for (Map.Entry<String, TileSheet> entry : sheets.entrySet()) {
                    // make sure that the id fits within the firstGid and lastGid
                    if (out[i][j].id >= entry.getValue().firstGId && out[i][j].id <= entry.getValue().lastGId) {
                        // setup sheet
                        out[i][j].sheet = entry.getValue();
                    }
                }
            }
        }
        return out;
    }

    /**
     * Get layer off name
     * @param name Name to search for
     * @return Layer with the name
     */
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

    public TileSheet getSheet(String name) {
        File f = new File(name);
        return sheets.getOrDefault(f.getAbsolutePath(), null);
    }

    public Map<String, TileSheet> getSheets() {
        return this.sheets;
    }

    public boolean containsSheet(String name) {
        File f = new File(name);
        return sheets.containsKey(f.getAbsolutePath());
    }

    public void addSheet(String name, TileSheet sheet) {
        File f = new File(name);
        this.sheets.put(f.getAbsolutePath(), sheet);
    }
}

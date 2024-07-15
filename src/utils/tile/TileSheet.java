/**
 * TileSheet class
 */
package utils.tile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.resources.Assets;
import utils.resources.Images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class TileSheet implements Images {
    // get image
    private BufferedImage image;
    // tile images based off id
    private ArrayList<TileImage> imageList;
    // width, height, padding
    private int tileWidth, tileHeight, padding;
    // last + first id
    public int firstGId = 0;
    public int lastGId = 0;
    // sheet width + sheet height
    private int sheetWidth;
    private int sheetHeight;

    public TileSheet(String path, int tileWidth, int tileHeight) {
        imageList = new ArrayList<>();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.image = Assets.getImage(path);
        init();
    }

    public TileSheet(String path, int tileWidth, int tileHeight, int padding) {
        imageList = new ArrayList<>();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.padding = padding;
        this.image = Assets.getImage(path);
        init();
    }

    public TileSheet(BufferedImage image, int tileWidth, int tileHeight, int padding) {
        imageList = new ArrayList<>();
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.image = image;
        this.padding = padding;

        init();
    }

    private void init() {
        // get number of tiles in x and y
        int numX = image.getWidth() / tileWidth;
        int numY = image.getHeight() / tileHeight;

        // subImage to get each tile
        for (int y = 0; y < numY; y++) {
            for (int x = 0; x < numX; x++) {
                imageList.add(
                        new TileImage(
                                image.getSubimage(
                                        x * (tileWidth + padding),
                                        y * (tileHeight + padding),
                                        tileWidth,
                                        tileHeight
                                )
                        )
                );
            }
        }
        this.sheetWidth = numX;
        this.sheetHeight = numY;
    }

    /**
     * Unused; Do not use
     * @param sheetSource Unused
     * @return Unused
     */
    public static TileSheet fromTiled(String sheetSource) {
        Document info = Assets.getXML(sheetSource);
        Element root = info.getDocumentElement();

        if (!root.getNodeName().equals("tileset")) {
            throw new RuntimeException(String.format("'%s' is not a tileset", sheetSource));
        }

        int tileWidth = Integer.parseInt(root.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(root.getAttribute("tileheight"));
        int spacing = 0;
        if (!root.getAttribute("spacing").isEmpty()) {
            spacing = Integer.parseInt(root.getAttribute("spacing"));
        }

        NodeList imageList = info.getElementsByTagName("image");

        if (imageList.getLength() > 0) {
            Element imageElement = (Element) imageList.item(0);

            String source = imageElement.getAttribute("source");
            File file = new File(sheetSource);
            File parent = new File(file.getParentFile().getAbsolutePath(), source);

            return Assets.getTileSheet(parent.getAbsolutePath(), tileWidth, tileHeight, spacing);
        } else {
            throw new RuntimeException(String.format("'%s' does not have an image", sheetSource));
        }
    }

    @Override
    public TileImage getImage(int index) {
        return imageList.get(index - firstGId + 1);
    }

    public int getTileSize() {
        return imageList.size();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getSheetWidth() {
        return sheetWidth;
    }

    public int getSheetHeight() {
        return sheetHeight;
    }

    public int getNumTiles() {
        return sheetWidth * sheetHeight;
    }
}

package cz.wa.secretagent.io.map.orig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.wautils.math.Vector2I;

/**
 * Parses map in format .sam produced by Camoto Studio.
 * By default the map size is 40x24 but other size can be defined (it is not determined automatically).
 * If the first line contains string 'size', then the next two separate numbers
 * are taken as size x and y (excluding the last character on line).
 * 
 * No tile mapping is performed, the output must be mapped and processed.
 * 
 * @author Ondrej Milenovsky
 */
public class MapParser {

    private static final Logger logger = LoggerFactory.getLogger(MapParser.class);

    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 24;

    private static final String SIZE_START = "size";

    private final File input;
    private int[][] tiles;
    private int[][] items;
    private int currLine;

    public MapParser(File input) {
        this.input = input;
    }

    public MapLevel parse() throws IOException {
        List<Byte[]> lines = splitToLines(FileUtils.readFileToByteArray(input));

        // level size
        int sizeX = SIZE_X;
        int sizeY = SIZE_Y;
        Vector2I newSize = parseMapSize(lines.get(0));
        if (newSize != null) {
            sizeX = newSize.getX();
            sizeY = newSize.getY();
        }

        // background
        int bgTile = parseBgTile(lines.get(0));
        int bgTileOver = parseBgTileOver(lines.get(1));

        // offsets
        int[] offsets = parseOffsets(lines.get(1));

        //map init
        tiles = new int[sizeX][sizeY];
        items = new int[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                tiles[x][y] = 0x20;
                items[x][y] = 0x20;
            }
        }
        // map parsing
        currLine = -1;
        for (int i = 2; i < lines.size(); i++) {
            processLine(lines.get(i));
        }

        return new MapLevel(new Vector2I(sizeX, sizeY), bgTile, bgTileOver, tiles, items, offsets);
    }

    private Vector2I parseMapSize(Byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            Byte b = bytes[i];
            if (b == null) {
                chars[i] = 0;
            } else {
                chars[i] = (char) ((byte) b);
            }
        }
        String line = new String(chars);
        int ind = line.indexOf(SIZE_START);
        if (ind >= 0) {
            int sizeX = -1;
            int sizeY = -1;
            line = line.substring(ind, line.length() - 2);
            for (String str : line.split("[^0-9]+")) {
                if (!str.isEmpty()) {
                    int a = Integer.parseInt(str);
                    if (a <= 4) {
                        logger.warn("Size must be at least 4x4");
                        return null;
                    }
                    if (sizeX < 0) {
                        sizeX = a;
                    } else {
                        sizeY = a;
                        return new Vector2I(sizeX, sizeY);
                    }
                }
            }
        }
        return null;
    }

    private int[] parseOffsets(Byte[] bytes) {
        int[] offsets = new int[37];
        for (int i = 0; i < 37; i++) {
            offsets[i] = bytes[i + 3];
        }
        return offsets;
    }

    private int parseBgTileOver(Byte[] bytes) {
        return bytes[1];
    }

    private void processLine(Byte[] bytes) {
        if (bytes[0] == null) {
            return;
        }

        int[][] array;
        if (bytes[0] == 0x2a) {
            // line stars with *, overlay line
            array = items;
        } else {
            // normal line
            array = tiles;
            currLine++;
            if (currLine >= SIZE_Y) {
                return;
            }
        }

        // process the line
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i];
            if (v < 0) {
                v += 256;
            }
            array[i][currLine] = v;
        }
    }

    private int parseBgTile(Byte[] bytes) {
        int bgTile = 0;
        for (int i = 0; i < 8; i++) {
            if (Character.isDigit(bytes[i])) {
                bgTile *= 10;
                bgTile += bytes[i] - 48;
            } else {
                break;
            }
        }
        return bgTile;
    }

    private List<Byte[]> splitToLines(byte[] bytes) {
        List<Byte[]> lines = new ArrayList<Byte[]>(SIZE_Y);
        lines.add(new Byte[SIZE_X]);
        Byte[] lastLine = lines.get(0);
        int off = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (((bytes[i] == 0x0d) && (bytes[i + 1] == 0x0a)) || (off >= lastLine.length)) {
                lastLine = new Byte[SIZE_X];
                lines.add(lastLine);
                i++;
                off = 0;
            } else {
                lastLine[off] = bytes[i];
                off++;
            }
        }
        return lines;
    }

}

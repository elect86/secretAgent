package cz.wa.secretagent.view;

import org.apache.commons.lang.Validate;

import cz.wa.wautils.string.StringUtilsWa;

/**
 * Tile set id and tile id. 
 * 
 * @author Ondrej Milenovsky
 */
public class TileId {
    private final int tileSetId;
    private final int tileId;

    /**
     * Parses the tile id from string. Format is [tileSetId]*[tileId], white spaces are removed, the separator can be '.'
     * @param str the input string
     * @throws IllegalArgumentException if the input string is in wrong format
     * @throws NumberFormatException if any of the ids is not integer 
     */
    public TileId(String str) {
        str = StringUtilsWa.noSpaces(str);
        String[] split = str.split("[\\*\\.]");
        Validate.isTrue(split.length == 2, "The tile id must have 2 numbers separated by * or .: " + str);
        tileSetId = Integer.parseInt(split[0]);
        tileId = Integer.parseInt(split[1]);
        Validate.isTrue(tileId >= 0, "tileId must be >= 0: " + tileId);
    }

    public TileId(int tileSetId, int tileId) {
        Validate.isTrue(tileId >= 0, "tileId must be >= 0: " + tileId);
        this.tileSetId = tileSetId;
        this.tileId = tileId;
    }

    public int getTileId() {
        return tileId;
    }

    public int getTileSetId() {
        return tileSetId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tileId;
        result = prime * result + tileSetId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TileId other = (TileId) obj;
        if (tileId != other.tileId) {
            return false;
        }
        if (tileSetId != other.tileSetId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tileSetId + "*" + tileId;
    }

}

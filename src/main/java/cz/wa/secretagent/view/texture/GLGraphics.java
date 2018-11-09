package cz.wa.secretagent.view.texture;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import org.jetbrains.annotations.Nullable;

/**
 * Holds all graphics 
 * 
 * @author Ondrej Milenovsky
 */
public class GLGraphics implements SAMGraphics, Serializable {
    private static final long serialVersionUID = 5985274627008462545L;

    private transient Map<Integer, GLTileSet> tileSets;

    public GLGraphics() {
        init();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    private void init() {
        tileSets = new HashMap<Integer, GLTileSet>();
    }

    @Nullable
    public TextureToDraw getTile(TileId tileId) {
        GLTileSet tileSet = tileSets.get(tileId.getTileSetId());
        if (tileSet == null) {
            return null;
        }
        return tileSet.getTile(tileId.getTileId());
    }

    public void setTileSet(int tileSetId, GLTileSet tileSet) {
        if (tileSets.containsKey(tileSetId)) {
            tileSets.get(tileSetId).dispose();
        }
        tileSets.put(tileSetId, tileSet);
    }

    public void clearOrigImages() {
        for (GLTileSet set : tileSets.values()) {
            set.clearOrigImage();
        }
    }

    @Override
    public void dispose() {
        for (GLTileSet set : tileSets.values()) {
            set.dispose();
        }
        init();
    }
}

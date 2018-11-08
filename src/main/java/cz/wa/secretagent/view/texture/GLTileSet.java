package cz.wa.secretagent.view.texture;

import java.util.HashMap;
import java.util.Map;

import cz.wa.secretagent.utils.lwjgl.Texture;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Class representing multiple tiles in single texture.
 * Holds texture and tile size, returns single tile by an index. 
 * 
 * @author Ondrej Milenovsky
 */
public class GLTileSet {

    private final Texture texture;
    private final int sizeX;
    private final int sizeY;

    private Map<Integer, TextureToDraw> cache;

    public GLTileSet(Texture texture, int sizeX, int sizeY) {
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        cache = new HashMap<Integer, TextureToDraw>();
    }

    public TextureToDraw getTile(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        TextureToDraw tex = createTexture(id);
        cache.put(id, tex);
        return tex;
    }

    private TextureToDraw createTexture(int id) {
        int width = texture.getWidth();
        int countX = width / sizeX;
        int x = (id % countX) * sizeX;
        int y = (id / countX) * sizeY;
        return new TextureToDraw(texture, new Rectangle2D(x, y, sizeX, sizeY));
    }

    public void clearOrigImage() {
        texture.clearOrigImage();
    }

    public void dispose() {
        texture.dispose();
    }

}

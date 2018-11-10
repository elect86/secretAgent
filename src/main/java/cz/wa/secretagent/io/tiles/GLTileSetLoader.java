package cz.wa.secretagent.io.tiles;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.wa.secretagent.utils.lwjgl.Texture;
import cz.wa.secretagent.utils.lwjgl.TextureLoader;
import cz.wa.wautils.math.Vector2I;
import secretAgent.view.renderer.GLTileSet;

/**
 * Loads image with multiple tiles. 
 * 
 * @author Ondrej Milenovsky
 */
public class GLTileSetLoader {
    private static final Logger logger = LoggerFactory.getLogger(GLTileSetLoader.class);

    private final File file;

    public GLTileSetLoader(File file) {
        this.file = file;
    }

    public GLTileSet load(Vector2I tileSize, boolean texFilter) throws IOException {
        logger.info("Loading texture: " + file.getAbsolutePath());
        Texture texture = TextureLoader.loadTexture(file, texFilter);
        return new GLTileSet(texture, tileSize.getX(), tileSize.getY());
    }

}

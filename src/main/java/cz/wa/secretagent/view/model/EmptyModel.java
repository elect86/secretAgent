package cz.wa.secretagent.view.model;

import java.util.Collections;
import java.util.Set;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.wautils.math.Rectangle2D;
import secretAgent.world.GLModel;

/**
 * Invisible model. 
 * 
 * @author Ondrej Milenovsky
 */
public class EmptyModel implements GLModel {

    public static final EmptyModel INSTANCE = new EmptyModel();

    private EmptyModel() {
    }

    @Override
    public Set<TileId> getAllTileIds() {
        return Collections.emptySet();
    }

    @Override
    public void linkTextures(SAMGraphics graphics) {
        // nothing
    }

    @Override
    public boolean hasLinkedTextures() {
        return true;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public Rectangle2D getMaxBounds() {
        return Rectangle2D.ZERO;
    }

    @Override
    public ModelType getType() {
        return ModelType.EMPTY;
    }

}

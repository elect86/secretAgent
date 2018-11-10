package cz.wa.secretagent.io.map.orig.generator.entity.usable;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.world.entity.usable.TeleportUsable;
import secretAgent.view.renderer.TileId;
import secretAgent.world.ObjectModel;

/**
 * Creates teleport. 
 * 
 * @author Ondrej Milenovsky
 */
public class TeleportEntityCreator implements EntityCreator<TeleportUsable> {

    private static final long serialVersionUID = -4814593624320559660L;

    @Override
    public TeleportUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        return new TeleportUsable(model, pos);
    }

}

package cz.wa.secretagent.io.map.orig.generator.entity.usable;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.usable.ExitUsable;
import secretAgent.world.ObjectModel;

/**
 * Creates open exit door. 
 * 
 * @author Ondrej Milenovsky
 */
public class ExitEntityCreator implements EntityCreator<ExitUsable> {

    private static final long serialVersionUID = -5234799660153295525L;

    @Override
    public ExitUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        return new ExitUsable(model, pos);
    }
}

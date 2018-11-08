package cz.wa.secretagent.io.map.orig.generator.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;

/**
 * Creates an entity for some EntityType.
 * 
 * @author Ondrej Milenovsky
 */
public interface EntityCreator<E extends Entity> extends Serializable {
    /**
     * Creates entity from parsed information and model.
     * @param args remaining arguments, the list should be emptied or else it will report warnings
     * @param pos position
     * @param tileId tile id
     * @param model model
     * @return new entity
     */
    E createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model);
}

package cz.wa.secretagent.io.map.orig.generator.entity;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;

/**
 * Creates entity by creator from map retrieved by arg0 as an enum 
 * @author Ondrej Milenovsky
 */
public abstract class TypeEntityCreator<E extends Entity> implements EntityCreator<E> {
    private static final long serialVersionUID = 3016926627492944449L;

    private static final Logger logger = LoggerFactory.getLogger(TypeEntityCreator.class);

    private Map<Object, EntityCreator<E>> creators;

    @Override
    public E createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
        if (args.isEmpty()) {
            logger.warn("Not enough arguments in entity definition: " + tileId);
            return null;
        }
        // get subcreator
        Object type;
        String arg0 = args.remove(0);
        try {
            type = getEnum(arg0);
        } catch (IllegalArgumentException e) {
            logger.error("Unknown type: " + arg0, e);
            return null;
        }

        // create
        if (creators.containsKey(type)) {
            return creators.get(type).createEntity(args, pos, tileId, model);
        } else {
            logger.warn("No entity creator for: " + type);
            return null;
        }
    }

    protected abstract Object getEnum(String arg0);

    public Map<Object, EntityCreator<E>> getCreators() {
        return creators;
    }

    @Required
    public void setCreators(Map<Object, EntityCreator<E>> creators) {
        this.creators = creators;
    }

}

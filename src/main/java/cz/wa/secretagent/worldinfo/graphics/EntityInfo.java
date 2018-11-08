package cz.wa.secretagent.worldinfo.graphics;

import cz.wa.secretagent.io.tiles.singleproperties.EntityProperties;

/**
 * Info about entity, same as entity properties. 
 * 
 * @author Ondrej Milenovsky
 */
public class EntityInfo extends EntityProperties {
    public EntityInfo(EntityProperties ep) {
        super(ep.getArgs());
    }
}

package cz.wa.secretagent.worldinfo.graphics;

import secretAgent.io.tiles.EntityProperties;

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

package cz.wa.secretagent.io.map.orig.generator.entity.platform;

import cz.wa.secretagent.io.map.orig.generator.entity.TypeEntityCreator;
import cz.wa.secretagent.world.entity.item.ItemEntity;
import cz.wa.secretagent.world.entity.platform.PlatformType;

/**
 * Creates platforms.
 * 
 * @author Ondrej Milenovsky
 */
public class PlatformEntityCreator extends TypeEntityCreator<ItemEntity> {

    private static final long serialVersionUID = 7144916967720674852L;

    @Override
    protected Object getEnum(String arg0) {
        return PlatformType.valueOf(arg0);
    }

}

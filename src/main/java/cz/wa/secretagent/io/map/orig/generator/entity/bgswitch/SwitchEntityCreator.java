package cz.wa.secretagent.io.map.orig.generator.entity.bgswitch;

import cz.wa.secretagent.io.map.orig.generator.entity.TypeEntityCreator;
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity;
import cz.wa.secretagent.world.entity.bgswitch.SwitchType;

/**
 * Creates switches. 
 * 
 * @author Ondrej Milenovsky
 */
public class SwitchEntityCreator extends TypeEntityCreator<SwitchEntity> {
    private static final long serialVersionUID = 4872658696537751315L;

    @Override
    protected Object getEnum(String arg0) {
        return SwitchType.valueOf(arg0);
    }

}

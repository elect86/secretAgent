package cz.wa.secretagent.io.map.orig.generator.entity.projectile;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.entity.projectile.LevelLaserProjectile;
import secretAgent.world.ObjectModel;

/**
 * Creates level laser entity that will be replaced by laser. 
 * 
 * @author Ondrej Milenovsky
 */
public class LevelLaserEntityCreator implements EntityCreator<LevelLaserProjectile> {

    private static final long serialVersionUID = 972352522358935169L;

    @Override
    public LevelLaserProjectile createEntity(List<String> args, Vector2D pos, TileId tileId,
            ObjectModel model) {
        return new LevelLaserProjectile(model, pos);
    }

}

package cz.wa.secretagent.game.sensor;

import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.wautils.collection.Array2DView;

/**
 * Sensor about level map. 
 * 
 * @author Ondrej Milenovsky
 */
public class LevelSensor {
    private final SAMWorld world;

    public LevelSensor(SAMWorld world) {
        this.world = world;
    }

    public Array2DView<TileType> getTiles() {
        return world.getLevelMap().getTypes();
    }

}

package cz.wa.secretagent.world.map;

import secretAgent.world.ObjectModel;

/**
 * Animated tile with offset in seconds. 
 * 
 * @author Ondrej Milenovsky
 */
public class AnimatedTile extends Tile {
    private final double offsetS;

    public AnimatedTile(TileType type, ObjectModel model, double offsetS) {
        super(type, model);
        this.offsetS = offsetS;
    }

    public double getOffsetS() {
        return offsetS;
    }

}

package cz.wa.secretagent.world.map;

/**
 * Type of tile 
 * 
 * @author Ondrej Milenovsky
 */
public enum TileType {

    /** background tile */
    GHOST(4),
    /** foreground tile */
    GHOST_FRONT(100),
    /** wall */
    WALL(0),
    /** shelf, agent can jump through it but cannot fall */
    SHELF(1),
    /** spikes, impales agents */
    SPIKES(2),
    /** water, agents will drown */
    WATER(3),
    /** this should never get to the level, used in parser */
    ENTITY(Integer.MAX_VALUE);

    /** type priority, lesser means more important */
    private final int priority;

    private TileType(int priority) {
        this.priority = priority;
    }

    public boolean isPreferredTo(TileType o) {
        if (o == null) {
            return true;
        }
        return priority < o.priority;
    }
}

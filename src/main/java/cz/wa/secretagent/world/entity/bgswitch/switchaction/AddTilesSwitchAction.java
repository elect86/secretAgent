package cz.wa.secretagent.world.entity.bgswitch.switchaction;

import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.map.StoredTile;
import secretAgent.view.renderer.TileId;
import secretAgent.world.SamWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Adds tiles to the map. 
 * 
 * @author Ondrej Milenovsky
 */
public class AddTilesSwitchAction implements SwitchAction {

    private final Set<TileId> tileIds;

    public AddTilesSwitchAction(Set<TileId> tileIds) {
        this.tileIds = tileIds;
    }

    @Override
    public void execute(AgentEntity agent, SamWorld world) {
        for (StoredTile tile : getStoredTiles(world)) {
            world.getLevelMap().addTile(tile);
        }
    }

    public Set<TileId> getTileIds() {
        return tileIds;
    }

    /**
     * Find and remove required tiles from stored tiles.
     */
    private List<StoredTile> getStoredTiles(SamWorld world) {
        Set<StoredTile> storedTiles = world.getLevelMap().getStoredTiles();
        List<StoredTile> ret = new ArrayList<StoredTile>(storedTiles.size());
        for (Iterator<StoredTile> it = storedTiles.iterator(); it.hasNext();) {
            StoredTile tile = it.next();
            for (TileId tileId : tileIds) {
                if (tile.getTile().hasTileId(tileId)) {
                    ret.add(tile);
                    it.remove();
                    break;
                }
            }
        }
        return ret;
    }
}

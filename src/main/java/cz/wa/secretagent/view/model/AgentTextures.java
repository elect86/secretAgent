package cz.wa.secretagent.view.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.view.SAMGraphics;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.view.texture.TextureToDraw;
import cz.wa.secretagent.world.entity.agent.AgentAction;
import secretAgent.view.model.AbstractModel;

/**
 * Textures for AgentModel for one direction. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentTextures {
    private final List<TileId> stayIds;
    private final List<TileId> moveIds;
    private final List<TileId> jumpIds;
    private final List<TileId> deathIds;

    private transient List<TextureToDraw> stayTxs;
    private transient List<TextureToDraw> moveTxs;
    private transient List<TextureToDraw> jumpTxs;
    private transient List<TextureToDraw> deathTxs;

    public AgentTextures(List<TileId> stayIds, List<TileId> moveIds, List<TileId> jumpIds,
            List<TileId> deathIds) {
        Validate.notNull(stayIds, "stayIds are null");
        Validate.notNull(moveIds, "moveIds are null");
        Validate.notNull(jumpIds, "jumpIds are null");
        Validate.notNull(deathIds, "deathIds are null");
        this.stayIds = stayIds;
        this.moveIds = moveIds;
        this.jumpIds = jumpIds;
        this.deathIds = deathIds;
    }

    public boolean hasLinkedTextures() {
        return stayTxs != null;
    }

    public void linkTextures(SAMGraphics graphics) {
        stayTxs = AbstractModel.extractTextures(graphics, stayIds);
        moveTxs = AbstractModel.extractTextures(graphics, moveIds);
        jumpTxs = AbstractModel.extractTextures(graphics, jumpIds);
        deathTxs = AbstractModel.extractTextures(graphics, deathIds);
    }

    public List<TextureToDraw> getAllTextures() {
        List<TextureToDraw> ret = new ArrayList<TextureToDraw>();
        ret.addAll(stayTxs);
        ret.addAll(moveTxs);
        ret.addAll(jumpTxs);
        ret.addAll(deathTxs);
        return ret;
    }

    public List<TextureToDraw> getTextures(AgentAction action) {
        if (action == AgentAction.STAY) {
            return stayTxs;
        } else if (action == AgentAction.MOVE) {
            return moveTxs;
        } else if (action == AgentAction.JUMP) {
            return jumpTxs;
        } else if (action == AgentAction.DEATH) {
            return deathTxs;
        } else {
            return null;
        }
    }

    public List<TileId> getStayIds() {
        return stayIds;
    }

    public List<TileId> getMoveIds() {
        return moveIds;
    }

    public List<TileId> getJumpIds() {
        return jumpIds;
    }

    public List<TileId> getDeathIds() {
        return deathIds;
    }

    public List<TextureToDraw> getStayTextures() {
        return stayTxs;
    }

    public List<TextureToDraw> getMoveTextures() {
        return moveTxs;
    }

    public List<TextureToDraw> getJumpTextures() {
        return jumpTxs;
    }

    public List<TextureToDraw> getDeathTextures() {
        return deathTxs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deathIds == null) ? 0 : deathIds.hashCode());
        result = prime * result + ((jumpIds == null) ? 0 : jumpIds.hashCode());
        result = prime * result + ((moveIds == null) ? 0 : moveIds.hashCode());
        result = prime * result + ((stayIds == null) ? 0 : stayIds.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AgentTextures other = (AgentTextures) obj;
        if (deathIds == null) {
            if (other.deathIds != null) {
                return false;
            }
        } else if (!deathIds.equals(other.deathIds)) {
            return false;
        }
        if (jumpIds == null) {
            if (other.jumpIds != null) {
                return false;
            }
        } else if (!jumpIds.equals(other.jumpIds)) {
            return false;
        }
        if (moveIds == null) {
            if (other.moveIds != null) {
                return false;
            }
        } else if (!moveIds.equals(other.moveIds)) {
            return false;
        }
        if (stayIds == null) {
            if (other.stayIds != null) {
                return false;
            }
        } else if (!stayIds.equals(other.stayIds)) {
            return false;
        }
        return true;
    }

}

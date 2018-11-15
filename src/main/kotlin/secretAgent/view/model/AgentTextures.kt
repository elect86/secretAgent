package secretAgent.view.model

import cz.wa.secretagent.view.model.AbstractModel
import secretAgent.view.SamGraphics
import secretAgent.view.renderer.TextureToDraw
import secretAgent.view.renderer.TileId
import secretAgent.world.entity.agent.AgentAction
import java.util.ArrayList

/**
 * Textures for AgentModel for one direction.
 *
 * @author Ondrej Milenovsky
 */
class AgentTextures(
        val stayIds: List<TileId>,
        val moveIds: List<TileId>,
        val jumpIds: List<TileId>,
        val deathIds: List<TileId>) {

    @Transient
    var stayTextures: List<TextureToDraw>? = null
        private set
    @Transient
    var moveTextures: List<TextureToDraw>? = null
        private set
    @Transient
    var jumpTextures: List<TextureToDraw>? = null
        private set
    @Transient
    var deathTextures: List<TextureToDraw>? = null
        private set

    val allTextures: List<TextureToDraw>
        get() {
            val ret = ArrayList<TextureToDraw>()
            ret += stayTextures!!
            ret += moveTextures!!
            ret += jumpTextures!!
            ret += deathTextures!!
            return ret
        }

    fun hasLinkedTextures() = stayTextures != null

    fun linkTextures(graphics: SamGraphics) {
        stayTextures = AbstractModel.extractTextures(graphics, stayIds)
        moveTextures = AbstractModel.extractTextures(graphics, moveIds)
        jumpTextures = AbstractModel.extractTextures(graphics, jumpIds)
        deathTextures = AbstractModel.extractTextures(graphics, deathIds)
    }

    fun getTextures(action: AgentAction): List<TextureToDraw>? {
        return when (action) {
            AgentAction.STAY -> stayTextures
            AgentAction.MOVE -> moveTextures
            AgentAction.JUMP -> jumpTextures
            AgentAction.DEATH -> deathTextures
        }
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + deathIds.hashCode()
        result = prime * result + jumpIds.hashCode()
        result = prime * result + moveIds.hashCode()
        result = prime * result + stayIds.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        return other is AgentTextures && deathIds == other.deathIds && jumpIds == other.jumpIds && moveIds == other.moveIds && stayIds == other.stayIds
    }
}
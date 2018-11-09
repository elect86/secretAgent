package secretAgent.game.player

import cz.wa.secretagent.game.player.PlayerWeapons
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import java.io.Serializable
import kotlin.collections.HashSet
import kotlin.random.Random

/**
 * This class holds only stats, that are saved to file (weapons, ammo, cash, finished levels).
 *
 * @author Ondrej Milenovsky
 */
class PlayerStats : Serializable {

    var cash = 0L
    var finishedLevels: Set<Int> = HashSet()
    lateinit var weapons: PlayerWeapons
    var islandPos: Vector2D? = null
    var seed = Random.nextLong()

    fun deepCopy(): PlayerStats {
        val ret = PlayerStats()
        ret.cash = cash
        ret.finishedLevels = HashSet(finishedLevels)
        ret.islandPos = islandPos
        ret.seed = seed
        ret.weapons = weapons.deepCopy()
        return ret
    }

    companion object {
        private const val serialVersionUID = -8256887380163936352L
    }
}
package secretAgent.game

import org.lwjgl.input.Keyboard
import secretAgent.game.controller.GameIslandController
import secretAgent.game.controller.GameLevelController
import secretAgent.game.player.Camera
import secretAgent.game.player.PlayerStats
import secretAgent.world.entity.HumanAgent
import secretAgent.world.entity.agent.Team
import java.io.Serializable

/**
 * Settings for game.
 *
 * @author Ondrej Milenovsky
 */
class GameSettings : Serializable {

    @JvmField
    var effectObjects = 0.0
    @JvmField
    var weaponEquipPolicy: WeaponEquipPolicy? = null
    @JvmField
    var confirmDialogs: Boolean = false

//    companion object {
//        private val serialVersionUID = -6943322563557755594L
//    }
}

/**
 * Holds classes for player.
 *
 * @author Ondrej Milenovsky
 */
class PlayerHolder : Serializable {

    @Transient
    lateinit var camera: Camera
    @Transient
    lateinit var agent: HumanAgent
    @Transient
    lateinit var keys: PlayerKeys
    @Transient
    lateinit var playerStats: PlayerStats
    @Transient
    var displayedText: String? = null

    lateinit var team: Team
    lateinit var islandController: GameIslandController
    lateinit var levelController: GameLevelController

    companion object {
        private const val serialVersionUID = -6134555295498514501L
    }
}

/**
 * Key definition for a player. (initialized with defaults)
 *
 * @author Ondrej Milenovsky
 */
class PlayerKeys {
    @JvmField
    var kMenu = Keyboard.KEY_ESCAPE

    @JvmField
    var kUp = Keyboard.KEY_UP
    @JvmField
    var kDown = Keyboard.KEY_DOWN
    @JvmField
    var kLeft = Keyboard.KEY_LEFT
    @JvmField
    var kRight = Keyboard.KEY_RIGHT
    @JvmField
    var kFire = Keyboard.KEY_RCONTROL
    @JvmField
    var kJump = Keyboard.KEY_RSHIFT
    @JvmField
    var kActivate = Keyboard.KEY_SPACE

    @JvmField
    var kWeapLeft = Keyboard.KEY_LBRACKET
    @JvmField
    var kWeapRight = Keyboard.KEY_RBRACKET
    @JvmField
    var kNoWeap = Keyboard.KEY_GRAVE
    @JvmField
    var kWeap1 = Keyboard.KEY_1
    @JvmField
    var kWeap2 = Keyboard.KEY_2
    @JvmField
    var kWeap3 = Keyboard.KEY_3
    @JvmField
    var kWeap4 = Keyboard.KEY_4
    @JvmField
    var kWeap5 = Keyboard.KEY_5
    @JvmField
    var kWeap6 = Keyboard.KEY_6
    @JvmField
    var kWeap7 = Keyboard.KEY_7
    @JvmField
    var kWeap8 = Keyboard.KEY_8
    @JvmField
    var kWeap9 = Keyboard.KEY_9
    @JvmField
    var kWeap10 = Keyboard.KEY_0
    @JvmField
    var kDropWeap = Keyboard.KEY_BACK
}

/**
 * When equip a weapon when picked up.
 *
 * @author Ondrej Milenovsky
 */
enum class WeaponEquipPolicy {
    /** never equip new weapon  */
    NEVER,
    /** equip only if no weapon is active  */
    NO_ACTIVE,
    /** equip if no weapon is active or no ammo for current weapon  */
    NO_AMMO,
    /** equip if the weapon is not in inventory  */
    NEW,
    /** always equip picked weapon  */
    ALWAYS
}
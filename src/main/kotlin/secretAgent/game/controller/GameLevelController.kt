package secretAgent.game.controller

import cz.wa.secretagent.game.sensor.AgentWeaponSensor
import cz.wa.secretagent.game.sensor.SensorFactory
import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.world.weapon.Weapon
import cz.wa.secretagent.world.weapon.WeaponOrder
import cz.wa.secretagent.worldinfo.WorldHolder
import org.lwjgl.input.Keyboard
import secretAgent.game.action.ActionFactory
import secretAgent.game.action.AgentLevelAction
import secretAgent.game.controller.menuCreator.GameMenuCreator
import secretAgent.world.entity.EntityXDirection
import secretAgent.world.entity.EntityYDirection

/**
 * Controller that works if the player is in level map. (moving, jumping, shooting)
 *
 * @author Ondrej Milenovsky
 */
class GameLevelController : AbstractPlayerController() {

    lateinit var weaponOrder: WeaponOrder
    lateinit var worldHolder: WorldHolder
    lateinit var gameMenuCreator: GameMenuCreator

    @Transient
    private var action: AgentLevelAction? = null
    @Transient
    private var weaponSensor: AgentWeaponSensor? = null

    @Transient
    private var canFire = false
    @Transient
    private var canSwitch = false
    @Transient
    private var canMenu = false

    @Transient
    private var menuFrame: GFrame? = null

    fun init(actionFactory: ActionFactory?, sensorFactory: SensorFactory?) {
        assert(actionFactory == null == (sensorFactory == null)) { "actionFactory and sensorFactory must be both set or null" }
        if (actionFactory == null) {
            action = null
            weaponSensor = null
        } else {
            action = actionFactory.getAction(AgentLevelAction::class.java)
            weaponSensor = sensorFactory!!.getSensor(AgentWeaponSensor::class.java)
        }
        if (menuFrame == null)
            menuFrame = gameMenuCreator.levelMainMenu
    }

    override fun activate() {
        canFire = false
        canSwitch = false
    }

    override fun processInput(timeS: Double) {
        if (action == null)
            return

        val keys = playerHolder.keys

        when {
            Keyboard.isKeyDown(keys.kLeft) -> action!!.moveX(EntityXDirection.LEFT)
            Keyboard.isKeyDown(keys.kRight) -> action!!.moveX(EntityXDirection.RIGHT)
            else -> action!!.moveX(EntityXDirection.NONE)
        }
        when {
            Keyboard.isKeyDown(keys.kUp) -> action!!.aimY(EntityYDirection.UP)
            Keyboard.isKeyDown(keys.kDown) -> action!!.aimY(EntityYDirection.DOWN)
            else -> action!!.aimY(EntityYDirection.NONE)
        }

        action!!.jump(Keyboard.isKeyDown(keys.kJump))

        // activate
        action!!.activate(Keyboard.isKeyDown(keys.kActivate))

        if (action == null)
            return  // exited level

        // fire
        if (Keyboard.isKeyDown(keys.kFire)) {
            if (canFire) {
                action!!.fire(true)
                return
            }
        } else {
            action!!.fire(false)
            canFire = true
        }

        // weapons
        when {
            Keyboard.isKeyDown(keys.kNoWeap) -> if (canSwitch) {
                action!!.switchWeapon(null)
                canSwitch = false
                canFire = false
            }
            Keyboard.isKeyDown(keys.kWeap1) -> switchWeapon(1)
            Keyboard.isKeyDown(keys.kWeap2) -> switchWeapon(2)
            Keyboard.isKeyDown(keys.kWeap3) -> switchWeapon(3)
            Keyboard.isKeyDown(keys.kWeap4) -> switchWeapon(4)
            Keyboard.isKeyDown(keys.kWeap5) -> switchWeapon(5)
            Keyboard.isKeyDown(keys.kWeap6) -> switchWeapon(6)
            Keyboard.isKeyDown(keys.kWeap7) -> switchWeapon(7)
            Keyboard.isKeyDown(keys.kWeap8) -> switchWeapon(8)
            Keyboard.isKeyDown(keys.kWeap9) -> switchWeapon(9)
            Keyboard.isKeyDown(keys.kWeap10) -> switchWeapon(10)
            Keyboard.isKeyDown(keys.kWeapLeft) -> switchNextWapon(-1)
            Keyboard.isKeyDown(keys.kWeapRight) -> switchNextWapon(1)
            Keyboard.isKeyDown(keys.kDropWeap) -> action!!.dropWeapon()
            else -> canSwitch = true
        }
        if (Keyboard.isKeyDown(keys.kMenu)) {
            if (canMenu) {
                worldHolder.world.isRunning = false
                menuFrame!!.selectedIndex = 0
                worldHolder.menuHolder.addFrame(menuFrame)
                canMenu = false
            }
        } else
            canMenu = true
    }

    private fun switchNextWapon(dir: Int) {
        if (!canSwitch)
            return
        canSwitch = false
        canFire = false
        var w: Weapon? = weaponSensor!!.activeWeapon
        var ind = -dir
        if (w != null)
            ind = weaponOrder.indexOf(w)
        val weapons = weaponSensor!!.weapons
        val wCount = weaponOrder.size
        for (i in 0 until wCount) {
            ind += dir
            if (ind < 0)
                ind = wCount - 1
            else if (ind >= wCount)
                ind = 0
            w = weaponOrder[ind]
            if (w in weapons) {
                action!!.switchWeapon(w)
                return
            }
        }
    }

    private fun switchWeapon(i: Int) {
        if (canSwitch) {
            if (i - 1 < weaponOrder.size) {
                val w = weaponOrder[i - 1]
                action!!.switchWeapon(w)
            }
            canSwitch = false
            canFire = false
        }
    }

    companion object {
        private const val serialVersionUID = -8521706793940602063L
    }
}

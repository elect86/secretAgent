package secretAgent.menu

import org.lwjgl.input.Keyboard
import secretAgent.menu.window.GFrame
import secretAgent.view.Settings2D
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.ArrayList

/**
 * Holds all menu GUI.
 *
 * @author Ondrej Milenovsky
 */
class MenuHolder : Serializable {

    lateinit var settings2d: Settings2D

    @Transient
    val frames: ArrayList<GFrame> = ArrayList()
    @Transient
    var keys: MenuKeys? = null

    /** @return true if there is any menu active     */
    val isMenuActive: Boolean
        get() = frames.isNotEmpty()

    /**
     * @return true if the menu is active and covers all the screen so there is nothing visible beneath it
     */
    val isSolid: Boolean
        get() {
            for (frame in frames) {
                val pos = frame.posSH
                val size = frame.sizeSH
                if (frame.alpha == 1.0 && pos.x <= 0 && pos.y <= 0
                        && pos.x + size.x >= settings2d.screenWidth / settings2d.screenHeight
                        && pos.y + size.y >= 1) {
                    return true
                }
            }
            return false
        }

    val topFrame: GFrame?
        get() = frames.lastOrNull()

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(stream: ObjectInputStream) {
        stream.defaultReadObject()
        frames.clear()
    }

    /**
     * Adds new frame to the top. If the frame is already displayed, moves it to the top.
     * @param frame
     */
    fun addFrame(frame: GFrame) {
        if (frame in frames)
            frames -= frame
         else
            frames += frame
    }

    fun removeTopFrame() = frames.removeAt(frames.lastIndex)

    fun removeAllFrames() = frames.clear()

    companion object {
        private const val serialVersionUID = 2010131159700853426L
    }
}

/**
 * Keys used in menu.
 *
 * @author Ondrej Milenovsky
 */
class MenuKeys {
    var kUp = Keyboard.KEY_UP
    var kDown = Keyboard.KEY_DOWN
    var kLeft = Keyboard.KEY_LEFT
    var kRight = Keyboard.KEY_RIGHT
    var kEnter = Keyboard.KEY_RETURN
    var kBack = Keyboard.KEY_ESCAPE
}
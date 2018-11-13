package secretAgent.menu.window

import cz.wa.wautils.math.Vector2I
import org.apache.commons.lang.Validate
import secretAgent.menu.window.component.ComponentType
import secretAgent.menu.window.component.GButtonListener
import secretAgent.menu.window.component.GComponent
import secretAgent.menu.window.component.GSelectable
import java.awt.Color
import java.util.*
import kotlin.collections.LinkedHashSet

/**
 * Window frame for openGL.
 *
 * @author Ondrej Milenovsky
 */
class GFrame : GComponent() {

    /** Size in tiles  */
    var sizeTiles = Vector2I(0, 0)
        set(sizeTiles) {
            assert(sizeTiles.x > 0 && sizeTiles.y > 0) { "sizeTiles must be > 0 in both dimensions, but is $sizeTiles" }
            field = sizeTiles
        }
    /** Color of border tiles  */
    var borderColor = Color.WHITE
        set(borderColor) {
            Validate.notNull(borderColor, "borderColor is null")
            field = borderColor
        }
    /** Color of middle tiles  */
    var middleColor = Color.WHITE
    /** Child components  */
    val components: MutableSet<GComponent> = LinkedHashSet()
    /** Transparency alpha  */
    var alpha = 1.0
        set(value) {
            assert(value > 0 && value <= 1) { "alpha must be > 0 and <= 1" }
            field = value
        }
    /** listener called when pressed escape  */
    var closeListener: GButtonListener? = null

    /** list of selectable components  */
    private val selectables = ArrayList<GSelectable>()
    /** index of selected component  */
    var selectedIndex = -1
        /** selectedIndex new index of component to select, -1 means none selected     */
        set(value) {
            var v = value
            assert(v < selectables.size) { "selectedIndex must be < ${selectables.size}, but is $v" }
            if (field >= 0)
                selectables[field].isSelected = false
            when {
                v >= 0 -> selectables[v].isSelected = true
                else -> v = -1
            }
            field = v
        }

    override val type: ComponentType
        get() = ComponentType.FRAME

    /** @return selected component or null     */
    val selectedComponent: GSelectable?
        get() = selectables.getOrNull(selectedIndex)

    /** @return number of selectable components     */
    val selectablesCount: Int
        get() = selectables.size

    /**
     * Adds the component to the frame. If the frame already contains the component, then nothing happens.
     * @param c
     */
    fun add(c: GComponent) {
        assert(c.type != ComponentType.FRAME) { "the component must not be frame" }
        if (components.add(c))
            if (c.type == ComponentType.SELECTABLE) {
                selectables += c as GSelectable
                if (selectedIndex < 0)
                    selectedIndex = 0
            }
    }

    /**
     * Removes the component from the frame. If the frame already contains the component, then nothing happens.
     * @param c
     */
    fun remove(c: GComponent) {
        if (components.remove(c))
            if (c.type == ComponentType.SELECTABLE) {
                selectables -= c as GSelectable
                if (selectedIndex >= selectablesCount)
                    selectedIndex = selectables.lastIndex
            }
    }

    /** Just calls the close listener     */
    fun callClose() = closeListener?.invoke()

    /** Select next component. Does nothing, if there are no selectable components.     */
    fun selectNextComponent() {
        if (selectables.isNotEmpty())
            selectedIndex = (selectedIndex + 1) % selectablesCount
    }

    /**
     * Select prev component. Does nothing, if there are no selectable components.
     */
    fun selectPrevComponent() {
        if (selectables.isNotEmpty())
            selectedIndex = (selectedIndex - 1 + selectablesCount) % selectablesCount
    }
}
package secretAgent.menu.window.component

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D

/**
 * Button which can be selected and pressed. Is drawn as the display component and selected frame.
 *
 * @author Ondrej Milenovsky
 */
class GButton : GSelectable {

    override var displayComponent: GComponent? = null
        set(value) {
            field = value.also {
                it!!.posSH = posSH
                it.sizeSH = sizeSH
            }
        }

    lateinit var listener: GButtonListener

    constructor(text: String) {
        displayComponent = GLabel(text)
    }

    constructor(display: GComponent) {
        displayComponent = display
    }

    override var posSH: Vector2D
        get() = super.posSH
        set(value) {
            super.posSH = value
            displayComponent?.posSH = value
        }

    override var sizeSH: Vector2D
        get() = super.sizeSH
        set(value) {
            super.sizeSH = value
            displayComponent?.sizeSH = value
        }

    override fun activate(): Boolean {
        listener()
        return true
    }

    override fun changeLeft() = false

    override fun changeRight() = false
}

/**
 * Listener for GButton.
 *
 * @author Ondrej Milenovsky
 */
typealias GButtonListener = () -> Unit

/**
 * Some selectable component.
 *
 * @author Ondrej Milenovsky
 */
abstract class GSelectable : GComponent() {

    var isSelected: Boolean = false

    abstract val displayComponent: GComponent?

    override val type: ComponentType
        get() = ComponentType.SELECTABLE

    /**
     * Enter pressed on the component.
     * @return true if the component does anything on this action
     */
    abstract fun activate(): Boolean

    /**
     * Left arrow pressed on the component.
     * @return true if the component does anything on this action
     */
    abstract fun changeLeft(): Boolean

    /**
     * Right arrow pressed on the component.
     * @return true if the component does anything on this action
     */
    abstract fun changeRight(): Boolean
}
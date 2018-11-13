package secretAgent.menu.window.component

import org.apache.commons.lang.Validate
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.world.ObjectModel
import java.awt.Color
import kotlin.properties.Delegates

/**
 * Types of components.
 *
 * @author Wanucha
 */
enum class ComponentType {
    FRAME,
    IMAGE,
    LABEL,
    SELECTABLE
}

/**
 * Some component. Units are SH (screen height), so 0.1 means screen_height / 10.
 *
 * @author Ondrej Milenovsky
 */
abstract class GComponent {

    open var posSH = Vector2D(0.0, 0.0)
        set(posSH) {
            assert(!posSH.isNaN && !posSH.isInfinite) {"pos must be real vector, but is $posSH"}
            field = posSH
        }
    open var sizeSH = Vector2D(0.0, 0.0)
        set(sizeSH) {
            assert(!sizeSH.isNaN && !sizeSH.isInfinite){"size must be real vector, but is $sizeSH"}
            field = sizeSH
        }

    abstract val type: ComponentType
}

/**
 * Image with some model (can only handle models that don't need entity).
 *
 * @author Ondrej Milenovsky
 */
class GImage(val model: ObjectModel) : GComponent() {

    override val type: ComponentType
        get() = ComponentType.IMAGE
}

/**
 * Text on screen.
 *
 * @author Ondrej Milenovsky
 */
class GLabel(var text: String = "", var color: Color = Color.WHITE) : GComponent() {

    override val type: ComponentType
        get() = ComponentType.LABEL
}
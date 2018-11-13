package secretAgent.menu

import cz.wa.secretagent.menu.window.GFrame
import cz.wa.secretagent.menu.window.component.GLabel
import cz.wa.secretagent.menu.window.component.selectable.GButton
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener
import cz.wa.wautils.math.Vector2I
import cz.wa.wautils.string.StringUtilsWa
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import org.apache.commons.math3.util.FastMath
import secretAgent.view.renderer.PrimitivesDrawer
import java.awt.Color
import java.io.Serializable
import java.util.*

/**
 * Creates simple question dialogs. The dialog consists of a text and some buttons under it.
 * The dialogs are centered and are scaled to fit the screen.
 *
 * @author Ondrej Milenovsky
 */
class DialogMenuBuilder : Serializable {

    lateinit var primitivesDrawer: PrimitivesDrawer

    /** Min distance between frame content and the border, 1 means height of a letter  */
    var borderWidthTH = 0.0
    /** Gap between the message and buttons, 1 means height of a letter  */
    var afterMessageGapTH = 0.0
    /** Gap between text lines or buttons, 1 means height of a letter  */
    var textVGapTH = 0.0
    /** Gap between screen and frame border, 1 means height of a letter  */
    var screenBorderGapTH = 0.0
    /** Gap between buttons when they are on same line, 1 means height of a letter  */
    var buttonsHGapTH = 0.0

    /** Minimum lines count per frame, this value determines maximum scale of the frame.
     * If the frame contains more lines, then it is scaled down to fit the screen.
     * If the frame contains less lines, then scale of the frame is computed from this value.  */
    var minFrameLines = 0

    /**
     * Creates dialog. First is centered message divided to lines, then there are buttons.
     * @param message the message
     * @param buttons buttons, consist of text and listener
     * @param color color of the frame
     * @param closeListener close listener
     * @param buttonsOnSingleLine true - the buttons are next to each other, false - the buttons are each on new line
     * @return the created frame
     */
    fun createDialog(message: String, buttons: List<TextButtonDescriptor>, color: Color,
                     closeListener: GButtonListener?, buttonsOnSingleLine: Boolean): GFrame {
        val lines = StringUtilsWa.splitToLines(message)

        val buttonLines = when {
            buttonsOnSingleLine -> when {
                buttons.isEmpty() -> 0
                else -> 1
            }
            else -> buttons.size
        }
        val maxLines = FastMath.max(minFrameLines, buttonLines + lines.size)
        // frame size, units are text height
        var widthTH = 0.0
        var heightTH = borderWidthTH * 2 + maxLines * (1 + textVGapTH) - textVGapTH * 2 + afterMessageGapTH

        val screenHeightTHInv = 1.0 / (heightTH + 2 * screenBorderGapTH)

        var i = 0
        // create the message labels
        val labels = ArrayList<GLabel>(lines.size)
        for (line in lines) {
            if (line.isNotEmpty()) {
                val textSize = primitivesDrawer.getTextSize(line)
                val textWidthTH = textSize.x / textSize.y
                widthTH = FastMath.max(widthTH, textWidthTH)

                val label = GLabel(line)
                label.posSH = Vector2D(borderWidthTH, borderWidthTH + i * (1 + textVGapTH)).scalarMultiply(screenHeightTHInv)
                label.sizeSH = Vector2D(textWidthTH, 1.0).scalarMultiply(screenHeightTHInv)
                labels += label
            }
            i++
        }

        // create buttons
        val btns = ArrayList<GButton>(buttons.size)
        var left = 0.0
        for (button in buttons) {
            val textSize = primitivesDrawer.getTextSize(button.text)
            val textWidthTH = textSize.x / textSize.y
            widthTH = FastMath.max(widthTH, textWidthTH)

            val btn = GButton(GLabel(button.text))
            btn.posSH = Vector2D(left + borderWidthTH, borderWidthTH + afterMessageGapTH - textVGapTH + i * (1 + textVGapTH))
                    .scalarMultiply(screenHeightTHInv)
            btn.sizeSH = Vector2D(textWidthTH, 1.0).scalarMultiply(screenHeightTHInv)
            btn.setListener(button.listener)
            btns += btn
            if (buttonsOnSingleLine)
                left += textWidthTH + buttonsHGapTH
            else
                i++
        }
        widthTH += 2 * borderWidthTH

        // center the message
        for (label in labels) {
            val x = widthTH / 2.0 * screenHeightTHInv - label.sizeSH.x / 2.0
            label.posSH = Vector2D(x, label.posSH.y)
        }

        heightTH = borderWidthTH * 2 + (buttons.size + lines.size) * (1 + textVGapTH) - textVGapTH * 2 + afterMessageGapTH
        val sizeTiles = Vector2I.createRounded(widthTH, heightTH)

        // create the frame
        val frame = GFrame().apply {
            this.sizeTiles = sizeTiles
            borderColor = color
            middleColor = color
            sizeSH = Vector2D(widthTH, heightTH).scalarMultiply(screenHeightTHInv)
            posSH = Vector2D(0.5, 0.5).subtract(sizeSH.scalarMultiply(0.5))
            this.closeListener = closeListener
        }
        for (label in labels)
            frame.add(label)
        for (btn in btns)
            frame.add(btn)

        return frame
    }

    companion object {
        private const val serialVersionUID = 5493363750141444630L
    }
}

/**
 * Describes single button with text.
 *
 * @author Ondrej Milenovsky
 */
class TextButtonDescriptor(val text: String, val listener: GButtonListener)
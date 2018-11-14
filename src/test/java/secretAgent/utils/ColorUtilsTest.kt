package secretAgent.utils

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.awt.Color

/**
 * Tests ColorUtils
 *
 * @author Ondrej Milenovsky
 */
class ColorUtilsTest {
    @Test
    fun testParseColor() {
        assertEquals(Color(0, 0, 0), "#000000".parseColor())
        assertEquals(Color(255, 255, 255), "#FFFFFF".parseColor())
        assertEquals(Color(255, 0, 128), "#FF0080".parseColor())

        assertEquals(Color(255, 0, 128, 255), "#FF0080FF".parseColor())
        assertEquals(Color(255, 0, 128, 0), "#FF008000".parseColor())
        assertEquals(Color(0, 255, 0, 128), "#00FF0080".parseColor())
    }
}

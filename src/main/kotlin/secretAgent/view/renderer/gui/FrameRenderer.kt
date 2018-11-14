package secretAgent.view.renderer.gui

import secretAgent.menu.window.GFrame

/**
 * Renders whole GFrame.
 *
 * @author Ondrej Milenovsky
 */
interface FrameRenderer {
    fun drawFrame(frame: GFrame)
}
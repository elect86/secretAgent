package cz.wa.secretagent.view.renderer.gui.component;

import org.apache.commons.lang.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.menu.window.component.GComponent;
import cz.wa.secretagent.view.renderer.PrimitivesDrawer;
import secretAgent.game.player.Camera;

/**
 * Component drawer that has reference to useful objects. 
 *
 * @author Ondrej Milenovsky
 */
public abstract class AbstractComponentDrawer<C extends GComponent> implements ComponentDrawer<C> {

    private static final long serialVersionUID = -7861001462889943667L;

    protected PrimitivesDrawer primitivesDrawer;

    protected double getScreenHeight() {
        return primitivesDrawer.getSettings().screenHeight;
    }

    protected Vector2D getDrawPos(GComponent component, Camera camera) {
        return component.getPosSH().scalarMultiply(getScreenHeight()).add(camera.getPos());
    }

    protected double getDrawTextH(GComponent component) {
        return component.getSizeSH().getY() * getScreenHeight();
    }

    public PrimitivesDrawer getPrimitivesDrawer() {
        return primitivesDrawer;
    }

    @Required
    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
        Validate.notNull(primitivesDrawer, "primitivesDrawer is null");
        this.primitivesDrawer = primitivesDrawer;
    }

}

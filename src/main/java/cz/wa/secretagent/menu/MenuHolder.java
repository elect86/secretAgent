package cz.wa.secretagent.menu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.menu.window.GFrame;
import secretAgent.view.renderer.Settings2D;

/**
 * Holds all menu GUI. 
 * 
 * @author Ondrej Milenovsky
 */
public class MenuHolder implements Serializable {

    private static final long serialVersionUID = 2010131159700853426L;

    private Settings2D settings2d;

    private transient List<GFrame> frames;
    private transient MenuKeys keys;

    public MenuHolder() {
        init();
    }

    private void init() {
        frames = new ArrayList<GFrame>();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    /**
     * Adds new frame to the top. If the frame is already displayed, moves it to the top.
     * @param frame
     */
    public void addFrame(GFrame frame) {
        if (frames.contains(frame)) {
            frames.remove(frame);
        } else {
            frames.add(frame);
        }
    }

    public GFrame removeTopFrame() {
        return frames.remove(frames.size() - 1);
    }

    public void removeAllFrames() {
        frames.clear();
    }

    public List<GFrame> getFrames() {
        return frames;
    }

    /**
     * @return true if there is any menu active
     */
    public boolean isMenuActive() {
        return !frames.isEmpty();
    }

    /**
     * @return true if the menu is active and covers all the screen so there is nothing visible beneath it
     */
    public boolean isSolid() {
        for (GFrame frame : frames) {
            Vector2D pos = frame.getPosSH();
            Vector2D size = frame.getSizeSH();
            if ((frame.getAlpha() == 1) && (pos.getX() <= 0) && (pos.getY() <= 0)
                    && (pos.getX() + size.getX() >= settings2d.screenWidth / settings2d.screenHeight)
                    && (pos.getY() + size.getY() >= 1)) {
                return true;
            }
        }
        return false;
    }

    public Settings2D getSettings2d() {
        return settings2d;
    }

    @Required
    public void setSettings2d(Settings2D settings2d) {
        this.settings2d = settings2d;
    }

    public GFrame getTopFrame() {
        if (frames.isEmpty()) {
            return null;
        } else {
            return frames.get(frames.size() - 1);
        }
    }

    public MenuKeys getKeys() {
        return keys;
    }

    public void setKeys(MenuKeys keys) {
        this.keys = keys;
    }

}

//package cz.wa.secretagent.menu.window.component.selectable;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.menu.window.component.GComponent;
//import cz.wa.secretagent.menu.window.component.GLabel;
//
///**
// * Button which can be selected and pressed. Is drawn as the display component and selected frame.
// *
// * @author Ondrej Milenovsky
// */
//public class GButton extends GSelectable {
//    private GComponent display;
//    private GButtonListener listener;
//
//    public GButton(String text) {
//        display = new GLabel(text);
//    }
//
//    public GButton(GComponent display) {
//        this.display = display;
//    }
//
//    @Override
//    public void setPosSH(Vector2D posSH) {
//        super.setPosSH(posSH);
//        if (display != null) {
//            display.setPosSH(getPosSH());
//        }
//    }
//
//    @Override
//    public void setSizeSH(Vector2D sizeSH) {
//        super.setSizeSH(sizeSH);
//        if (display != null) {
//            display.setSizeSH(sizeSH);
//        }
//    }
//
//    @Override
//    public GComponent getDisplayComponent() {
//        return display;
//    }
//
//    public void setDisplayComponent(GComponent display) {
//        this.display = display;
//        display.setPosSH(getPosSH());
//        display.setSizeSH(getSizeSH());
//    }
//
//    public void setListener(GButtonListener l) {
//        this.listener = l;
//    }
//
//    @Override
//    public boolean activate() {
//        listener.actionPerformed();
//        return true;
//    }
//
//    @Override
//    public boolean changeLeft() {
//        return false;
//    }
//
//    @Override
//    public boolean changeRight() {
//        return false;
//    }
//}

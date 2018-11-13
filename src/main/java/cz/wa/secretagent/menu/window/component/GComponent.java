//package cz.wa.secretagent.menu.window.component;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
///**
// * Some component. Units are SH (screen height), so 0.1 means screen_height / 10.
// *
// * @author Ondrej Milenovsky
// */
//public abstract class GComponent {
//    private Vector2D posSH;
//    private Vector2D sizeSH;
//
//    public Vector2D getPosSH() {
//        return posSH;
//    }
//
//    public void setPosSH(Vector2D posSH) {
//        Validate.notNull(posSH, "pos is null");
//        Validate.isTrue(!posSH.isNaN() && !posSH.isInfinite(), "pos must be real vector, but is " + posSH);
//        this.posSH = posSH;
//    }
//
//    public Vector2D getSizeSH() {
//        return sizeSH;
//    }
//
//    public void setSizeSH(Vector2D sizeSH) {
//        Validate.notNull(sizeSH, "size is null");
//        Validate.isTrue(!sizeSH.isNaN() && !sizeSH.isInfinite(),
//                "size must be real vector, but is " + sizeSH);
//        this.sizeSH = sizeSH;
//    }
//
//    public abstract ComponentType getType();
//
//    @Override
//    public final int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public final boolean equals(Object obj) {
//        return super.equals(obj);
//    }
//}

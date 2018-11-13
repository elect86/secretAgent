//package cz.wa.secretagent.menu.builder;
//
//import java.awt.Color;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.menu.window.GFrame;
//import cz.wa.secretagent.menu.window.component.GLabel;
//import cz.wa.secretagent.menu.window.component.selectable.GButton;
//import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;
//import cz.wa.wautils.math.Vector2I;
//import cz.wa.wautils.string.StringUtilsWa;
//import secretAgent.view.renderer.PrimitivesDrawer;
//
///**
// * Creates simple question dialogs. The dialog consists of a text and some buttons under it.
// * The dialogs are centered and are scaled to fit the screen.
// *
// * @author Ondrej Milenovsky
// */
//public class DialogMenuBuilder implements Serializable {
//
//    private static final long serialVersionUID = 5493363750141444630L;
//
//    private PrimitivesDrawer primitivesDrawer;
//
//    /** Min distance between frame content and the border, 1 means height of a letter */
//    private double borderWidthTH;
//    /** Gap between the message and buttons, 1 means height of a letter */
//    private double afterMessageGapTH;
//    /** Gap between text lines or buttons, 1 means height of a letter */
//    private double textVGapTH;
//    /** Gap between screen and frame border, 1 means height of a letter */
//    private double screenBorderGapTH;
//    /** Gap between buttons when they are on same line, 1 means height of a letter */
//    private double buttonsHGapTH;
//
//    /** Minimum lines count per frame, this value determines maximum scale of the frame.
//     * If the frame contains more lines, then it is scaled down to fit the screen.
//     * If the frame contains less lines, then scale of the frame is computed from this value. */
//    private int minFrameLines;
//
//    /**
//     * Creates dialog. First is centered message divided to lines, then there are buttons.
//     * @param message the message
//     * @param buttons buttons, consist of text and listener
//     * @param color color of the frame
//     * @param closeListener close listener
//     * @param buttonsOnSingleLine true - the buttons are next to each other, false - the buttons are each on new line
//     * @return the created frame
//     */
//    public GFrame createDialog(String message, List<TextButtonDescriptor> buttons, Color color,
//            GButtonListener closeListener, boolean buttonsOnSingleLine) {
//        List<String> lines = StringUtilsWa.splitToLines(message);
//
//        int buttonLines = 0;
//        if (buttonsOnSingleLine) {
//            if (buttons.isEmpty()) {
//                buttonLines = 0;
//            } else {
//                buttonLines = 1;
//            }
//        } else {
//            buttonLines = buttons.size();
//        }
//        int maxLines = FastMath.max(minFrameLines, buttonLines + lines.size());
//        // frame size, units are text height
//        double widthTH = 0;
//        double heightTH = borderWidthTH * 2 + maxLines * (1 + textVGapTH) - textVGapTH * 2
//                + afterMessageGapTH;
//
//        double screenHeightTHInv = 1.0 / (heightTH + 2 * screenBorderGapTH);
//
//        int i = 0;
//        // create the message labels
//        List<GLabel> labels = new ArrayList<GLabel>(lines.size());
//        for (String line : lines) {
//            if (!line.isEmpty()) {
//                Vector2D textSize = primitivesDrawer.getTextSize(line);
//                double textWidthTH = textSize.getX() / textSize.getY();
//                widthTH = FastMath.max(widthTH, textWidthTH);
//
//                GLabel label = new GLabel(line);
//                label.setPosSH(new Vector2D(borderWidthTH, borderWidthTH + i * (1 + textVGapTH))
//                        .scalarMultiply(screenHeightTHInv));
//                label.setSizeSH(new Vector2D(textWidthTH, 1).scalarMultiply(screenHeightTHInv));
//                labels.add(label);
//            }
//            i++;
//        }
//
//        // create buttons
//        List<GButton> btns = new ArrayList<GButton>(buttons.size());
//        double left = 0;
//        for (TextButtonDescriptor button : buttons) {
//            Vector2D textSize = primitivesDrawer.getTextSize(button.getText());
//            double textWidthTH = textSize.getX() / textSize.getY();
//            widthTH = FastMath.max(widthTH, textWidthTH);
//
//            GButton btn = new GButton(new GLabel(button.getText()));
//            btn.setPosSH(new Vector2D(left + borderWidthTH,
//                    borderWidthTH + afterMessageGapTH - textVGapTH + i * (1 + textVGapTH))
//                            .scalarMultiply(screenHeightTHInv));
//            btn.setSizeSH(new Vector2D(textWidthTH, 1).scalarMultiply(screenHeightTHInv));
//            btn.setListener(button.getListener());
//            btns.add(btn);
//            if (buttonsOnSingleLine) {
//                left += textWidthTH + buttonsHGapTH;
//            } else {
//                i++;
//            }
//        }
//        widthTH += 2 * borderWidthTH;
//
//        // center the message
//        for (GLabel label : labels) {
//            double x = widthTH / 2.0 * screenHeightTHInv - label.getSizeSH().getX() / 2.0;
//            label.setPosSH(new Vector2D(x, label.getPosSH().getY()));
//        }
//
//        heightTH = borderWidthTH * 2 + (buttons.size() + lines.size()) * (1 + textVGapTH) - textVGapTH * 2
//                + afterMessageGapTH;
//        Vector2I sizeTiles = Vector2I.createRounded(widthTH, heightTH);
//
//        // create the frame
//        GFrame frame = new GFrame();
//        frame.setSizeTiles(sizeTiles);
//        frame.setBorderColor(color);
//        frame.setMiddleColor(color);
//        frame.setSizeSH(new Vector2D(widthTH, heightTH).scalarMultiply(screenHeightTHInv));
//        frame.setPosSH(new Vector2D(0.5, 0.5).subtract(frame.getSizeSH().scalarMultiply(0.5)));
//        frame.setCloseListener(closeListener);
//
//        for (GLabel label : labels) {
//            frame.add(label);
//        }
//        for (GButton btn : btns) {
//            frame.add(btn);
//        }
//
//        return frame;
//    }
//
//    public PrimitivesDrawer getPrimitivesDrawer() {
//        return primitivesDrawer;
//    }
//
//    @Required
//    public void setPrimitivesDrawer(PrimitivesDrawer primitivesDrawer) {
//        this.primitivesDrawer = primitivesDrawer;
//    }
//
//    public int getMinFrameLines() {
//        return minFrameLines;
//    }
//
//    @Required
//    public void setMinFrameLines(int minFrameLines) {
//        this.minFrameLines = minFrameLines;
//    }
//
//    public double getBorderWidthTH() {
//        return borderWidthTH;
//    }
//
//    @Required
//    public void setBorderWidthTH(double borderWidthTH) {
//        this.borderWidthTH = borderWidthTH;
//    }
//
//    public double getAfterMessageGapTH() {
//        return afterMessageGapTH;
//    }
//
//    @Required
//    public void setAfterMessageGapTH(double afterMessageGapTH) {
//        this.afterMessageGapTH = afterMessageGapTH;
//    }
//
//    public double getTextVGapTH() {
//        return textVGapTH;
//    }
//
//    @Required
//    public void setTextVGapTH(double textVGapTH) {
//        this.textVGapTH = textVGapTH;
//    }
//
//    public double getScreenBorderGapTH() {
//        return screenBorderGapTH;
//    }
//
//    @Required
//    public void setScreenBorderGapTH(double screenBorderGapTH) {
//        this.screenBorderGapTH = screenBorderGapTH;
//    }
//
//    public double getButtonsHGapTH() {
//        return buttonsHGapTH;
//    }
//
//    @Required
//    public void setButtonsHGapTH(double buttonsHGapTH) {
//        this.buttonsHGapTH = buttonsHGapTH;
//    }
//
//}

//package cz.wa.secretagent.view.renderer;
//
//import static org.lwjgl.opengl.GL11.GL_QUADS;
//import static org.lwjgl.opengl.GL11.glBegin;
//import static org.lwjgl.opengl.GL11.glColor4f;
//import static org.lwjgl.opengl.GL11.glEnd;
//import static org.lwjgl.opengl.GL11.glLoadIdentity;
//import static org.lwjgl.opengl.GL11.glRotated;
//import static org.lwjgl.opengl.GL11.glScaled;
//import static org.lwjgl.opengl.GL11.glTexCoord2f;
//import static org.lwjgl.opengl.GL11.glTranslated;
//import static org.lwjgl.opengl.GL11.glVertex2f;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.io.Serializable;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import org.newdawn.slick.SlickException;
//import org.newdawn.slick.UnicodeFont;
//import org.newdawn.slick.font.effects.ColorEffect;
//import org.newdawn.slick.font.effects.ShadowEffect;
//import org.newdawn.slick.opengl.TextureImpl;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.utils.lwjgl.Texture;
//import cz.wa.secretagent.view.Settings2D;
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import cz.wa.wautils.math.Rectangle2D;
//import cz.wa.wautils.math.Rectangle2I;
//import cz.wa.wautils.math.Vector2F;
//import secretAgent.view.DrawPosition;
//
///**
// * Draws primitives to canvas. No other class should draw anything because of the viewport.
// *
// * @author Ondrej Milenovsky
// */
//public class PrimitivesDrawer implements Serializable {
//
//    private static final long serialVersionUID = -4323747666228324745L;
//
//    private static final float SHADOW_ALPHA = 1f;
//    private static final double SHADOW_DIST = 0.1;
//
//    private Settings2D settings;
//    private Font srcFont;
//
//    private transient DrawPosition drawPosition = DrawPosition.CENTER;
//    private transient UnicodeFont font;
//    private transient ShadowEffect shadowEffect;
//    private transient Color texColor;
//    private transient boolean singleUseColor;
//
//    @SuppressWarnings("unchecked")
//    public void init() {
//        font = new UnicodeFont(srcFont);
//        font.addAsciiGlyphs();
//        shadowEffect = new ShadowEffect(Color.BLACK, 1, 1, 1f);
//        font.getEffects().add(shadowEffect);
//        font.getEffects().add(new ColorEffect());
//        try {
//            font.loadGlyphs();
//        } catch (SlickException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void dispose() {
//        font.clearGlyphs();
//        font = null;
//        shadowEffect = null;
//    }
//
//    public Settings2D getSettings() {
//        return settings;
//    }
//
//    @Required
//    public void setSettings(Settings2D settings) {
//        this.settings = settings;
//    }
//
//    /**
//     * All textures will be covered by this color.
//     * @param color color of textures
//     */
//    private void setOverlayColor(Color color) {
//        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
//                color.getAlpha() / 255f);
//    }
//
//    /**
//     * Draws texture as rectangle to canvas.
//     * @param tex the texture
//     * @param pos position in pixels (center)
//     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
//     * @param scale size multiplier in pixels
//     * @param color overlay color (null = white)
//     */
//    public void drawTexture(TextureToDraw tex, Vector2D pos, DrawBounds bounds, double scale) {
//        drawTexture(tex, pos, bounds, scale, false);
//    }
//
//    /**
//     * Draws rotated (90 deg) or flipped texture as rectangle to canvas.
//     * @param tex the texture
//     * @param pos position in pixels (center)
//     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
//     * @param scale size multiplier in pixels
//     * @param color overlay color (null = white)
//     * @param flip true if the texture will be flipped horizontally before rotating
//     */
//    public void drawTexture(TextureToDraw tex, Vector2D pos, DrawBounds bounds, double scale, boolean flip) {
//        Validate.notNull(tex, "texture is null");
//        // tex coords
//        Vector2F[] t = new Vector2F[4];
//        Rectangle2D r = tex.getTileBounds();
//        double w = tex.getTexture().getWidth();
//        double h = tex.getTexture().getHeight();
//        double cut = (settings.texFilter ? 0.5 : 0.01);
//        t[0] = new Vector2F((float) ((r.getX() + cut) / w), (float) ((r.getY() + cut) / h));
//        t[2] = new Vector2F((float) ((r.getX2() - cut) / w), (float) ((r.getY2() - cut) / h));
//        t[1] = new Vector2F(t[2].x, t[0].y);
//        t[3] = new Vector2F(t[0].x, t[2].y);
//        if (flip) {
//            flipTexCoords(t);
//        }
//
//        // canvas coords
//        Vector2F[] p = new Vector2F[4];
//        Vector2D s1;
//        Vector2D s2;
//        if (drawPosition == DrawPosition.CENTER) {
//            s1 = new Vector2D(bounds.getX1() * scale, bounds.getY1() * scale);
//            s2 = new Vector2D(bounds.getX2() * scale, bounds.getY2() * scale);
//        } else if (drawPosition == DrawPosition.UPPER_LEFT) {
//            s1 = Vector2D.ZERO;
//            s2 = new Vector2D(bounds.getX2() - bounds.getX1(), bounds.getY2() - bounds.getY1())
//                    .scalarMultiply(scale);
//        } else {
//            // upper right
//            s1 = new Vector2D(bounds.getX2() - bounds.getX1(), 0).scalarMultiply(scale);
//            s2 = new Vector2D(0, bounds.getY2() - bounds.getY1()).scalarMultiply(scale);
//        }
//        p[0] = new Vector2F(s1);
//        p[2] = new Vector2F(s2);
//        p[1] = new Vector2F(p[2].x, p[0].y);
//        p[3] = new Vector2F(p[0].x, p[2].y);
//
//        // draw
//        if (texColor != null) {
//            setOverlayColor(texColor);
//        } else {
//            setOverlayColor(Color.WHITE);
//        }
//        glTranslated(pos.getX(), pos.getY(), 0);
//        drawTexture(tex.getTexture(), t, p);
//        glLoadIdentity();
//        if (singleUseColor) {
//            texColor = null;
//        }
//    }
//
//    /**
//     * Draws rotated texture.
//     * @param tex the texture
//     * @param pos position in pixels (center)
//     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
//     * @param scale size multiplier in pixels
//     * @param color overlay color (null = white)
//     * @param angle angle in radians to rotate
//     */
//    public void drawTexture(TextureToDraw tex, Vector2D pos, DrawBounds bounds, double scale, double angle) {
//        drawTexture(tex, pos, bounds, scale, angle, false);
//    }
//
//    /**
//     * Draws rotated texture.
//     * @param tex the texture
//     * @param pos position in pixels (center)
//     * @param bounds bounds where the texture will be drawn relative to pos, will be scaled
//     * @param scale size multiplier in pixels
//     * @param color overlay color (null = white)
//     * @param angle angle in radians to rotate
//     * @param flip true if the texture will be flipped horizontally before rotating
//     */
//    public void drawTexture(TextureToDraw tex, Vector2D pos, DrawBounds bounds, double scale, double angle,
//            boolean flip) {
//        // translate and rotate
//        glTranslated(pos.getX(), pos.getY(), 0);
//        glRotated(angle * 180 / FastMath.PI, 0, 0, 1);
//        // draw to 0:0
//        drawTexture(tex, Vector2D.ZERO, bounds, scale, flip);
//        // revert
//        glLoadIdentity();
//    }
//
//    private void flipTexCoords(Vector2F[] t) {
//        Vector2F u;
//        u = t[0];
//        t[0] = t[1];
//        t[1] = u;
//        u = t[2];
//        t[2] = t[3];
//        t[3] = u;
//    }
//
//    private void drawTexture(Texture texture, Vector2F[] t, Vector2F[] p) {
//        texture.bind();
//        glBegin(GL_QUADS);
//        glTexCoord2f(t[0].x, t[0].y);
//        glVertex2f(p[0].x, p[0].y);
//        glTexCoord2f(t[1].x, t[1].y);
//        glVertex2f(p[1].x, p[1].y);
//        glTexCoord2f(t[2].x, t[2].y);
//        glVertex2f(p[2].x, p[2].y);
//        glTexCoord2f(t[3].x, t[3].y);
//        glVertex2f(p[3].x, p[3].y);
//        glEnd();
//    }
//
//    public Vector2D getTextSize(String text) {
//        return new Vector2D(font.getWidth(text), font.getFont().getSize());
//    }
//
//    /**
//     * Draws text on screen.
//     * @param text the text
//     * @param pos center position in pixels
//     * @param fontSize size in pixels
//     * @param color color
//     */
//    public void drawText(String text, Vector2D pos, double fontSize, Color color) {
//        // the slick font uses different textures, need to unbind them manually
//        // also the slick does not revert the color
//        TextureImpl.bindNone();
//        org.newdawn.slick.Color slickColor = new org.newdawn.slick.Color(color.getRed(), color.getGreen(),
//                color.getBlue(), color.getAlpha());
//        fontSize /= srcFont.getSize();
//        int width = (int) FastMath.round(font.getWidth(text) * fontSize);
//        int height = (int) FastMath.round(font.getHeight(text) * fontSize);
//
//        // shadow
//        shadowEffect.setOpacity(color.getAlpha() / 255f * SHADOW_ALPHA);
//        float shadowDist = (float) (fontSize * SHADOW_DIST);
//        shadowEffect.setXDistance(shadowDist);
//        shadowEffect.setYDistance(shadowDist);
//
//        // draw text
//        if (drawPosition == DrawPosition.CENTER) {
//            pos = pos.subtract(new Vector2D(width / 2.0, height / 2.0));
//        } else if (drawPosition == DrawPosition.UPPER_RIGHT) {
//            pos = pos.subtract(new Vector2D(width, 0));
//        }
//        glTranslated(pos.getX(), pos.getY(), 0);
//        glScaled(fontSize, fontSize, 0);
//        font.drawString(0, 0, text, slickColor);
//        // revert
//        glLoadIdentity();
//        Texture.unbind();
//    }
//
//    /**
//     * Fills rectangle on screen, no position remapping
//     * @param rect
//     * @param color
//     */
//    public void fillRect(Rectangle2I rect, Color color) {
//        Texture.unbind();
//        setOverlayColor(color);
//        glBegin(GL_QUADS);
//        glVertex2f(rect.getX(), rect.getY());
//        glVertex2f(rect.getX2(), rect.getY());
//        glVertex2f(rect.getX2(), rect.getY2());
//        glVertex2f(rect.getX(), rect.getY2());
//        glEnd();
//    }
//
//    /**
//     * Draws rectangle on screen, no position remapping
//     * @param rect
//     * @param width
//     * @param color
//     */
//    public void drawRect(Rectangle2D rect, double width, Color color) {
//        float x1 = (float) rect.getX();
//        float y1 = (float) rect.getY();
//        float x2 = (float) rect.getX2();
//        float y2 = (float) rect.getY2();
//        float w = (float) width;
//
//        Texture.unbind();
//        setOverlayColor(color);
//
//        // upper
//        glBegin(GL_QUADS);
//        glVertex2f(x1, y1);
//        glVertex2f(x2, y1);
//        glVertex2f(x2, y1 + w);
//        glVertex2f(x1, y1 + w);
//        glEnd();
//        // lower
//        glBegin(GL_QUADS);
//        glVertex2f(x1, y2);
//        glVertex2f(x2, y2);
//        glVertex2f(x2, y2 - w);
//        glVertex2f(x1, y2 - w);
//        glEnd();
//        // left
//        glBegin(GL_QUADS);
//        glVertex2f(x1, y1 + w);
//        glVertex2f(x1 + w, y1 + w);
//        glVertex2f(x1 + w, y2 - w);
//        glVertex2f(x1, y2 - w);
//        glEnd();
//        // right
//        glBegin(GL_QUADS);
//        glVertex2f(x2, y1 + w);
//        glVertex2f(x2 - w, y1 + w);
//        glVertex2f(x2 - w, y2 - w);
//        glVertex2f(x2, y2 - w);
//        glEnd();
//    }
//
//    public DrawPosition getDrawPosition() {
//        return drawPosition;
//    }
//
//    public void setDrawPosition(DrawPosition drawPosition) {
//        this.drawPosition = drawPosition;
//    }
//
//    /**
//     * Set color for next texture that will be drawn.
//     * @param color the color
//     */
//    public void setTexColor(Color color) {
//        texColor = color;
//        singleUseColor = true;
//    }
//
//    public void setColor(Color color, boolean singleUse) {
//        texColor = color;
//        singleUseColor = singleUse;
//    }
//
//    public Font getSrcFont() {
//        return srcFont;
//    }
//
//    @Required
//    public void setSrcFont(Font srcFont) {
//        this.srcFont = srcFont;
//    }
//
//}

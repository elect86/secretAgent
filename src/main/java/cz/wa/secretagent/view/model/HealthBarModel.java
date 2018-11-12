//package cz.wa.secretagent.view.model;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.apache.commons.lang.Validate;
//
//import cz.wa.secretagent.view.SamGraphics;
//import cz.wa.secretagent.view.TileId;
//import cz.wa.secretagent.view.texture.GLGraphics;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import cz.wa.wautils.math.Rectangle2D;
//
///**
// * Model of health bar.
// *
// * @author Ondrej Milenovsky
// */
//public class HealthBarModel extends AbstractModel {
//    private final TileId frameTileId;
//    private final TileId healthTileId;
//
//    private transient TextureToDraw frameTex;
//    private transient TextureToDraw healthTex;
//
//    public HealthBarModel(TileId frameTileId, TileId healthTileId, double scale) {
//        super(scale);
//        this.frameTileId = frameTileId;
//        this.healthTileId = healthTileId;
//    }
//
//    @Override
//    public ModelType getType() {
//        return ModelType.HEALTH_BAR;
//    }
//
//    @Override
//    public Set<TileId> getAllTileIds() {
//        return new HashSet<TileId>(Arrays.asList(frameTileId, healthTileId));
//    }
//
//    @Override
//    public boolean hasLinkedTextures() {
//        return healthTex != null;
//    }
//
//    @Override
//    protected Rectangle2D linkTexturesInternal(SamGraphics graphics) {
//        frameTex = ((GLGraphics) graphics).getTile(frameTileId);
//        healthTex = ((GLGraphics) graphics).getTile(healthTileId);
//        Validate.notNull(frameTex, "missing tile " + frameTileId);
//        Validate.notNull(healthTex, "missing tile " + healthTileId);
//        if (frameTex != null) {
//            return getModelBounds(frameTex.getTileBounds());
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public Collection<TextureToDraw> getAllTextures() {
//        return Arrays.asList(frameTex, healthTex);
//    }
//
//    public TextureToDraw getFrameTexture() {
//        return frameTex;
//    }
//
//    public TextureToDraw getHealthTexture() {
//        return healthTex;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = super.hashCode();
//        result = prime * result + ((frameTileId == null) ? 0 : frameTileId.hashCode());
//        result = prime * result + ((healthTileId == null) ? 0 : healthTileId.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (!super.equals(obj)) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        HealthBarModel other = (HealthBarModel) obj;
//        if (frameTileId == null) {
//            if (other.frameTileId != null) {
//                return false;
//            }
//        } else if (!frameTileId.equals(other.frameTileId)) {
//            return false;
//        }
//        if (healthTileId == null) {
//            if (other.healthTileId != null) {
//                return false;
//            }
//        } else if (!healthTileId.equals(other.healthTileId)) {
//            return false;
//        }
//        return true;
//    }
//
//}

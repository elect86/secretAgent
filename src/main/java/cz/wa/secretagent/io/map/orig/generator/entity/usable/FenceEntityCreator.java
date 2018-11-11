//package cz.wa.secretagent.io.map.orig.generator.entity.usable;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
//import cz.wa.secretagent.world.entity.usable.FenceUsable;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ObjectModel;
//
///**
// * Creates fence blocking final level.
// *
// * @author Ondrej Milenovsky
// */
//public class FenceEntityCreator implements EntityCreator<FenceUsable> {
//
//    private static final long serialVersionUID = 8907848311734064349L;
//
//    @Override
//    public FenceUsable createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
//        return new FenceUsable(model, pos);
//    }
//}

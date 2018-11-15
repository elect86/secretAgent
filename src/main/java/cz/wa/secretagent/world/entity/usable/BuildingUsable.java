//package cz.wa.secretagent.world.entity.usable;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import secretAgent.world.ObjectModel;
//
///**
// * Building on island map.
// *
// * @author Ondrej Milenovsky
// */
//public class BuildingUsable extends UsableEntity {
//
//    private int levelId;
//    private final String finishedModel;
//    private final boolean finalBuilding;
//
//    public BuildingUsable(ObjectModel model, Vector2D pos, String finishedModel, boolean finalBuilding) {
//        super(model, pos, true);
//        this.finishedModel = finishedModel;
//        this.finalBuilding = finalBuilding;
//        levelId = -1;
//    }
//
//    public int getLevelId() {
//        return levelId;
//    }
//
//    public void setLevelId(int levelId) {
//        this.levelId = levelId;
//    }
//
//    public boolean isFinalBuilding() {
//        return finalBuilding;
//    }
//
//    public String getFinishedModel() {
//        return finishedModel;
//    }
//
//    @Override
//    public UsableType getSecondType() {
//        return UsableType.BUILDING;
//    }
//}

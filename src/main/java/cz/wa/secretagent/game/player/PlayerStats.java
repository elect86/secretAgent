//package cz.wa.secretagent.game.player;
//
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Set;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
///**
// * This class holds only stats, that are saved to file (weapons, ammo, cash, finished levels).
// *
// * @author Ondrej Milenovsky
// */
//public class PlayerStats implements Serializable {
//
//    private static final long serialVersionUID = -8256887380163936352L;
//
//    private static Random random = new Random();
//
//    private long cash;
//    private Set<Integer> finishedLevels;
//    private PlayerWeapons weapons;
//    private Vector2D islandPos;
//    private long seed;
//
//    public PlayerStats() {
//        finishedLevels = new HashSet<Integer>();
//        seed = random.nextLong();
//    }
//
//    public long getCash() {
//        return cash;
//    }
//
//    public void setCash(long cash) {
//        this.cash = cash;
//    }
//
//    public Set<Integer> getFinishedLevels() {
//        return finishedLevels;
//    }
//
//    public void setFinishedLevels(Set<Integer> finishedLevels) {
//        this.finishedLevels = finishedLevels;
//    }
//
//    public PlayerWeapons getWeapons() {
//        return weapons;
//    }
//
//    public void setWeapons(PlayerWeapons weapons) {
//        this.weapons = weapons;
//    }
//
//    public Vector2D getIslandPos() {
//        return islandPos;
//    }
//
//    public void setIslandPos(Vector2D islandPos) {
//        this.islandPos = islandPos;
//    }
//
//    public long getSeed() {
//        return seed;
//    }
//
//    public void setSeed(long seed) {
//        this.seed = seed;
//    }
//
//    public PlayerStats deepCopy() {
//        PlayerStats ret = new PlayerStats();
//        ret.setCash(cash);
//        ret.setFinishedLevels(new HashSet<Integer>(finishedLevels));
//        ret.setIslandPos(islandPos);
//        ret.setSeed(seed);
//        ret.setWeapons(weapons.deepCopy());
//        return ret;
//    }
//
//}

//package cz.wa.secretagent.io.map.orig.generator.entity.item;
//
//import java.util.Collection;
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
//import cz.wa.secretagent.world.entity.item.AmmoItem;
//import cz.wa.secretagent.world.weapon.Weapon;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ObjectModel;
//
///**
// * Creates ammo items.
// *
// * @author Ondrej Milenovsky
// */
//public class AmmoEntityCreator implements EntityCreator<AmmoItem> {
//    private static final long serialVersionUID = -3881522195676956109L;
//
//    private static final Logger logger = LoggerFactory.getLogger(AmmoEntityCreator.class);
//
//    private Collection<Weapon> weapons;
//
//    @Override
//    public AmmoItem createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
//        int count = 0;
//        String arg0 = args.remove(0);
//        try {
//            count = Integer.parseInt(arg0);
//        } catch (NumberFormatException e) {
//            logger.error("Wrong ammo count number: " + arg0 + " for ammo: " + tileId, e);
//            return null;
//        }
//        if (count <= 0) {
//            logger.error("Ammo count must be > 0: " + arg0 + " for ammo: " + tileId);
//            return null;
//        }
//        Weapon weapon = null;
//        if (!args.isEmpty()) {
//            String arg1 = args.remove(0);
//            weapon = findWeapon(arg1);
//            if (weapon == null) {
//                logger.error("Wrong weapon name: " + arg1 + " for ammo: " + tileId);
//            }
//        }
//        return new AmmoItem(model, pos, weapon, count);
//    }
//
//    private Weapon findWeapon(String name) {
//        for (Weapon weapon : weapons) {
//            if (weapon.getName().equals(name)) {
//                return weapon;
//            }
//        }
//        return null;
//    }
//
//    public Collection<Weapon> getWeapons() {
//        return weapons;
//    }
//
//    @Required
//    public void setWeapons(Collection<Weapon> weapons) {
//        this.weapons = weapons;
//    }
//
//}

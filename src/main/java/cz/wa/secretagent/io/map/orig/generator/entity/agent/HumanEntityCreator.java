//package cz.wa.secretagent.io.map.orig.generator.entity.agent;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.io.map.orig.generator.entity.EntityCreator;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.entity.agent.Team;
//import cz.wa.secretagent.world.weapon.Weapon;
//import cz.wa.wautils.math.Rectangle2D;
//import secretAgent.view.model.AgentModel;
//import secretAgent.view.renderer.TileId;
//import secretAgent.world.ObjectModel;
//import secretAgent.world.entity.EntityXDirection;
//
///**
// * Creates player start position.
// *
// * @author Ondrej Milenovsky
// */
//public class HumanEntityCreator implements EntityCreator<HumanAgent> {
//    private static final long serialVersionUID = -9044910312669697692L;
//
//    private static final Logger logger = LoggerFactory.getLogger(HumanEntityCreator.class);
//
//    private Rectangle2D sizeBounds;
//    private Team team;
//    private EnemyHumanPropertiesCreator propertiesCreator;
//
//    @Override
//    public HumanAgent createEntity(List<String> args, Vector2D pos, TileId tileId, ObjectModel model) {
//        EntityXDirection dir;
//        if (args.isEmpty()) {
//            logger.warn("Human agent model requires one more argument for human type: " + tileId);
//            return null;
//        }
//        EnemyHumanType type = getType(args.remove(0));
//        if (args.isEmpty()) {
//            dir = AgentCreatorUtils.getDirection(tileId, (AgentModel) model);
//        } else {
//            dir = EntityXDirection.valueOf(args.remove(0));
//        }
//        HumanAgent human = new HumanAgent(model, pos, team, dir, sizeBounds);
//
//        EnemyHumanProperties humanProperties = propertiesCreator.createProperties(type);
//        human.setCapabilities(humanProperties.getCapabilities());
//        human.setHealth(humanProperties.getCapabilities().getMaxHealth());
//        List<Weapon> weapons = humanProperties.getWeapons();
//        if (!weapons.isEmpty()) {
//            human.setWeapon(weapons.get(0));
//        }
//        for (Weapon weapon : weapons) {
//            human.getInventory().addWeapon(weapon);
//            human.getInventory().addAmmo(weapon, Integer.MAX_VALUE);
//        }
//        return human;
//    }
//
//    private EnemyHumanType getType(String str) {
//        try {
//            return EnemyHumanType.valueOf(str);
//        } catch (IllegalArgumentException e) {
//            logger.error("Unknown enemy human type: " + str, e);
//        }
//        return null;
//    }
//
//    public Rectangle2D getSizeBounds() {
//        return sizeBounds;
//    }
//
//    @Required
//    public void setSizeBounds(Rectangle2D sizeBounds) {
//        this.sizeBounds = sizeBounds;
//    }
//
//    public Team getTeam() {
//        return team;
//    }
//
//    @Required
//    public void setTeam(Team team) {
//        this.team = team;
//    }
//
//    public EnemyHumanPropertiesCreator getPropertiesCreator() {
//        return propertiesCreator;
//    }
//
//    @Required
//    public void setPropertiesCreator(EnemyHumanPropertiesCreator propertiesCreator) {
//        this.propertiesCreator = propertiesCreator;
//    }
//
//}

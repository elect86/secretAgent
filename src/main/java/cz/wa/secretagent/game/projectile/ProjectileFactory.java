package cz.wa.secretagent.game.projectile;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.view.model.ExplosionModel;
import cz.wa.secretagent.view.model.LaserModel;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.agent.AgentType;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.direction.EntityXDirection;
import cz.wa.secretagent.world.entity.explosion.Explosion;
import cz.wa.secretagent.world.entity.laser.LineLaser;
import cz.wa.secretagent.world.entity.projectile.DynamiteProjectile;
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity;
import cz.wa.secretagent.world.entity.projectile.ProjectileType;
import cz.wa.secretagent.world.weapon.Weapon;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Factory that creates projectiles fired from weapons.
 * Also creates lasers and explosions.
 * 
 * @author Ondrej Milenovsky
 */
public class ProjectileFactory implements Serializable {
    private static final long serialVersionUID = -6967087863987254819L;

    private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);

    private static final String DYNAMITE_MODEL = "dynamite";
    private static final double DYNAMITE_FUSE = 3;

    private static final String LASER_SIGHT_MODEL = "laserSight";
    private static final double LASER_SIGHT_WIDTH = 2;

    private transient Random random;

    private WorldHolder worldHolder;

    public ProjectileFactory() {
        init();
    }

    private void init() {
        random = new Random();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    /**
     * Creates projectile fired by the agent.
     * @param weapon weapon from which is the projectile fired
     * @param agent agent who fired the projectile
     * @return new projectile, can be null if the agent cannot fire (place mine in the air)
     */
    public ProjectileEntity createProjectile(Weapon weapon, AgentEntity agent) {
        ProjectileEntity projectile = createEntity(weapon, agent);
        // position
        projectile.setPos(agent.getPos().add(agent.getWeaponCenter()));
        // speed
        double spd = weapon.getProjectileSpeed();
        double angle = 0;
        if (agent.getSecondType() == AgentType.HUMAN) {
            angle = ((HumanAgent) agent).getAimAngle();
        }
        if (agent.getDirection() == EntityXDirection.LEFT) {
            angle = FastMath.PI - angle;
        }
        if (weapon.getInaccuracyDg() != 0) {
            angle += (random.nextDouble() - 0.5) * 2 * weapon.getInaccuracyDg() * FastMath.PI / 180;
        }
        Vector2D speed = new Vector2D(spd * FastMath.cos(angle), spd * FastMath.sin(angle));
        projectile.setSpeed(speed);
        // other parameters
        projectile.setDamage(weapon.getProjectileDamage());
        projectile.setRemainingDist(weapon.getRange());
        return projectile;
    }

    /**
     * Creates projectile entity without any non final parameters.
     */
    private ProjectileEntity createEntity(Weapon weapon, AgentEntity agent) {
        ObjectModel model = worldHolder.getGraphicsInfo().getModel(weapon.getProjectileModelName());
        ProjectileType type = weapon.getProjectileType();
        return type.createEntity(model, agent.getTeam());
    }

    /**
     * Creates dynamite at the position
     * @param pos center position
     * @return new dynamite entity
     */
    public DynamiteProjectile createDynamite(Vector2D pos) {
        ObjectModel model = worldHolder.getGraphicsInfo().getModel(DYNAMITE_MODEL);
        return new DynamiteProjectile(model, pos, DYNAMITE_FUSE);
    }

    /**
     * Creates laser defined by two points with no damage. 
     * @param pos1 point 1
     * @param pos2 point 2
     * @param color color
     * @return new laser entity
     */
    public LineLaser createLaserSight(Vector2D pos1, Vector2D pos2, Color color) {
        ObjectModel model1 = worldHolder.getGraphicsInfo().getModel(LASER_SIGHT_MODEL);
        if (!(model1 instanceof LaserModel)) {
            logger.warn("Model '" + LASER_SIGHT_MODEL + "' must be laser model");
            return null;
        }
        LaserModel model2 = (LaserModel) model1;
        LaserModel model = model2.copyWithColor(color);
        return new LineLaser(model, pos1, null, 0, pos2, LASER_SIGHT_WIDTH, false);
    }

    /**
     * Creates explosion. (does not add to the world)
     * @param pos center position
     * @param modelName model name
     * @param radius radius
     * @param damage damage in center
     * @param durationS duration in seconds
     * @return the explosion entity
     */
    public Explosion createExplosion(Vector2D pos, String modelName, double radius, double damage,
            double durationS) {
        ObjectModel model = worldHolder.getGraphicsInfo().getModel(modelName);
        if (!(model instanceof ExplosionModel)) {
            logger.warn("Model '" + modelName + "' must be explosion model");
            return null;
        }
        return new Explosion(model, pos, radius, damage, durationS);
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }
}

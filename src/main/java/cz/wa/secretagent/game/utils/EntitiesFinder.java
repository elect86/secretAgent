package cz.wa.secretagent.game.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import cz.wa.secretagent.Constants;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.SAMWorld;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.agent.AgentType;
import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity;
import cz.wa.secretagent.world.entity.platform.PlatformEntity;
import cz.wa.secretagent.world.entity.platform.PlatformType;
import cz.wa.secretagent.world.entity.usable.UsableEntity;
import cz.wa.secretagent.world.map.TileType;
import cz.wa.wautils.math.Rectangle2D;

/**
 * Finds specific collections of entities from all entities. 
 * 
 * @author Ondrej Milenovsky
 */
public class EntitiesFinder {
    private final SAMWorld world;

    public EntitiesFinder(SAMWorld world) {
        this.world = world;
    }

    /**
     * @return all platforms and cans (solid usables create their walls)
     */
    public List<Entity> getSolidEntities() {
        List<Entity> ret = new ArrayList<Entity>();
        EntityMap entityMap = world.getEntityMap();
        for (Entity entity : entityMap.getEntities(EntityType.PLATFORM)) {
            ret.add(entity);
        }
        return ret;
    }

    /**
     * @return list of entities that can fall (human agents, movable cans, some items)
     */
    public List<Entity> getFallableEntities() {
        List<Entity> ret = new ArrayList<Entity>();
        EntityMap entityMap = world.getEntityMap();
        for (Entity entity : entityMap.getEntities(EntityType.AGENT)) {
            if (entity.getSecondType() == AgentType.HUMAN) {
                ret.add(entity);
            }
        }
        for (Entity entity : entityMap.getEntities(EntityType.PLATFORM)) {
            if (entity.getSecondType() == PlatformType.CAN) {
                ret.add(entity);
            }
        }
        for (Entity entity : entityMap.getEntities(EntityType.ITEM)) {
            if (!entity.isStaticPos()) {
                ret.add(entity);
            }
        }
        return ret;
    }

    /**
     * Finds closest usable entity in front of the agent (usables and switches)
     */
    public Entity findUsableEntity(AgentEntity agent, double maxDist) {
        Entity ret = null;
        Vector2D addVector = agent.getDirection().getVector().scalarMultiply(Constants.TILE_SIZE.getX());
        Vector2D pos = agent.getPos().add(addVector);
        Rectangle2D bounds = agent.getSizeBounds().move(agent.getPos())
                .expand(new Vector2D(maxDist, maxDist));

        // usables
        for (Entity entity : world.getEntityMap().getEntities(EntityType.USABLE)) {
            UsableEntity usable = (UsableEntity) entity;
            Rectangle2D entityBounds = usable.getSizeBounds().move(usable.getPos());
            if (usable.isActive() && bounds.intersectsOrTouches(entityBounds)
                    && isBySide(entityBounds, agent.getPos())) {
                if ((ret == null) || (pos.distance(usable.getPos()) < pos.distance(ret.getPos()))) {
                    ret = usable;
                }
            }
        }
        // switches
        for (Entity entity : world.getEntityMap().getEntities(EntityType.SWITCH)) {
            Rectangle2D entityBounds = entity.getSizeBounds().move(entity.getPos());
            if (((SwitchEntity) entity).isActive()) {
                if (bounds.intersectsOrTouches(entityBounds) && isBySide(entityBounds, agent.getPos())) {
                    if ((ret == null) || (pos.distance(entity.getPos()) < pos.distance(ret.getPos()))) {
                        ret = entity;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Finds entities standing on the platform.
     * @param lift some platform
     * @return list of entities
     */
    public List<Entity> findEntitiesToCarry(PlatformEntity lift) {
        Rectangle2D liftBounds = lift.getSizeBounds().move(lift.getPos());
        List<Entity> carying = new ArrayList<Entity>(3);
        for (Entity e2 : getFallableEntities()) {
            EntityObserver posSensor = new EntityObserver(e2, world);
            if (posSensor.isStandingOn(lift)) {
                // check if the entity is standing more on tile
                Rectangle2D entityBounds = e2.getSizeBounds().move(e2.getPos());
                double liftDx = getXIntersect(entityBounds, liftBounds);
                boolean add = true;
                for (TileSensing tile : posSensor.getTilesUnder()) {
                    if ((tile.getType() == TileType.WALL) && (posSensor.isStandingOn(tile.getBounds()))) {
                        double dx = getXIntersect(entityBounds, tile.getBounds());
                        if (liftDx <= dx) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add) {
                    carying.add(e2);
                }
            }
        }
        return carying;
    }

    /**
     * Intersecting length of x values, they already intersects.
     */
    private double getXIntersect(Rectangle2D bounds1, Rectangle2D bounds2) {
        return FastMath.min(bounds1.getX2(), bounds2.getX2()) - FastMath.max(bounds1.getX(), bounds2.getX());
    }

    /**
     * @param objectBounds the object bounds
     * @param entityPos the entity center
     * @return if the entity center is vertically or horizontally within the object
     */
    private boolean isBySide(Rectangle2D objectBounds, Vector2D entityPos) {
        return ((objectBounds.getX() < entityPos.getX()) && (objectBounds.getX2() > entityPos.getX()))
                || ((objectBounds.getY() < entityPos.getY()) && (objectBounds.getY2() > entityPos.getY()));
    }
}

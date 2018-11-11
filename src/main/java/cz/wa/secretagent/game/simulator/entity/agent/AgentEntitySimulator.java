//package cz.wa.secretagent.game.simulator.entity.agent;
//
//import java.util.ArrayList;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.game.PlayerHolder;
//import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
//import cz.wa.secretagent.game.simulator.entity.projectile.ProjectileHitTypes;
//import cz.wa.secretagent.game.utils.EntitiesFinder;
//import cz.wa.secretagent.game.utils.EntityMover;
//import cz.wa.secretagent.game.utils.EntityObserver;
//import cz.wa.secretagent.game.utils.collision.CollidedTile;
//import cz.wa.secretagent.game.utils.collision.ProjectileCollider;
//import cz.wa.secretagent.world.EntityMap;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.EntityType;
//import cz.wa.secretagent.world.entity.agent.AgentAction;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//import cz.wa.secretagent.world.entity.agent.AgentType;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
//import cz.wa.secretagent.world.entity.bgswitch.SwitchEntity;
//import cz.wa.secretagent.world.entity.item.AmmoItem;
//import cz.wa.secretagent.world.entity.item.ItemEntity;
//import cz.wa.secretagent.world.entity.item.ItemType;
//import cz.wa.secretagent.world.entity.laser.LineLaser;
//import cz.wa.secretagent.world.entity.projectile.BulletProjectile;
//import cz.wa.secretagent.world.entity.projectile.ProjectileEntity;
//import cz.wa.secretagent.world.entity.usable.DoorUsable;
//import cz.wa.secretagent.world.entity.usable.UsableType;
//import cz.wa.secretagent.world.weapon.Weapon;
//import cz.wa.wautils.math.Rectangle2D;
//import cz.wa.wautils.math.VectorUtils;
//import secretAgent.game.ProjectileFactory;
//import secretAgent.view.model.EmptyModel;
//import secretAgent.world.entity.EntityXDirection;
//import secretAgent.world.entity.EntityYDirection;
//
///**
// * Simulates an agent.
// * Agent interacts with solid items in the world. Can use usables, pick up items, fire, move.
// * This simulator does not care about platforms moving with the agent, explosions, projectiles, pushing cans.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentEntitySimulator extends AbstractEntitySimulator<AgentEntity> {
//
//    private static final long serialVersionUID = 4679035238019821173L;
//
//    private final double STAY_MAX_SPEED = 1;
//    private final double MAX_DIST_FROM_USABLE = 2;
//
//    private ProjectileFactory projectileFactory;
//
//    private double fallG;
//    /** aiming speed deg/sec */
//    private double aimSpeedDS;
//    private double maxTotalSpeed;
//    /** speed of slowing down when standing on ground */
//    private double slowMove;
//
//    @Override
//    public boolean move(AgentEntity entity, double timeS) {
//        if (worldHolder.getWorld().isIsland()) {
//            moveOnIsland(entity, timeS);
//        } else {
//            moveInLevel(entity, timeS);
//        }
//        return true;
//    }
//
//    /**
//     * Perform moving on island.
//     */
//    private void moveOnIsland(AgentEntity agent, double timeS) {
//        agent.addActionTime(FastMath.round(timeS * 1000));
//        EntityMover entityMover = new EntityMover(worldHolder.getWorld());
//
//        // move
//        entityMover.moveOnIsland(agent, timeS, STAY_MAX_SPEED);
//        // clip
//        entityMover.doClipping(agent);
//        // find usable item
//        findUsables(agent);
//    }
//
//    /**
//     * Finds and processes usables
//     */
//    private void findUsables(AgentEntity agent) {
//        if (agent.getCapabilities().canActivate()) {
//            Entity usable = new EntitiesFinder(worldHolder.getWorld()).findUsableEntity(agent,
//                    MAX_DIST_FROM_USABLE);
//            agent.setEntityToUse(usable);
//            PlayerHolder playerHolder = worldHolder.getPlayerHolder();
//            if ((playerHolder.getAgent() == agent) && (usable != null)) {
//                playerHolder.setDisplayedText(createDysplayText(usable));
//            } else {
//                playerHolder.setDisplayedText(null);
//            }
//        }
//    }
//
//    /**
//     * Creates text to display when about to use some entity
//     */
//    private String createDysplayText(Entity usable) {
//        if (usable.getType() == EntityType.SWITCH) {
//            return ((SwitchEntity) usable).getDescription();
//        } else if (usable.getType() == EntityType.USABLE) {
//            if (usable.getSecondType() == UsableType.DOOR) {
//                return "Open door with " + ((DoorUsable) usable).getLockType();
//            } else if (usable.getSecondType() == UsableType.TELEPORT) {
//                return "Use teleport";
//            } else if (usable.getSecondType() == UsableType.BUILDING) {
//                return "Enter building";
//            } else if (usable.getSecondType() == UsableType.EXIT_DOOR) {
//                return "Use dynamite";
//            } else if (usable.getSecondType() == UsableType.EXIT) {
//                return "Finish building";
//            }
//        }
//        return "Activate";
//    }
//
//    /**
//     * Perform moving in level.
//     */
//    private void moveInLevel(AgentEntity agent, double timeS) {
//        agent.addActionTime(FastMath.round(timeS * 1000));
//        EntityMover entityMover = new EntityMover(worldHolder.getWorld());
//        AgentAction newAction = AgentAction.STAY;
//        // fall
//        EntityObserver posSensor = new EntityObserver(agent, worldHolder.getWorld());
//        Vector2D speed = agent.getSpeed();
//        boolean onGround = posSensor.isOnGround();
//        if (onGround) {
//            // remove blast speed, fall and jump
//            double spdX = speed.getX();
//            if (spdX != 0) {
//                if (spdX > 0) {
//                    spdX -= slowMove * timeS;
//                    if (spdX < 0) {
//                        spdX = 0;
//                    }
//                } else {
//                    spdX += slowMove * timeS;
//                    if (spdX > 0) {
//                        spdX = 0;
//                    }
//                }
//            }
//            agent.setSpeed(new Vector2D(spdX, 0));
//        } else {
//            agent.setSpeed(speed.add(new Vector2D(0, getFallG() * timeS)));
//            newAction = AgentAction.JUMP;
//        }
//        // move
//        newAction = entityMover.moveInLevel(agent, timeS, STAY_MAX_SPEED, getMaxTotalSpeed(), newAction,
//                onGround, posSensor);
//        // clip
//        entityMover.doClipping(agent);
//        // find usable item
//        findUsables(agent);
//
//        if (agent.getCurrentAction() != AgentAction.DEATH) {
//            agent.setCurrentAction(newAction);
//        }
//        // pick up items
//        if ((agent.getSecondType() == AgentType.HUMAN) && (agent.getCurrentAction() != AgentAction.DEATH)) {
//            pickUpItems((HumanAgent) agent);
//        }
//        // aim
//        processAiming(agent, onGround, timeS);
//        // fire
//        processFiring(agent, onGround, timeS);
//    }
//
//    private void processFiring(AgentEntity agent, boolean onGround, double timeS) {
//        double reload = agent.getReloadTimeRemainingS() - timeS;
//        agent.setReloadTimeRemainingS(FastMath.max(reload, 0));
//        if (agent.isFiring() && agent.getSecondType() == AgentType.HUMAN) {
//            HumanAgent human = (HumanAgent) agent;
//            Weapon weapon = human.getWeapon();
//            if ((weapon != null) && (human.getInventory().getAmmo(weapon) > 0)
//                    && (weapon.isAimMove() || onGround)) {
//                if (reload <= 0) {
//                    fireWeapon(human, timeS);
//                }
//            } else {
//                agent.setFiring(false);
//            }
//        }
//    }
//
//    /**
//     * Only rotate aiming
//     * @param onGround
//     */
//    private void processAiming(AgentEntity agent, boolean onGround, double timeS) {
//        if (agent.getSecondType() == AgentType.HUMAN) {
//            HumanAgent human = (HumanAgent) agent;
//            Weapon weapon = human.getWeapon();
//            // equipped weapon can aim
//            if ((weapon != null) && weapon.isAimRotate()) {
//                EntityYDirection aiming = human.getAiming();
//                if (!onGround && !weapon.isAimMove()) {
//                    // in the air and the weapon cannot work in the air
//                    human.setAimAngle(0);
//                    human.setAiming(EntityYDirection.NONE);
//                    LineLaser laser = human.getLaserSights();
//                    if (laser != null) {
//                        worldHolder.getWorld().getEntityMap().removeEntity(laser);
//                    }
//                    human.setLaserSights(null);
//                } else {
//                    if (aiming != EntityYDirection.NONE) {
//                        // aiming
//                        double add = aiming.getVector().getY() * aimSpeedDS * FastMath.PI / 180.0 * timeS;
//                        double angle = human.getAimAngle() + add;
//                        if (angle < -FastMath.PI / 2.0) {
//                            angle = -FastMath.PI / 2.0;
//                        } else if (angle > FastMath.PI / 2.0) {
//                            angle = FastMath.PI / 2.0;
//                        }
//                        human.setAimAngle(angle);
//                    }
//                    if (weapon.getLaserSights() != null) {
//                        // laser sight
//                        processLaserSights(human);
//                    }
//                }
//            }
//        }
//    }
//
//    private void processLaserSights(HumanAgent human) {
//        LineLaser laser = human.getLaserSights();
//        if (laser == null) {
//            return;
//        }
//        Weapon weapon = human.getWeapon();
//        double dist = weapon.getRange();
//        double angle = human.getAimAngle();
//        Vector2D pos = human.getWeaponCenter();
//        if (human.getDirection() == EntityXDirection.LEFT) {
//            angle = FastMath.PI - angle;
//            pos = new Vector2D(-pos.getX(), pos.getY());
//        }
//        pos = pos.add(human.getPos());
//        // create tmp projectile to simulate collision
//        ProjectileEntity projectile = new BulletProjectile(EmptyModel.INSTANCE, null);
//        projectile.setPos(pos);
//        projectile.setSpeed(new Vector2D(FastMath.cos(angle), FastMath.sin(angle)).scalarMultiply(dist));
//        projectile.setRemainingDist(dist);
//        // find collision
//        CollidedTile collision = new ProjectileCollider(worldHolder.getWorld(), projectile, 1)
//                .findNearestTileCollision(ProjectileHitTypes.TILE_TYPES);
//        // get second position
//        Vector2D pos2;
//        if (collision == null) {
//            pos2 = pos.add(projectile.getSpeed());
//        } else {
//            pos2 = collision.getHitPos();
//        }
//        // modify the laser
//        laser.setPos(pos);
//        laser.setPos2(pos2);
//    }
//
//    /**
//     * Fire weapon, remove ammo, add projectiles to the world, update reloading, ...
//     * Can fail if projectile factory does not create a projectile because of environment.
//     */
//    private void fireWeapon(HumanAgent human, double timeS) {
//        Weapon weapon = human.getWeapon();
//        double reloadTimeS = weapon.getReloadTimeS();
//        if (tryFireWeapon(human)) {
//            human.getInventory().removeOneAmmo(weapon);
//            if (reloadTimeS >= 0) {
//                human.setReloadTimeRemainingS(reloadTimeS);
//            } else {
//                human.setFiring(false);
//            }
//        } else {
//            human.setFiring(false);
//        }
//    }
//
//    /**
//     * Try to fire the weapon, create projectiles, add them to the world.
//     * Can fail only if the weapon cannot be fired because of environment (does not check ammo)
//     * @return success
//     */
//    private boolean tryFireWeapon(HumanAgent human) {
//        Weapon weapon = human.getWeapon();
//        EntityMap entityMap = worldHolder.getWorld().getEntityMap();
//        ProjectileEntity lastProjectile = null;
//        // create projectiles
//        for (int i = 0; i < weapon.getProjectileCount(); i++) {
//            lastProjectile = projectileFactory.createProjectile(weapon, human);
//            if (lastProjectile == null) {
//                return false;
//            }
//            entityMap.addEntity(lastProjectile);
//        }
//        // shake weapon
//        if ((weapon.getProjectileCount() == 1) && (weapon.getInaccuracyDg() != 0)) {
//            double angle = VectorUtils.getAngle(lastProjectile.getSpeed());
//            if (human.getDirection() == EntityXDirection.LEFT) {
//                angle = FastMath.PI - angle;
//            }
//            human.setAimAngle(angle);
//        }
//        return true;
//    }
//
//    /**
//     * Tries to pick up items with the agent.
//     */
//    private void pickUpItems(HumanAgent agent) {
//        Rectangle2D agentBounds = agent.getSizeBounds().move(agent.getPos());
//        for (Entity entity : new ArrayList<Entity>(
//                worldHolder.getWorld().getEntityMap().getEntities(EntityType.ITEM))) {
//            Rectangle2D itemBounds = entity.getSizeBounds().move(entity.getPos());
//            ItemEntity item = (ItemEntity) entity;
//            if (agentBounds.intersects(itemBounds)
//                    && agent.getCapabilities().getInventoryLimits().canAdd(agent.getInventory(), item)) {
//                pickUpItem(agent, item);
//            }
//        }
//    }
//
//    private void pickUpItem(HumanAgent agent, ItemEntity item) {
//        AgentInventory inventory = agent.getInventory();
//        if (item.getSecondType() == ItemType.AMMO) {
//            AmmoItem ammo = (AmmoItem) item;
//            if (ammo.getWeapon() == null) {
//                if (agent.getWeapon() == null) {
//                    return;
//                } else {
//                    inventory.addAmmo(agent.getWeapon(), ammo.getCount());
//                    worldHolder.getWorld().getEntityMap().removeEntity(item);
//                    return;
//                }
//            }
//        }
//        worldHolder.getWorld().getEntityMap().removeEntity(item);
//        inventory.addItem(item);
//        // TODO equip weapon
//    }
//
//    public double getFallG() {
//        return fallG;
//    }
//
//    @Required
//    public void setFallG(double fallG) {
//        this.fallG = fallG;
//    }
//
//    public double getMaxTotalSpeed() {
//        return maxTotalSpeed;
//    }
//
//    @Required
//    public void setMaxTotalSpeed(double maxTotalSpeed) {
//        this.maxTotalSpeed = maxTotalSpeed;
//    }
//
//    public ProjectileFactory getProjectileFactory() {
//        return projectileFactory;
//    }
//
//    @Required
//    public void setProjectileFactory(ProjectileFactory projectileFactory) {
//        this.projectileFactory = projectileFactory;
//    }
//
//    public double getAimSpeedDS() {
//        return aimSpeedDS;
//    }
//
//    @Required
//    public void setAimSpeedDS(double aimSpeedDS) {
//        this.aimSpeedDS = aimSpeedDS;
//    }
//
//    public double getSlowMove() {
//        return slowMove;
//    }
//
//    @Required
//    public void setSlowMove(double slowMove) {
//        this.slowMove = slowMove;
//    }
//}

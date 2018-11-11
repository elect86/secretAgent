//package cz.wa.secretagent.game.action;
//
//import java.util.Set;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//
//import cz.wa.secretagent.game.utils.EntityObserver;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//import cz.wa.secretagent.world.entity.agent.AgentType;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.entity.laser.LineLaser;
//import cz.wa.secretagent.world.weapon.Weapon;
//import secretAgent.game.ProjectileFactory;
//import secretAgent.game.action.AgentAction;
//import secretAgent.game.action.AgentActivateAction;
//import secretAgent.world.entity.EntityXDirection;
//import secretAgent.world.entity.EntityYDirection;
//
///**
// * Action to control agent in level map.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentLevelAction extends AgentAction<AgentEntity> {
//
//    private AgentActivateAction activateAction;
//    private EntityObserver posSensor;
//    private ProjectileFactory projectileFactory;
//
//    private boolean canRepeatFire = false;
//    private boolean fireDown = false;
//
//    AgentLevelAction() {
//    }
//
//    @Override
//    public void init() {
//        activateAction = getActionFactory().getAction(AgentActivateAction.class);
//        posSensor = new EntityObserver(getAgent(), getWorld());
//        projectileFactory = getActionFactory().getProjectileFactory();
//    }
//
//    public void moveX(EntityXDirection dir) {
//        Validate.notNull(dir, "vector is null");
//        AgentEntity agent = getAgent();
//        if (agent.isControlable()) {
//            if (!isAimingWeapon()) {
//                double maxSpeed = agent.getCapabilities().getMaxSpeed();
//                agent.setMoveSpeed(new Vector2D(0, agent.getMoveSpeed().getY()).add(dir.getVector()
//                        .scalarMultiply(maxSpeed)));
//            } else {
//                agent.setMoveSpeed(Vector2D.ZERO);
//            }
//            if (dir != EntityXDirection.NONE) {
//                agent.setDirection(dir);
//            }
//        }
//    }
//
//    public void aimY(EntityYDirection dir) {
//        Validate.notNull(dir, "vector is null");
//        AgentEntity agent = getAgent();
//        if (agent.isControlable() && agent.getCapabilities().canAim()
//                && (agent.getSecondType() == AgentType.HUMAN)) {
//            HumanAgent human = (HumanAgent) agent;
//            Weapon weapon = human.getWeapon();
//            if (weapon == null) {
//                return;
//            }
//            if (weapon.isAimRotate()) {
//                if (fireDown && weapon.isAimRotate() && (human.getReloadTimeRemainingS() <= 0)) {
//                    human.setAiming(dir);
//                    processLaserSight(human, weapon);
//                } else {
//                    human.setAiming(EntityYDirection.NONE);
//                }
//            } else {
//                human.setAiming(dir);
//                if (dir == EntityYDirection.UP) {
//                    human.setAimAngle(-FastMath.PI / 2.0);
//                } else if ((dir == EntityYDirection.DOWN) && !posSensor.isOnGround()) {
//                    human.setAimAngle(FastMath.PI / 2.0);
//                } else {
//                    human.setAimAngle(0);
//                }
//            }
//        }
//    }
//
//    private void processLaserSight(HumanAgent human, Weapon weapon) {
//        if ((weapon.getLaserSights() != null) && (human.getLaserSights() == null)) {
//            // has no laser but needs one, create it but collapsed (might be immediately removed)
//            Vector2D pos = human.getPos();
//            LineLaser laser = projectileFactory.createLaserSight(pos, pos, weapon.getLaserSights());
//            // use the laser
//            human.setLaserSights(laser);
//            getWorld().getEntityMap().addEntity(laser);
//        }
//    }
//
//    public void jump(boolean b) {
//        if (getAgent().getSecondType() == AgentType.HUMAN) {
//            HumanAgent agent = (HumanAgent) getAgent();
//            double jumpStrength = agent.getCapabilities().getJumpStrength();
//            if (agent.isControlable() && (jumpStrength > 0) && !isAimingWeapon()) {
//                if (b && (agent.isJumping() || posSensor.isOnGround())) {
//                    if (!agent.isJumping()) {
//                        agent.setJumpRemainingS(agent.getCapabilities().getJumpTimeS());
//                    }
//                    agent.setJumping(true);
//                } else {
//                    agent.setJumping(false);
//                }
//            }
//        }
//    }
//
//    public void activate(boolean b) {
//        if (!b) {
//            activateAction.stopUsing();
//            return;
//        }
//        AgentEntity agent = getAgent();
//        if (agent.isControlable()) {
//            if (agent.getSecondType() == AgentType.HUMAN) {
//                HumanAgent human = (HumanAgent) agent;
//                if (fireDown && (human.getWeapon() != null) && human.getWeapon().isAimRotate()) {
//                    // cancel aiming
//                    human.setAimAngle(0);
//                    human.setAiming(EntityYDirection.NONE);
//                    removeLaserSight(human);
//                    fireDown = false;
//                    canRepeatFire = false;
//                    return;
//                }
//            }
//            if (agent.getCapabilities().canActivate()) {
//                Entity usable = agent.getEntityToUse();
//                if (usable != null) {
//                    activateAction.useEntity(usable);
//                }
//            }
//        }
//    }
//
//    public void fire(boolean b) {
//        AgentEntity agent = getAgent();
//        if (!b) {
//            canRepeatFire = true;
//        }
//        if (agent.isControlable() && (agent.getSecondType() == AgentType.HUMAN)) {
//            HumanAgent human = (HumanAgent) agent;
//            Weapon weapon = human.getWeapon();
//            if (weapon == null) {
//                human.setFiring(false);
//                return;
//            }
//            if (weapon.isAimRotate()) {
//                if (!b) {
//                    // aiming weapon and not holding the trigger
//                    if (fireDown) {
//                        // fire
//                        fireAimedWeapon(human);
//                    } else {
//                        // reset rotating weapon
//                        human.setFiring(false);
//                        human.setAimAngle(0);
//                    }
//                } else if (!canRepeatFire) {
//                    return;
//                }
//            } else if (b && (human.getInventory().getAmmo(weapon) > 0)) {
//                // check repeat fire weapon
//                if (weapon.isAimRotate()) {
//                    human.setFiring(false);
//                } else if (canRepeatFire || (weapon.getReloadTimeS() >= 0)) {
//                    fireAimedWeapon(human);
//                    canRepeatFire = false;
//                } else {
//                    human.setFiring(false);
//                }
//            } else {
//                human.setFiring(false);
//            }
//            fireDown = b;
//        }
//    }
//
//    private void fireAimedWeapon(HumanAgent human) {
//        human.setFiring(true);
//        removeLaserSight(human);
//    }
//
//    private void removeLaserSight(HumanAgent human) {
//        LineLaser laser = human.getLaserSights();
//        if (laser != null) {
//            getWorld().getEntityMap().removeEntity(laser);
//            human.setLaserSights(null);
//        }
//    }
//
//    public void switchWeapon(Weapon weapon) {
//        if (getAgent().isControlable()) {
//            HumanAgent agent = (HumanAgent) getAgent();
//            if (agent.isFiring() || (agent.getReloadTimeRemainingS() > 0)) {
//                return;
//            }
//            Set<Weapon> weapons = agent.getInventory().getWeapons();
//            if ((weapon == null) || weapons.contains(weapon)) {
//                agent.setAimAngle(0);
//                agent.setWeapon(weapon);
//            }
//        }
//    }
//
//    public void dropWeapon() {
//        if (getAgent().isControlable()) {
//            // TODO drop weapon
//        }
//    }
//
//    /**
//     * @return if currently aiming weapon and cannot move
//     */
//    private boolean isAimingWeapon() {
//        if (fireDown && (getAgent().getSecondType() == AgentType.HUMAN)) {
//            HumanAgent agent = (HumanAgent) getAgent();
//            Weapon weapon = agent.getWeapon();
//            return (weapon != null) && weapon.isAimRotate() && !weapon.isAimMove()
//                    && (agent.getReloadTimeRemainingS() <= 0);
//        } else {
//            return false;
//        }
//    }
//}

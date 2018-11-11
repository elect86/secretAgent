//package cz.wa.secretagent.game.utils;
//
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.EntityType;
//import cz.wa.secretagent.world.entity.agent.AgentAction;
//import cz.wa.secretagent.world.entity.agent.AgentEntity;
//import cz.wa.secretagent.world.entity.agent.AgentType;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.map.LevelMap;
//import cz.wa.secretagent.world.map.TileType;
//import cz.wa.wautils.collection.Array2D;
//import cz.wa.wautils.math.Rectangle2D;
//import cz.wa.wautils.math.Vector2I;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import secretAgent.world.SamWorld;
//
///**
// * Class with useful methods for simulators and sensors.
// *
// * @author Ondrej Milenovsky
// */
//public class EntityMover {
//    private final SamWorld world;
//
//    public EntityMover(SamWorld world) {
//        this.world = world;
//    }
//
//    public void moveOnIsland(AgentEntity agent, double timeS, double maxStaySpeed) {
//        Vector2D speedMove = agent.getMoveSpeed();
//        // limit move speed
//        double maxSpeed = agent.getCapabilities().getMaxSpeed();
//        double speedNorm = speedMove.getNorm();
//        if (speedNorm > maxSpeed) {
//            speedMove = speedMove.scalarMultiply(maxSpeed / speedNorm);
//            speedNorm = maxSpeed;
//            agent.setMoveSpeed(speedMove);
//        }
//
//        // set action
//        if (speedNorm <= maxStaySpeed) {
//            agent.setCurrentAction(AgentAction.STAY);
//        } else {
//            agent.setCurrentAction(AgentAction.MOVE);
//        }
//
//        // move
//        Vector2D pos = agent.getPos().add(speedMove.scalarMultiply(timeS));
//        agent.setPos(pos);
//    }
//
//    /**
//     * Modifies speed and moves the agent. Does not perform clipping except shelfs and platforms.
//     * @param agent agent to move
//     * @param timeS time diff
//     * @param maxStaySpeed max speed still considered as staying
//     * @param maxTotalSpeed max total speed
//     * @param newAction new action
//     * @param onGround if standing on ground
//     * @param posSensor position sensor
//     * @return modified new action
//     */
//    public AgentAction moveInLevel(AgentEntity agent, double timeS, double maxStaySpeed,
//            double maxTotalSpeed, AgentAction newAction, boolean onGround, EntityObserver posSensor) {
//        Vector2D speedMove = agent.getMoveSpeed();
//        // limit move speed
//        double maxSpeed = agent.getCapabilities().getMaxSpeed();
//        double speedX = speedMove.getX();
//        if (speedX > maxSpeed) {
//            speedX = maxSpeed;
//        } else if (speedX < -maxSpeed) {
//            speedX = -maxSpeed;
//        }
//        speedMove = new Vector2D(speedX, speedMove.getY());
//        agent.setMoveSpeed(speedMove);
//
//        // jump
//        if (agent.getSecondType() == AgentType.HUMAN) {
//            HumanAgent human = (HumanAgent) agent;
//            if (human.isJumping()) {
//                double jumpTimeS = FastMath.min(human.getJumpRemainingS(), timeS);
//                human.setJumpRemainingS(human.getJumpRemainingS() - timeS);
//
//                Vector2D jumpAdd = new Vector2D(0, -agent.getCapabilities().getJumpStrength() * jumpTimeS);
//                agent.setSpeed(agent.getSpeed().add(jumpAdd));
//                if (human.getJumpRemainingS() <= 0) {
//                    human.setJumping(false);
//                }
//            }
//        }
//
//        // set action
//        if ((newAction != AgentAction.JUMP) && (FastMath.abs(speedX) > maxStaySpeed)) {
//            newAction = AgentAction.MOVE;
//        }
//
//        // limit total speed
//        Vector2D speed = agent.getSpeed().add(speedMove);
//        double speedNorm = speed.getNorm();
//        if (speedNorm > maxTotalSpeed) {
//            speed = speed.scalarMultiply(maxTotalSpeed / speedNorm);
//            agent.setSpeed(speed.subtract(speedMove));
//        }
//        // move
//        Vector2D speedPS = speed.scalarMultiply(timeS);
//        moveWithStepables(agent, speedPS, onGround, posSensor);
//        return newAction;
//    }
//
//    /**
//     * Moves the agent and checks if fell on shelf or platform
//     * @param entity the entity to move
//     * @param speedPS speed in pixel/s
//     * @param onGround if standing on ground
//     */
//    public void moveWithStepables(Entity agent, Vector2D speedPS, boolean onGround, EntityObserver posSensor) {
//        // move
//        Vector2D pos = agent.getPos().add(speedPS);
//        agent.setPos(pos);
//
//        // check shelfs and platforms if falling
//        if (!onGround && (speedPS.getY() > 0)) {
//            // shelfs
//            TileSensing[] tilesUnder = posSensor.getTilesUnder();
//            Rectangle2D agentBounds = agent.getSizeBounds().move(pos);
//            for (TileSensing tile : tilesUnder) {
//                // check if he is touching a shelf with legs
//                if ((tile.getType() == TileType.SHELF) && (agentBounds.intersects(tile.getBounds()))) {
//                    if (stepOn(agent, speedPS, agentBounds, tile.getBounds())) {
//                        return;
//                    }
//                }
//            }
//            // platforms
//            for (Entity entity : new EntitiesFinder(world).getSolidEntities()) {
//                Rectangle2D entityBounds = entity.getSizeBounds().move(entity.getPos());
//                if (agentBounds.intersects(entityBounds) && stepOn(agent, speedPS, agentBounds, entityBounds)) {
//                    return;
//                }
//            }
//        }
//    }
//
//    /**
//     * Makes the agent step on the target tile (or platform)
//     * @param entity the entity
//     * @param speedPS speed for this frame
//     * @param agentBounds moved bounds of the agent
//     * @param tileBounds not moved bounds of the tile
//     * @return true if stepped
//     */
//    private boolean stepOn(Entity agent, Vector2D speedPS, Rectangle2D agentBounds, Rectangle2D tileBounds) {
//        // check if was above the tile before moved
//        Vector2D pos = agent.getPos();
//        double y2 = agentBounds.getY2() - speedPS.getY();
//        double tileY1 = tileBounds.getY();
//        if (y2 <= tileY1) {
//            pos = new Vector2D(pos.getX(), tileY1 - agent.getSizeBounds().getY2());
//            agent.setPos(pos);
//            agent.setSpeed(new Vector2D(agent.getSpeed().getX(), 0));
//            // no need to check anything else
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Pushes agent (entity) the best direction from the walls
//     */
//    public void doClipping(Entity agent) {
//        // DISGUSTING EVIL !!! I'm sure the original game has something nicer
//        LevelMap map = world.getLevelMap();
//        // agent pos
//        double px = agent.getPos().getX();
//        double py = agent.getPos().getY();
//        // agent speed
//        Vector2D speed = agent.getSpeed();
//        if (agent.getType() == EntityType.AGENT) {
//            speed = speed.add(((AgentEntity) agent).getMoveSpeed());
//        }
//        double spdX = speed.getX();
//        double spdY = speed.getY();
//        double spdX2 = agent.getSpeed().getX();
//        double spdY2 = agent.getSpeed().getY();
//        // agent bounds
//        Rectangle2D agentSize = agent.getSizeBounds();
//        double sx1 = px + agentSize.getX();
//        double sy1 = py + agentSize.getY();
//        double sx2 = px + agentSize.getX2();
//        double sy2 = py + agentSize.getY2();
//        // tile index
//        Vector2D tileSize = map.getTileSize();
//        int ix = (int) FastMath.floor(px / tileSize.getX());
//        int iy = (int) FastMath.floor(py / tileSize.getY());
//        // tile pos
//        double tx = (ix + 0.5) * tileSize.getX();
//        double ty = (iy + 0.5) * tileSize.getY();
//        // check only 4 closest tiles around him
//        TileType t11 = getType(map, ix, iy);
//        TileType t21 = getType(map, ix + 1, iy);
//        TileType t12 = getType(map, ix, iy + 1);
//        TileType t22 = getType(map, ix + 1, iy + 1);
//        // stop speed
//        boolean stopX = false;
//        boolean stopY = false;
//        boolean stopJump = false;
//        // upper left tile
//        if ((t11 == TileType.WALL) && (sx1 < tx) && (sy1 < ty)) {
//            double dx = tx - sx1;
//            double dy = ty - sy1;
//            if ((t12 == TileType.WALL) || ((t21 != TileType.WALL) && (dx < dy))) {
//                // push right
//                px += dx;
//                sx1 += dx;
//                sx2 += dx;
//                if ((spdX < 0) && (spdX2 < 0)) {
//                    stopX = true;
//                }
//            } else {
//                // push down
//                py += dy;
//                sy1 += dy;
//                sy2 += dy;
//                if ((spdY < 0) && (spdY2 < 0)) {
//                    stopY = true;
//                }
//                stopJump = true;
//            }
//        }
//        // upper right tile
//        if ((t21 == TileType.WALL) && (sx2 > tx) && (sy1 < ty)) {
//            double dx = tx - sx2;
//            double dy = ty - sy1;
//            if ((t22 == TileType.WALL) || ((t11 != TileType.WALL) && (-dx < dy))) {
//                // push left
//                px += dx;
//                sx1 += dx;
//                sx2 += dx;
//                if ((spdX > 0) && (spdX2 > 0)) {
//                    stopX = true;
//                }
//            } else {
//                // push down
//                double d = ty - sy1;
//                py += d;
//                sy1 += d;
//                sy2 += d;
//                if ((spdY < 0) && (spdY2 < 0)) {
//                    stopY = true;
//                }
//                stopJump = true;
//            }
//        }
//        // lower left tile
//        if ((t12 == TileType.WALL) && (sx1 < tx) && (sy2 > ty)) {
//            double dx = tx - sx1;
//            double dy = ty - sy2;
//            if ((t11 == TileType.WALL) || ((t22 != TileType.WALL) && (dx < -dy))) {
//                // push right
//                px += dx;
//                sx1 += dx;
//                sx2 += dx;
//                if ((spdX < 0) && (spdX2 < 0)) {
//                    stopX = true;
//                }
//            } else {
//                // push up
//                py += dy;
//                sy1 += dy;
//                sy2 += dy;
//                if ((spdY > 0) && (spdY2 > 0)) {
//                    stopY = true;
//                }
//                stopJump = true;
//            }
//        }
//        // lower right tile
//        if ((t22 == TileType.WALL) && (sx2 > tx) && (sy2 > ty)) {
//            double dx = tx - sx2;
//            double dy = ty - sy2;
//            if ((t21 == TileType.WALL) || ((t12 != TileType.WALL) && (-dx < -dy))) {
//                // push left
//                px += dx;
//                sx1 += dx;
//                sx2 += dx;
//                if ((spdX > 0) && (spdX2 > 0)) {
//                    stopX = true;
//                }
//            } else {
//                // push up
//                py += dy;
//                sy1 += dy;
//                sy2 += dy;
//                if ((spdY > 0) && (spdY2 > 0)) {
//                    stopY = true;
//                }
//                stopJump = true;
//            }
//        }
//        if (stopJump && (agent.getSecondType() == AgentType.HUMAN) && ((HumanAgent) agent).isJumping()) {
//            spdY2 = 0;
//        }
//        speed = new Vector2D(stopX ? 0 : spdX2, stopY ? 0 : spdY2);
//        agent.setSpeed(speed);
//        agent.setPos(new Vector2D(px, py));
//    }
//
//    /**
//     * Get type at the position, if out of map, returns WALL.
//     */
//    private TileType getType(LevelMap map, int x, int y) {
//        Vector2I i = new Vector2I(x, y);
//        Array2D<TileType> types = map.getTypes();
//        if (types.isInside(i)) {
//            return types.get(i);
//        } else {
//            return TileType.WALL;
//        }
//    }
//
//}

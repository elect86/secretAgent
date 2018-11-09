package cz.wa.secretagent.game.simulator.entity.platform;

import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.game.utils.EntitiesFinder;
import cz.wa.secretagent.game.utils.EntityMover;
import cz.wa.secretagent.game.utils.EntityObserver;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.platform.MovableCan;
import cz.wa.secretagent.world.entity.platform.PlatformEntity;
import cz.wa.secretagent.world.entity.platform.PlatformType;
import cz.wa.wautils.math.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.world.SamWorld;

import java.util.List;

/**
 * Simulates a pushable can.
 * Can is pushed by agents and moves things stanting on it.
 *
 * @author Ondrej Milenovsky
 */
public class CanEntitySimulator extends AbstractEntitySimulator<PlatformEntity> {

    private static final long serialVersionUID = -3871663176151622542L;

    private double canFallG;
    private double canMaxSpeed;

    @Override
    public boolean move(PlatformEntity entity, double timeS) {
        moveCan((MovableCan) entity, timeS);
        return true;
    }

    /**
     * Can is being pushed by agents that can activate, moves entities standing on it.
     */
    private void moveCan(MovableCan can, double timeS) {
        SamWorld world = worldHolder.getWorld();
        // push
        for (Entity e2 : world.getEntityMap().getEntities(EntityType.AGENT)) {
            AgentEntity agent = (AgentEntity) e2;
            // get agent's future position
            Vector2D agentPos = agent.getPos()
                    .add(agent.getSpeed().add(agent.getMoveSpeed()).scalarMultiply(timeS));
            if (agent.getCapabilities().canActivate() && isAgentPushing(can, agent, agentPos)) {
                pushCan(can, agent, agentPos, timeS);
            }
        }
        // push with other cans
        Rectangle2D bounds = can.getSizeBounds().move(can.getPos());
        for (Entity e2 : world.getEntityMap().getEntities(EntityType.PLATFORM)) {
            if ((e2.getSecondType() == PlatformType.CAN)) {
                Rectangle2D bounds2 = e2.getSizeBounds().move(e2.getPos());
                if (intersectFromSide(bounds, bounds2)) {
                    separateCans(can, e2);
                }
            }
        }

        // fall
        EntityObserver posSensor = new EntityObserver(can, world);
        EntityMover canMover = new EntityMover(world);
        double spdX = can.getSpeed().getX();
        boolean onGround = posSensor.isOnGround();
        if (!onGround) {
            double spdY = can.getSpeed().getY() + canFallG * timeS;
            spdY = FastMath.min(spdY, canMaxSpeed);
            can.setSpeed(new Vector2D(spdX, spdY));
            canMover.moveWithStepables(can, can.getSpeed().scalarMultiply(timeS), onGround, posSensor);
        } else {
            can.setSpeed(new Vector2D(spdX, 0));
        }
        // clip
        canMover.doClipping(can);
    }

    /**
     * If the two bounds intersect and and are side by side
     */
    private boolean intersectFromSide(Rectangle2D b1, Rectangle2D b2) {
        return (b1.getX() < b2.getX2()) && (b1.getX2() > b2.getX())
                && (FastMath.abs(b1.getY() - b2.getY()) < b1.getHeight() / 2.0);
    }

    /**
     * Move the two cans from each other in x
     */
    private void separateCans(MovableCan can, Entity e2) {
        double dist = can.getPos().getX() - e2.getPos().getX();
        Vector2D move = new Vector2D(dist / 2.0, 0);
        can.setPos(can.getPos().add(move));
        e2.setPos(e2.getPos().subtract(move));
    }

    /**
     * Push the can by the agent and move items on it.
     */
    private void pushCan(MovableCan can, AgentEntity agent, Vector2D agentPos, double timeS) {
        // get entities standing on it
        List<Entity> carying = new EntitiesFinder(worldHolder.getWorld()).findEntitiesToCarry(can);

        // move
        Vector2D oldPos = can.getPos();
        if (oldPos.getX() < agentPos.getX()) {
            pushLeft(can, agent, agentPos);
        } else {
            pushRight(can, agent, agentPos);
        }

        // move entities on it
        Vector2D speed = can.getPos().subtract(oldPos);
        for (Entity e2 : carying) {
            e2.setPos(e2.getPos().add(speed));
        }
    }

    private void pushLeft(MovableCan can, AgentEntity agent, Vector2D agentPos) {
        double newX = agent.getSizeBounds().getX() + agentPos.getX() - can.getSizeBounds().getX2();
        can.setPos(new Vector2D(newX, can.getPos().getY()));
    }

    private void pushRight(MovableCan can, AgentEntity agent, Vector2D agentPos) {
        double newX = agent.getSizeBounds().getX2() + agentPos.getX() - can.getSizeBounds().getX();
        can.setPos(new Vector2D(newX, can.getPos().getY()));
    }

    /**
     * If the agent is touching the can from sides and touching can's y center
     */
    private boolean isAgentPushing(MovableCan can, AgentEntity agent, Vector2D agentPos) {
        Rectangle2D canBounds = can.getSizeBounds().move(can.getPos());
        Rectangle2D agentBounds = agent.getSizeBounds().move(agentPos);
        return (agentBounds.getX() < canBounds.getX2()) && (agentBounds.getX2() > canBounds.getX())
                && (agentBounds.getY() <= can.getPos().getY())
                && (agentBounds.getY2() >= can.getPos().getY());
    }

    public double getCanFallG() {
        return canFallG;
    }

    @Required
    public void setCanFallG(double canFallG) {
        this.canFallG = canFallG;
    }

    public double getCanMaxSpeed() {
        return canMaxSpeed;
    }

    @Required
    public void setCanMaxSpeed(double canMaxSpeed) {
        this.canMaxSpeed = canMaxSpeed;
    }

}

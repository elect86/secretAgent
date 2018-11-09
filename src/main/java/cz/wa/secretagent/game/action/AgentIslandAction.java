//package cz.wa.secretagent.game.action;
//
//import org.apache.commons.lang.Validate;
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.entity.direction.EntityXDirection;
//import cz.wa.secretagent.world.entity.direction.EntityYDirection;
//import secretAgent.game.action.AgentAction;
//import secretAgent.game.action.AgentActivateAction;
//
///**
// * Action to control an agent on island map.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentIslandAction extends AgentAction<HumanAgent> {
//
//    public AgentActivateAction activateAction;
//
//    @Override
//    public void init() {
//        activateAction = getActionFactory().getAction(AgentActivateAction.class);
//    }
//
//    public void moveX(EntityXDirection dir) {
//        Validate.notNull(dir, "dir is null");
//        HumanAgent agent = getAgent();
//        if (agent.isControlable()) {
//            double maxSpeed = agent.getCapabilities().getMaxSpeed();
//            Vector2D addSpeed = dir.getVector().scalarMultiply(maxSpeed);
//            agent.setMoveSpeed(new Vector2D(0, agent.getMoveSpeed().getY()).add(addSpeed));
//            if (dir != EntityXDirection.NONE) {
//                agent.setDirection(dir);
//            }
//        }
//    }
//
//    public void moveY(EntityYDirection dir) {
//        Validate.notNull(dir, "dir is null");
//        HumanAgent agent = getAgent();
//        if (agent.isControlable()) {
//            double maxSpeed = agent.getCapabilities().getMaxSpeed();
//            agent.setMoveSpeed(new Vector2D(agent.getMoveSpeed().getX(), 0).add(dir.getVector()
//                    .scalarMultiply(maxSpeed)));
//        }
//    }
//
//    public void activate(boolean b) {
//        if (!b) {
//            activateAction.stopUsing();
//            return;
//        }
//        HumanAgent agent = getAgent();
//        if (agent.isControlable() && agent.getCapabilities().canActivate()) {
//            Entity usable = agent.getEntityToUse();
//            if (usable != null) {
//                activateAction.useEntity(usable);
//            }
//        }
//    }
//
//}

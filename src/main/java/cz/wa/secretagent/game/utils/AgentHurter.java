package cz.wa.secretagent.game.utils;

import cz.wa.secretagent.world.entity.agent.AgentAction;
import cz.wa.secretagent.world.entity.agent.AgentEntity;
import cz.wa.secretagent.world.entity.agent.AgentType;
import cz.wa.secretagent.world.entity.agent.HumanAgent;
import cz.wa.secretagent.world.entity.agent.inventory.AgentInventory;
import cz.wa.secretagent.world.entity.item.ItemEntity;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import secretAgent.world.SamWorld;
import secretAgent.world.entity.EntityYDirection;

import java.util.ArrayList;

/**
 * Class used to hurt (or heal) agents. 
 * 
 * @author Ondrej Milenovsky
 */
public class AgentHurter {
    private final SamWorld world;
    private final AgentEntity agent;

    public AgentHurter(SamWorld world, AgentEntity agent) {
        super();
        this.world = world;
        this.agent = agent;
    }

    /**
     * Hit agent with the projectile. If the agent's health < 0, it will kill him.
     * @param projectile
     */
    public void hit(ProjectileEntity projectile) {
        hurt(projectile.getDamage());
    }

    public void hurt(double damage) {
        double health = agent.getHealth() - damage;
        // TODO bleed
        if (health < 0) {
            agent.setHealth(0);
            agent.setCurrentAction(AgentAction.DEATH);
            agent.setFiring(false);
            agent.setMoveSpeed(Vector2D.ZERO);
            if (agent.getSecondType() == AgentType.HUMAN) {
                HumanAgent human = (HumanAgent) agent;
                human.setJumping(false);
                human.setAiming(EntityYDirection.NONE);
                human.setWeapon(null);
                // drop items
                // TODO drop weapons, moving items
                AgentInventory inventory = human.getInventory();
                for (ItemEntity item : new ArrayList<ItemEntity>(inventory.getItems(ItemType.JUNK))) {
                    item.setPos(human.getPos());
                    inventory.removeItem(item);
                    world.getEntityMap().addEntity(item);
                }
            }
        } else {
            agent.setHealth(health);
        }
    }

}

package cz.wa.secretagent.io.map.orig.generator.entity.agent;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import cz.wa.secretagent.world.entity.agent.capabilities.AgentCapabilities;
import cz.wa.secretagent.world.entity.agent.capabilities.AgentCapabilitiesTmp;
import cz.wa.secretagent.world.entity.agent.capabilities.InventoryLimits;
import cz.wa.secretagent.world.entity.item.ItemType;
import cz.wa.secretagent.world.weapon.Weapon;

/**
 * Creates weapons and capabilities for enemy human agents. 
 * 
 * @author Ondrej Milenovsky
 */
public class EnemyHumanPropertiesCreator implements Serializable {
    private static final long serialVersionUID = 5141884147037917924L;

    /**
     * Creates properties for an enemy agent.
     * @param type agent type
     * @return new properties
     */
    public EnemyHumanProperties createProperties(EnemyHumanType type) {
        // TODO make enemies much more evil
        AgentCapabilitiesTmp capabilities = new AgentCapabilitiesTmp();

        InventoryLimits inventoryLimits = new InventoryLimits();
        inventoryLimits.getPicksItems().add(ItemType.JUNK);

        capabilities.setInventoryLimits(inventoryLimits);
        capabilities.setCanAim(true);
        if (type == EnemyHumanType.WHITE_GUY) {
            capabilities.setMaxHealth(100);
            capabilities.setMaxSpeed(20);
        } else if (type == EnemyHumanType.RED_GUY) {
            capabilities.setMaxHealth(200);
            capabilities.setMaxSpeed(40);
        } else if (type == EnemyHumanType.NINJA_GUY) {
            capabilities.setMaxHealth(300);
            capabilities.setMaxSpeed(120);
            capabilities.setJumpStrength(200);
            capabilities.setJumpTimeS(0.1);
        } else if (type == EnemyHumanType.BLUE_GUY) {
            capabilities.setMaxHealth(400);
            capabilities.setMaxSpeed(80);
        } else if (type == EnemyHumanType.GRAY_GUY) {
            capabilities.setMaxHealth(500);
            capabilities.setMaxSpeed(100);
            capabilities.setJumpStrength(120);
            capabilities.setJumpTimeS(0.15);
        } else {
            capabilities.setMaxHealth(1);
        }
        List<Weapon> weapons = Collections.emptyList();
        return new EnemyHumanProperties(new AgentCapabilities(capabilities), weapons);
    }
}

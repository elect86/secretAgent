package cz.wa.secretagent.game.simulator.entity.item;

import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.world.entity.item.ItemEntity;

/**
 * Simulates an item (item can only fall).
 * 
 * @author Ondrej Milenovsky
 */
public class ItemEntitySimulator extends AbstractEntitySimulator<ItemEntity> {

    private static final long serialVersionUID = 7027349662870740521L;

    @Override
    public boolean move(ItemEntity entity, double timeS) {
        // TODO Auto-generated method stub
        return true;
    }

}

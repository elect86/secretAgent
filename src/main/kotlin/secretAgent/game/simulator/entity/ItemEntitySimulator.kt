package secretAgent.game.simulator.entity

import secretAgent.world.entity.ItemEntity

/**
 * Simulates an item (item can only fall).
 *
 * @author Ondrej Milenovsky
 */
class ItemEntitySimulator : AbstractEntitySimulator<ItemEntity>() {

    override fun move(entity: ItemEntity, timeS: Double): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    companion object {
        private const val serialVersionUID = 7027349662870740521L
    }
}
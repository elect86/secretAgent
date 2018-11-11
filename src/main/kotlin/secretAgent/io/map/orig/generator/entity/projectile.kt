package secretAgent.io.map.orig.generator.entity

import cz.wa.secretagent.world.entity.projectile.LevelLaserProjectile
import cz.wa.secretagent.world.entity.projectile.ProjectileEntity
import cz.wa.secretagent.world.entity.projectile.ProjectileType
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import secretAgent.view.renderer.TileId
import secretAgent.world.ObjectModel

/**
 * Creates level laser entity that will be replaced by laser.
 *
 * @author Ondrej Milenovsky
 */
class LevelLaserEntityCreator : EntityCreator<LevelLaserProjectile> {

    override fun createEntity(args: ArrayList<String>, pos: Vector2D, tileId: TileId, model: ObjectModel): LevelLaserProjectile =
            LevelLaserProjectile(model, pos)

    companion object {
        private const val serialVersionUID = 972352522358935169L
    }
}

/**
 * Creates projectile entities.
 *
 * @author Ondrej Milenovsky
 */
class ProjectileEntityCreator : TypeEntityCreator<ProjectileEntity>() {

    override fun getEnum(arg0: String): Any = ProjectileType.valueOf(arg0)

    companion object {
        private const val serialVersionUID = -1947151859836607795L
    }
}
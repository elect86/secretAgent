package secretAgent.utils

import cz.wa.secretagent.utils.raycaster.RayHit
import cz.wa.wautils.math.Vector2I
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D

/**
 * Ray casting in a grid.
 *
 * @author Ondrej Milenovsky
 */
abstract class RayCaster protected constructor(private val startPos: Vector2D,
                                               private val dir: Vector2D,
                                               private val maxDist: Double) {
    /**
     * Cast the ray.
     * @return tile coords and exact hit position or null if reached max distance
     */
    open fun castRay(): RayHit? {
        val rayPosX = startPos.x
        val rayPosY = startPos.y
        val rayDirX = dir.x
        val rayDirY = dir.y
        // which box of the map we're in
        var mapX = rayPosX.toInt()
        var mapY = rayPosY.toInt()

        // length of ray from current position to next x or y-side
        var sideDistX: Double
        var sideDistY: Double

        // length of ray from one x or y-side to next x or y-side
        val deltaDistX = Math.sqrt(1 + rayDirY * rayDirY / (rayDirX * rayDirX))
        val deltaDistY = Math.sqrt(1 + rayDirX * rayDirX / (rayDirY * rayDirY))

        // what direction to step in x or y-direction (either +1 or -1)
        val stepX: Int
        val stepY: Int

        // calculate step and initial sideDist
        if (rayDirX < 0) {
            stepX = -1
            sideDistX = (rayPosX - mapX) * deltaDistX
        } else {
            stepX = 1
            sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX
        }
        if (rayDirY < 0) {
            stepY = -1
            sideDistY = (rayPosY - mapY) * deltaDistY
        } else {
            stepY = 1
            sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY
        }

        var sideX: Boolean
        // perform DDA
        while (true) {
            // jump to next map square in x or in y
            if (sideDistX < sideDistY) {
                sideDistX += deltaDistX
                mapX += stepX
                sideX = true
            } else {
                sideDistY += deltaDistY
                mapY += stepY
                sideX = false
            }

            // hit position
            val pos2x: Double
            val pos2y: Double
            if (sideX) {
                pos2x = (mapX + (1 - stepX) / 2).toDouble()
                pos2y = rayPosY + (pos2x - rayPosX) / rayDirX * rayDirY
            } else {
                pos2y = (mapY + (1 - stepY) / 2).toDouble()
                pos2x = rayPosX + (pos2y - rayPosY) / rayDirY * rayDirX
            }
            val pos2 = Vector2D(pos2x, pos2y)
            // check max distance
            if (startPos.distance(pos2) > maxDist)
                return null

            //Check if ray has hit a wall
            val mapPos = Vector2I(mapX, mapY)
            if (isWall(mapPos))
                return RayHit(mapPos, pos2)
        }
    }

    /**
     * @param i
     * @return true if there is wall at the position
     */
    protected abstract fun isWall(i: Vector2I): Boolean
}

/**
 * Class returned by raycaster. Holds hit map tile and hit point.
 *
 * @author Ondrej Milenovsky
 */
class RayHit(val mapPos: Vector2I, val hitPos: Vector2D)

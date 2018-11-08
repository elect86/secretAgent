package cz.wa.secretagent.utils.raycaster;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import cz.wa.wautils.math.Vector2I;

/**
 * Ray casting in a grid. 
 * 
 * @author Ondrej Milenovsky
 */
public abstract class RayCaster {

    private final Vector2D startPos;
    private final Vector2D dir;
    private final double maxDist;

    protected RayCaster(Vector2D startPos, Vector2D dir, double maxDist) {
        this.startPos = startPos;
        this.dir = dir;
        this.maxDist = maxDist;
    }

    /**
     * Cast the ray.
     * @return tile coords and exact hit position or null if reached max distance
     */
    public RayHit castRay() {
        double rayPosX = startPos.getX();
        double rayPosY = startPos.getY();
        double rayDirX = dir.getX();
        double rayDirY = dir.getY();
        // which box of the map we're in  
        int mapX = (int) rayPosX;
        int mapY = (int) rayPosY;

        // length of ray from current position to next x or y-side
        double sideDistX;
        double sideDistY;

        // length of ray from one x or y-side to next x or y-side
        double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
        double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));

        // what direction to step in x or y-direction (either +1 or -1)
        int stepX;
        int stepY;

        // calculate step and initial sideDist
        if (rayDirX < 0) {
            stepX = -1;
            sideDistX = (rayPosX - mapX) * deltaDistX;
        } else {
            stepX = 1;
            sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
        }
        if (rayDirY < 0) {
            stepY = -1;
            sideDistY = (rayPosY - mapY) * deltaDistY;
        } else {
            stepY = 1;
            sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
        }

        boolean sideX;
        // perform DDA
        while (true) {
            // jump to next map square in x or in y
            if (sideDistX < sideDistY) {
                sideDistX += deltaDistX;
                mapX += stepX;
                sideX = true;
            } else {
                sideDistY += deltaDistY;
                mapY += stepY;
                sideX = false;
            }

            // hit position
            double pos2x;
            double pos2y;
            if (sideX) {
                pos2x = mapX + (1 - stepX) / 2;
                pos2y = rayPosY + (pos2x - rayPosX) / rayDirX * rayDirY;
            } else {
                pos2y = mapY + (1 - stepY) / 2;
                pos2x = rayPosX + (pos2y - rayPosY) / rayDirY * rayDirX;
            }
            Vector2D pos2 = new Vector2D(pos2x, pos2y);
            // check max distance
            if (startPos.distance(pos2) > maxDist) {
                return null;
            }

            //Check if ray has hit a wall
            Vector2I mapPos = new Vector2I(mapX, mapY);
            if (isWall(mapPos)) {
                return new RayHit(mapPos, pos2);
            }
        }
    }

    /**
     * @param i
     * @return true if there is wall at the position
     */
    protected abstract boolean isWall(Vector2I i);

}

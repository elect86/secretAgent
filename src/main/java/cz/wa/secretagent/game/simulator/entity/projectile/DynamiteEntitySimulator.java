package cz.wa.secretagent.game.simulator.entity.projectile;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.simulator.entity.AbstractEntitySimulator;
import cz.wa.secretagent.game.starter.MapStarter;
import cz.wa.secretagent.game.utils.EntityObserver;
import cz.wa.secretagent.game.utils.TileWithPosition;
import cz.wa.secretagent.io.SAMIO;
import cz.wa.secretagent.view.TileId;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.ObjectModel;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;
import cz.wa.secretagent.world.entity.explosion.Explosion;
import cz.wa.secretagent.world.entity.projectile.DynamiteProjectile;
import cz.wa.secretagent.world.entity.usable.ExitDoorUsable;
import cz.wa.secretagent.world.entity.usable.ExitUsable;
import cz.wa.secretagent.world.entity.usable.UsableType;
import cz.wa.secretagent.world.map.LevelMap;
import cz.wa.secretagent.world.map.StoredTile;
import cz.wa.secretagent.world.map.Tile;
import cz.wa.wautils.math.Vector2I;
import secretAgent.game.ProjectileFactory;

/**
 * Simulates dynamite. At first the dynamite burns, then explodes.
 * Opens all exits near the dynamite, also creates explosion to hurt agents. 
 * 
 * @author Ondrej Milenovsky
 */
public class DynamiteEntitySimulator extends AbstractEntitySimulator<DynamiteProjectile> {

    private static final long serialVersionUID = 6710267400479925390L;

    private MapStarter mapStarter;
    private SAMIO samIO;
    private ProjectileFactory projectileFactory;

    private double explosionDamage;
    private double explosionRadius;
    private double explosionDurationS;
    private String explosionModel;

    @Override
    public boolean move(DynamiteProjectile entity, double timeS) {
        entity.addTime(timeS);
        if (entity.getRemainingTimeS() <= 0) {
            worldHolder.getWorld().getEntityMap().removeEntity(entity);
            ExitDoorUsable exitDoor = replaceExit();
            replaceTiles(exitDoor);
            explode(entity.getPos());
        }
        return true;
    }

    private void explode(Vector2D pos) {
        Explosion explosion = projectileFactory.createExplosion(pos, explosionModel, explosionRadius,
                explosionDamage, explosionDurationS);
        worldHolder.getWorld().getEntityMap().addEntity(explosion);
    }

    private void replaceTiles(ExitDoorUsable exitDoor) {
        Map<TileId, Tile> replaceTiles = exitDoor.getReplaceTiles();
        LevelMap map = worldHolder.getWorld().getLevelMap();
        for (TileWithPosition sensing : new EntityObserver(exitDoor, worldHolder.getWorld())
                .get9TilesAround(replaceTiles.keySet())) {
            Vector2I pos = sensing.getPos();
            map.removeTile(pos, sensing.getTile());
            map.addTile(new StoredTile(pos, replaceTiles.get(sensing.getTileId())));
            map.updateType(pos);
        }
    }

    /**
     * Replace the closed exit door by open, returns the closed exit door
     */
    private ExitDoorUsable replaceExit() {
        EntityMap entityMap = worldHolder.getWorld().getEntityMap();
        LevelMap levelMap = worldHolder.getWorld().getLevelMap();

        for (Entity usable : new ArrayList<Entity>(entityMap.getEntities(EntityType.USABLE))) {
            // TODO closest
            if (usable.getSecondType() == UsableType.EXIT_DOOR) {
                ExitDoorUsable exitDoor = (ExitDoorUsable) usable;
                // remove the old switch
                entityMap.removeEntity(usable);
                // add exit switch
                ObjectModel model = samIO.getWorldHolder().getGraphicsInfo()
                        .getModel(exitDoor.getOpenModel());
                ExitUsable exit = new ExitUsable(model, usable.getPos());
                entityMap.addEntity(exit);
                Vector2I pos = levelMap.getNearestTilePos(exit.getPos());
                levelMap.updateType(pos);
                return exitDoor;
            }
        }
        return null;
    }

    public MapStarter getMapStarter() {
        return mapStarter;
    }

    @Required
    public void setMapStarter(MapStarter mapStarter) {
        this.mapStarter = mapStarter;
    }

    public SAMIO getSamIO() {
        return samIO;
    }

    @Required
    public void setSamIO(SAMIO samIO) {
        this.samIO = samIO;
    }

    public ProjectileFactory getProjectileFactory() {
        return projectileFactory;
    }

    @Required
    public void setProjectileFactory(ProjectileFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }

    public double getExplosionDamage() {
        return explosionDamage;
    }

    @Required
    public void setExplosionDamage(double explosionDamage) {
        this.explosionDamage = explosionDamage;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    @Required
    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public double getExplosionDurationS() {
        return explosionDurationS;
    }

    @Required
    public void setExplosionDurationS(double explosionDurationS) {
        this.explosionDurationS = explosionDurationS;
    }

    public String getExplosionModel() {
        return explosionModel;
    }

    @Required
    public void setExplosionModel(String explosionModel) {
        this.explosionModel = explosionModel;
    }
}

package cz.wa.secretagent.view.renderer.world;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.player.Camera;
import cz.wa.secretagent.world.EntityMap;
import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityOrder;

/**
 * Renders all entities (items, agents, explosions). 
 * 
 * @author Ondrej Milenovsky
 */
public class EntitiesRenderer extends AbstractWorldRenderer {

    private static final long serialVersionUID = -8370881564278955844L;

    /** Entities drawn between level background and foreground */
    private EntityOrder entityOrder;
    /** Entities drawn over level foreground */
    private EntityOrder entityOverOrder;

    @Override
    public void init() {
        // empty
    }

    @Override
    public void dispose() {
        // empty
    }

    public void drawEntities(EntityMap entityMap, Camera camera) {
        for (Entity entity : entityOrder.getAllEntities(entityMap)) {
            renderModel(entity, camera, entity.getModel(), entity.getPos());
        }
    }

    public void drawOverEntities(EntityMap entityMap, Camera camera) {
        for (Entity entity : entityOverOrder.getAllEntities(entityMap)) {
            renderModel(entity, camera, entity.getModel(), entity.getPos());
        }
    }

    public EntityOrder getEntityOrder() {
        return entityOrder;
    }

    @Required
    public void setEntityOrder(EntityOrder entityOrder) {
        this.entityOrder = entityOrder;
    }

    public EntityOrder getEntityOverOrder() {
        return entityOverOrder;
    }

    @Required
    public void setEntityOverOrder(EntityOrder entityOverOrder) {
        this.entityOverOrder = entityOverOrder;
    }
}

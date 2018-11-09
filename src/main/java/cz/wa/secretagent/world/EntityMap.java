package cz.wa.secretagent.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.wa.secretagent.world.entity.Entity;
import cz.wa.secretagent.world.entity.EntityType;

/**
 * Map holding all entities. Entities are mapped by type.
 * Preserves order of entity types and entities for each type. Does not preserve insertion order for list of all entities.
 * 
 * @author Ondrej Milenovsky
 */
public class EntityMap {

    private Map<EntityType, Set<Entity>> entityMap;
    private int size;

    public EntityMap() {
        entityMap = new LinkedHashMap<EntityType, Set<Entity>>();
        size = 0;
    }

    public boolean containsEntity(Entity entity) {
        EntityType type = entity.getType();
        if (!entityMap.containsKey(type)) {
            return false;
        }
        return entityMap.get(type).contains(entity);
    }

    public void addEntity(Entity entity) {
        EntityType type = entity.getType();
        Set<Entity> entities;
        if (entityMap.containsKey(type)) {
            entities = entityMap.get(type);
        } else {
            entities = new LinkedHashSet<Entity>();
            entityMap.put(type, entities);
        }
        if (entities.add(entity)) {
            size++;
        }
    }

    /**
     * Remove the entity
     * @param entity entity to remove
     * @throws IllegalArgumentException if the entity is not in the map
     */
    public void removeEntity(Entity entity) {
        EntityType type = entity.getType();
        if (!entityMap.containsKey(type)) {
            throw new IllegalArgumentException("The map does not contain entity: " + entity);
        }
        Set<Entity> entities = entityMap.get(type);
        entities.remove(entity);
        if (entities.isEmpty()) {
            if (entityMap.remove(type) != null) {
                size--;
            }
        }
    }

    /**
     * @param type entity type
     * @return unmodifiable set of entities with the specified type
     */
    public Set<Entity> getEntities(EntityType type) {
        if (entityMap.containsKey(type)) {
            return Collections.unmodifiableSet(entityMap.get(type));
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * @return new list of all entities
     */
    public List<Entity> getAllEntities() {
        List<Entity> ret = new ArrayList<Entity>(size);
        for (Set<Entity> set : entityMap.values()) {
            ret.addAll(set);
        }
        return ret;
    }

    /**
     * @return unmodifiable set of all entity types stored in the map
     */
    public Set<EntityType> getAllTypes() {
        return Collections.unmodifiableSet(entityMap.keySet());
    }

    public int getTypeCount() {
        return entityMap.size();
    }

    public int getEntityCount() {
        return size;
    }
}

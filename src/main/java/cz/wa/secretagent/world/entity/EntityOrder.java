//package cz.wa.secretagent.world.entity;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//
//import cz.wa.secretagent.world.EntityMap;
//
///**
// * Order of entity types.
// *
// * @author Ondrej Milenovsky
// */
//public class EntityOrder implements Serializable {
//    private static final long serialVersionUID = -5390144215185157807L;
//
//    private final List<EntityType> order;
//
//    public EntityOrder(List<EntityType> order) {
//        this.order = order;
//    }
//
//    public List<EntityType> getOrder() {
//        return order;
//    }
//
//    /**
//     * @param entityMap entity map
//     * @return new list of all entities with types only from the map sorted by the order,
//     */
//    public List<Entity> getAllEntities(EntityMap entityMap) {
//        List<Entity> ret = new ArrayList<Entity>(entityMap.getEntityCount());
//        // find types not defined by the order
//        Set<EntityType> types = Collections.emptySet();
//        if (entityMap.getTypeCount() > order.size()) {
//            types = new LinkedHashSet<EntityType>(entityMap.getAllTypes());
//            types.removeAll(order);
//        }
//        // then add ordered types
//        for (EntityType type : order) {
//            ret.addAll(entityMap.getEntities(type));
//        }
//        return ret;
//    }
//
//    /**
//     * @param entityMap entity map
//     * @return new list containing only entities with ordered type, ordered by the type
//     */
//    public List<Entity> getOrderedEntities(EntityMap entityMap) {
//        List<Entity> ret = new ArrayList<Entity>(entityMap.getEntityCount());
//        for (EntityType type : order) {
//            ret.addAll(entityMap.getEntities(type));
//        }
//        return ret;
//
//    }
//}

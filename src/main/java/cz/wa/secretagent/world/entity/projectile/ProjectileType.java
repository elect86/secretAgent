//package cz.wa.secretagent.world.entity.projectile;
//
//import secretAgent.world.ObjectModel;
//import secretAgent.world.entity.EntityType2;
//import secretAgent.world.entity.agent.Team;
//
///**
// * Type of projectile
// *
// * @author Ondrej Milenovsky
// */
//public enum ProjectileType implements EntityType2 {
//    /** any bullet that flies single in direction and impacts on agents or walls */
//    BULLET {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            return new BulletProjectile(model, team);
//        }
//    },
//    /** bullet that falls */
//    BULLET_FALL {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            return new BulletFallProjectile(model, team);
//        }
//    },
//    /** same as bullet but on impact creates an explosion */
//    ROCKET {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            return new RocketProjectile(model, team);
//        }
//    },
//    /** grenade with physics, explodes after some time */
//    GRENADE {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    },
//    /** static mine, explodes if any agent steps on it */
//    MINE {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    },
//    /** ball that bounces from the floor */
//    BALL {
//        @Override
//        public ProjectileEntity createEntity(ObjectModel model, Team team) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    },
//    /** dynamite that opens the exit, creates explosion */
//    DYNAMITE,
//    /** security laser, will not get to the game, will be replaced by laser entity */
//    LEVEL_LASER;
//
//    /**
//     * Creates projectile entity with model and team and nothing else.
//     * @param model the model
//     * @param team team of agent who fired it (can be null)
//     * @return new entity
//     * @throws UnsupportedOperationException if the type cannot create entity (DYNAMITE, LEVEL_LASER)
//     */
//    public ProjectileEntity createEntity(ObjectModel model, Team team) {
//        throw new UnsupportedOperationException("This projectile cannot be created from its type.");
//    }
//}

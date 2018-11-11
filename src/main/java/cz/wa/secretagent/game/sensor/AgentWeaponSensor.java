//package cz.wa.secretagent.game.sensor;
//
//import java.util.Collections;
//import java.util.Set;
//
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.weapon.Weapon;
//
///**
// * Sensor about agent's weapons.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentWeaponSensor extends AgentSensor<HumanAgent> {
//
//    AgentWeaponSensor() {
//    }
//
//    @Override
//    protected void init() {
//        // empty
//    }
//
//    /**
//     * @return list of weapons (even without ammo)
//     */
//    public Set<Weapon> getWeapons() {
//        return Collections.unmodifiableSet(getEntity().getInventory().getWeapons());
//    }
//
//    /**
//     * @return active weapon or null
//     */
//    public Weapon getActiveWeapon() {
//        return getEntity().getWeapon();
//    }
//
//}

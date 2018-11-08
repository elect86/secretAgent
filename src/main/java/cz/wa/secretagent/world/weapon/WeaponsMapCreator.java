package cz.wa.secretagent.world.weapon;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Craetes map of weapons per name from collection of weapons. 
 * 
 * @author Ondrej Milenovsky
 */
public class WeaponsMapCreator {

    public static Map<String, Weapon> createMap(Collection<Weapon> weapons) {
        Map<String, Weapon> ret = new LinkedHashMap<String, Weapon>();
        for (Weapon weapon : weapons) {
            ret.put(weapon.getName(), weapon);
        }
        return ret;
    }

    private WeaponsMapCreator() {
    }

}

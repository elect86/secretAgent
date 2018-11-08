package cz.wa.secretagent.world.weapon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List of weapons. 
 * 
 * @author Ondrej Milenovsky
 */
public class WeaponOrder extends ArrayList<Weapon> {

    private static final long serialVersionUID = -4063846334937522852L;

    public WeaponOrder() {
        super();
    }

    public WeaponOrder(Collection<? extends Weapon> c) {
        super(c);
    }

    public WeaponOrder(int initialCapacity) {
        super(initialCapacity);
    }

}

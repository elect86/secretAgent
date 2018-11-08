package cz.wa.secretagent.game;

import java.io.Serializable;

/**
 * Settings for game.
 *
 * @author Ondrej Milenovsky
 */
public class GameSettings implements Serializable {
    private static final long serialVersionUID = -6943322563557755594L;

    public double effectObjects;
    public WeaponEquipPolicy weaponEquipPolicy;
    public boolean confirmDialogs;
}

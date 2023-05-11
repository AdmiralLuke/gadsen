package com.gats.simulation.weapons;

import com.gats.simulation.GameCharacter;

public class WeaponWrapper {

    private WeaponWrapper() {
    }

    /**
     * make it Singelton
     * @return
     */
    public static WeaponWrapper instance() {
            return new WeaponWrapper();
    }

    public void addAmmo(Weapon weapon, int addAmmo) {
        weapon.setAmmo(weapon.getAmmo() + addAmmo);
    }


}

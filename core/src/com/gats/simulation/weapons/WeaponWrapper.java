package com.gats.simulation.weapons;

import com.gats.simulation.GameCharacter;

public class WeaponWrapper {
    public static int instanceCounter = 0;

    private WeaponWrapper() {
        instanceCounter++;
    }

    /**
     * make it Singelton
     * @return
     */
    public static WeaponWrapper instance() {
        if (instanceCounter == 0) {
            return new WeaponWrapper();
        }
        return null;
    }

    public void addAmmo(Weapon weapon, int addAmmo) {
        weapon.setAmmo(weapon.getAmmo() + addAmmo);
    }


}

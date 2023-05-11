package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.action.Action;

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

    public Action shoot(Action head, Weapon weapon, Vector2 dir, float strength, Vector2 pos, GameCharacter character){
     return weapon.shoot(head, dir, strength, pos, character);
    }

}

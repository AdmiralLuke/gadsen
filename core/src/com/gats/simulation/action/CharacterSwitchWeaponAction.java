package com.gats.simulation.action;

import com.gats.simulation.WeaponType;

public class CharacterSwitchWeaponAction extends CharacterAction {

    private final WeaponType wpType;

    public CharacterSwitchWeaponAction(int team, int character, WeaponType wpType) {
        super(team, character, 0);
        this.wpType = wpType;
    }

    public WeaponType getWpType() {
        return wpType;
    }
}

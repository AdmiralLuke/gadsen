package com.gats.simulation;

public class CharacterSwitchWeaponAction extends CharacterAction {

    private WeaponType wpType;

    public CharacterSwitchWeaponAction(int team, int character, WeaponType wpType) {
        super(team, character, 0);
        this.wpType = wpType;
    }
}

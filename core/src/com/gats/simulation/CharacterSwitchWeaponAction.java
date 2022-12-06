package com.gats.simulation;

public class CharacterSwitchWeaponAction extends CharacterAction {

    private Weapon.Type wpType;

    public CharacterSwitchWeaponAction(int team, int character, Weapon.Type wpType) {
        super(team, character, 0);
        this.wpType = wpType;
    }
}

package com.gats.simulation;

public class CharacterSwitchWeaponAction extends CharacterAction {

    private ChristmasWeapon.ChristmasWeaponType wpType;

    public CharacterSwitchWeaponAction(int team, int character, ChristmasWeapon.ChristmasWeaponType wpType) {
        super(team, character, 0);
        this.wpType = wpType;
    }
}

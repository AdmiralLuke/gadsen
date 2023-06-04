package com.gats.simulation.action;

import com.gats.simulation.WeaponType;

public class InventoryAction extends CharacterAction{

    private final WeaponType wpType;
    private final int amount;

    public InventoryAction(int team, int character, WeaponType wpType, int amount) {
        super(0, team, character);
        this.wpType = wpType;
        this.amount = amount;
    }

    public WeaponType getWpType() {
        return wpType;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {

        return "New Amount: " + wpType.toString() + " = " + amount;

    }
}

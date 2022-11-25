package com.gats.simulation;

/**
 * Repräsentiert eine Waffe im Spiel, die durch einen {@link GameCharacter Spielfigur} benutzt werden kann.
 */
abstract class Weapon {

    private final double damage;
    private final double damageLoss;
    private final double projRange;
    private int shoots;
    private final boolean hitThroughBoxes;

    enum Type {
        LONG_RANGE,
        SHORT_RANGE,
        CLOSE_COMBAT,
        BOMB
    }

    private final Type type;

    Weapon(double damage, double damageLoss, double projRange, int shoots, boolean hitThroughBoxes, Type type) {
        this.damage = damage;
        this.damageLoss = damageLoss;
        this.projRange = projRange;
        this.hitThroughBoxes = hitThroughBoxes;
        this.shoots = shoots;
        this.type = type;
    }


    public Type getType() {
        return type;
    }

    public boolean canHitThroughBoxes() {
        return hitThroughBoxes;
    }

    public double getStartDamage() {
        return damage;
    }

    public double getDamageLoss() {
        return damageLoss;
    }

    public double getProjRange() {
        return projRange;
    }

    public int getShootsLeft() {
        return shoots;
    }

    public void shoot() {
        if (shoots <= 0) {
            return;
        }
        shoots--;
    }
}

package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

public class ChristmasWeapon extends Weapon{

    enum ChristmasWeaponType {
        COOKIE,
        SUGAR_CANE
    }

    private ChristmasWeaponType chrType;

    ChristmasWeapon(double damage, double projRange, int shoots, boolean hitThroughBoxes, Type type, Simulation sim, ChristmasWeaponType chrType, GameCharacter character) {
        super(damage, 0, projRange, shoots, hitThroughBoxes, type, sim, character);
        this.chrType = chrType;
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public boolean canHitThroughBoxes() {
        return super.canHitThroughBoxes();
    }

    @Override
    public double getStartDamage() {
        return super.getStartDamage();
    }

    @Override
    public double getDamageLoss() {
        return super.getDamageLoss();
    }

    @Override
    public double getProjRange() {
        return super.getProjRange();
    }

    @Override
    public int getShootsLeft() {
        return super.getShootsLeft();
    }

    public ChristmasWeaponType getChrType() {
        return chrType;
    }

    public void shoot(Vector2 dir, double strength) {
        if (chrType == ChristmasWeaponType.COOKIE) {
            super.shoot(dir, strength,  ProjectileAction.ProjectileType.COOKIE ,Projectile.Type.LINEAR);
        } else {
            super.shoot(dir, strength, ProjectileAction.ProjectileType.CANDY_CANE, Projectile.Type.PARABLE);
        }
    }
}

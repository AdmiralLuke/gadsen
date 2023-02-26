package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * @Weihnachtsaufgabe Waffen für die Weihnachtsaufgabe (Kekse & Zuckerstange)
 */
public class ChristmasWeapon extends Weapon{

    ChristmasWeapon(int damage, double projRange, int shoots, boolean hitThroughBoxes, WeaponType type, Simulation sim, GameCharacter character) {
        super(damage, 0, projRange, shoots, hitThroughBoxes, type, sim, character);
    }

    @Override
    public WeaponType getType() {
        return super.getType();
    }

    @Override
    public boolean canHitThroughBoxes() {
        return super.canHitThroughBoxes();
    }

    @Override
    public int getDamage() {
        return super.getDamage();
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



    public void shoot(Vector2 dir, double strength, Action head) {
        if (this.getType() == WeaponType.COOKIE) {
            super.shoot(dir, strength,  ProjectileAction.ProjectileType.COOKIE ,Projectile.Type.PARABLE, head);
        } else {
            super.shoot(dir, strength, ProjectileAction.ProjectileType.CANDY_CANE, Projectile.Type.LINEAR, head);
        }
    }
}

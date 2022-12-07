package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert eine Waffe im Spiel, die durch einen {@link GameCharacter Spielfigur} benutzt werden kann.
 */
abstract class Weapon {

    private final int damage;
    private final double damageLoss;
    private double projRange;
    private int shoots;
    private final boolean hitThroughBoxes;
    private GameCharacter character;
    private Simulation sim;

    private final WeaponType type;


    Weapon(int damage, double damageLoss, double projRange, int shoots, boolean hitThroughBoxes, WeaponType type, Simulation sim, GameCharacter character) {
        this.damage = damage;
        this.damageLoss = damageLoss;
        this.projRange = projRange;
        this.hitThroughBoxes = hitThroughBoxes;
        this.shoots = shoots;
        this.type = type;
        this.sim = sim;
        this.character = character;
    }


    public WeaponType getType() {
        return type;
    }

    public boolean canHitThroughBoxes() {
        return hitThroughBoxes;
    }

    public int getStartDamage() {
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

    public void shoot(Vector2 dir, double strength) {
        if (this.getType() == Type.COOKIE) {
            this.shoot(dir, strength,  ProjectileAction.ProjectileType.COOKIE ,Projectile.Type.PARABLE);
        } else {
            this.shoot(dir, strength, ProjectileAction.ProjectileType.CANDY_CANE, Projectile.Type.LIN_LASER);
        }
    }

    public void shoot(Vector2 dir, double strength, ProjectileAction.ProjectileType AcType, Projectile.Type type) {
        if (strength > projRange) {
            projRange = strength;
        }
        if (shoots <= 0) {
            return;
        }
        sim.getActionLog().goToNextAction();
        sim.getActionLog().addAction(new CharacterShootAction(character.getTeam(), character.getTeamPos()));
        Projectile proj = new Projectile(damage, projRange, character.getPlayerPos(), dir, type, AcType, sim, character, strength);
        proj.move();
        shoots--;


    }
}

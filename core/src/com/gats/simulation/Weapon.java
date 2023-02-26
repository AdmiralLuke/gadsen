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

    /**
     * @return Waffen-Typ als enum
     */
    public WeaponType getType() {
        return type;
    }

    protected boolean canHitThroughBoxes() {
        return hitThroughBoxes;
    }

    /**
     * @return Schaden, den diese Waffe beim Treffen verursacht.
     */
    public int getDamage() {
        return damage;
    }

    protected double getDamageLoss() {
        return damageLoss;
    }

    protected double getProjRange() {
        return projRange;
    }

    /**
     * @return Anzahl der Schüsse, die mit dieser Waffe verbleiben
     */
    public int getShootsLeft() {
        return shoots;
    }

    protected void shoot(Vector2 dir, double strength, Action head) {
        if (this.getType() == WeaponType.COOKIE) {
            this.shoot(dir, strength,  ProjectileAction.ProjectileType.COOKIE ,Projectile.Type.PARABLE, head);
        } else {
            this.shoot(dir, strength, ProjectileAction.ProjectileType.CANDY_CANE, Projectile.Type.LINEAR, head);
        }
    }

    protected void shoot(Vector2 dir, double strength, ProjectileAction.ProjectileType AcType, Projectile.Type type, Action head) {
        if (strength > projRange) {
            projRange = strength;
        }
        if (shoots <= 0) {
            return;
        }
        CharacterShootAction shootAction = new CharacterShootAction(character.getTeam(), character.getTeamPos());
        head.addChild(shootAction);
        Projectile proj = new Projectile(damage, projRange, character.getPlayerPos(), dir, type, AcType, sim, character, strength);
        proj.move(shootAction);
        if (this.type != WeaponType.NOT_SELECTED) shoots--;
    }
}

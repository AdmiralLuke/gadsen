package com.gats.simulation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

/**
 * Speichert die Eigenschaften und legt die Verhaltensweise eines Projektils fest,
 * welches durch das Benutzen einer {@link Weapon Waffe} mittels eines {@link GameCharacter Spielfigur} erzeugt wird
 */
public class Projectile {

    private double damage;
    private double damageLoss;
    private double range;
    private double livingTime;
    private Vector2 pos;
    private Vector2 dir;
    private ProjectileAction.ProjectileType projectileType;
    private Simulation sim;
    private GameCharacter character;

    private Path path;


    // ToDo: find the best settings for gravity
    private final static float g = 9.81f;

    /**
     * Definiert den Projektil-Flug-Typ
     */
    enum Type {
        LINEAR,
        PARABLE
    }

    private Type type;

    /**
     * Erstellt ein Projektil
     * @param damage Ursprungsschaden des Projektils
     * @param damageLoss Abnahme des Schadens über die Zeit
     * @param range maximale Reichweite des Projektils
     * @param pos aktuelle Position
     * @param dir aktuelle Richtung
     * @param type Schusslinien-Typ
     * @param prType (für Animation) welche Art von Projektil
     * @param sim aktuelle Simulation
     * @param character Charakter der Projektil verschossen hat
     */
    Projectile(double damage, double damageLoss, double range, Vector2 pos, Vector2 dir, Type type, ProjectileAction.ProjectileType prType, Simulation sim, GameCharacter character) {
        this.damage = damage;
        this.damageLoss = damageLoss;
        this.range = range;
        this.livingTime = 0;
        this.pos = pos;
        this.dir = dir;
        this.type = type;
        this.projectileType = prType;
        this.sim = sim;
        this.character = character;
    }

    /**
     * Konstruktor ohne DamageLoss
     */
    Projectile(double damage, double range, Vector2 pos, Vector2 dir, Type type, ProjectileAction.ProjectileType prType, Simulation sim, GameCharacter character) {
        new Projectile(damage, 0, range, pos, dir, type, prType, sim, character);
    }

    /**
     * berechnet die Flugbahn des Projektils, erkennt Kollisionen mit Spieler oder Tiles
     */
    private void move() {
        Path path = null; // ToDo:
        if (this.type == Type.LINEAR) {
            // moveLog.addAction();
        } else if (this.type == Type.PARABLE) {
            if (livingTime < range) {
                Vector2 oldPos = pos.cpy();
                pos.x = (float)(dir.y * livingTime);
                dir.y = (float)(dir.y - (g * livingTime));
                pos.y = (float)((dir.y * livingTime) - ((g / 2) * livingTime * livingTime));
                livingTime++;

            }
        }
        float duration = 1000; //ToDo calculate duration
        sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, duration));
        Vector2 posCharBef = this.character.getPlayerPos();
        character.move(-1);
        sim.getActionLog().addAction(new CharacterMoveAction(posCharBef, character.getPlayerPos(), character.getTeam(), character.getTeamPos(), 0));
        sim.getActionLog().goToNextAction();
        // ToDo: check for hit
    }


}

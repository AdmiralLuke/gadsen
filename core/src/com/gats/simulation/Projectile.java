package com.gats.simulation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

/**
 * Speichert die Eigenschaften und legt die Verhaltensweise eines Projektils fest,
 * welches durch das Benutzen einer {@link Weapon Waffe} mittels eines {@link GameCharacter Spielfigur} erzeugt wird
 */
public class Projectile {

    private int damage;
    private double damageLoss;
    private double range;
    private double livingTime;
    private Vector2 pos;
    private Vector2 dir;
    private ProjectileAction.ProjectileType projectileType;
    private Simulation sim;
    private GameCharacter character;
    private double strength;

    private Path path;


    // ToDo: find the best settings for gravity
    private final static float g = 9.81f;

    /**
     * Definiert den Projektil-Flug-Typ
     */
    enum Type {
        LINEAR,
        PARABLE,
        LIN_LASER
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
     * @param strength Stärke zwischen 0 und 1
     */
    Projectile(int damage, double damageLoss, double range, Vector2 pos, Vector2 dir, Type type, ProjectileAction.ProjectileType prType, Simulation sim, GameCharacter character, double strength) {
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
        this.strength = strength;
    }

    /**
     * Konstruktor ohne DamageLoss
     */
    Projectile(int damage, double range, Vector2 pos, Vector2 dir, Type type, ProjectileAction.ProjectileType prType, Simulation sim, GameCharacter character, double strength) {
        this.damage = damage;
        this.damageLoss = 0;
        this.range = range;
        this.pos = pos;
        this.dir = dir;
        this.type = type;
        this.projectileType = prType;
        this.sim = sim;
        this.character = character;
        this.strength = strength;
        // new Projectile(damage, 0, range, pos, dir, type, prType, sim, character, strength);
    }

    /**
     * berechnet die Flugbahn des Projektils, erkennt Kollisionen mit Spieler oder Tiles
     */
    void move() {
        this.dir.nor();
        Vector2 startPos = this.pos.cpy();
        if (this.type == Type.LINEAR || this.type == Type.LIN_LASER) {
            while (livingTime < range) {
                if (this.pos.x / 16 >= this.sim.getState().getBoardSizeX() ||this.pos.y / 16 >= this.sim.getState().getBoardSizeY()
                    || this.pos.x / 16<= 0 ||this.pos.y / 16 <= 0) {
                    this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos) : new LaserPath(startPos, pos);
                    sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float)(this.pos.cpy().sub(startPos).len() / 0.0001)));
                    return;
                }
                if (this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                    this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos) : new LaserPath(startPos, pos);
                    sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float)(this.pos.cpy().sub(startPos).len() / 0.0001)));
                    this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16).destroyTile();
                    return;
                }
                if (!((int)this.pos.x == startPos.x) && !((int)this.pos.y == startPos.y)) {
                    for (GameCharacter[] characters : this.sim.getState().getTeams()) {
                        for (GameCharacter character : characters) {
                            if ((int) character.getPlayerPos().x == (int) this.pos.x && (int) character.getPlayerPos().y == (int) this.pos.y) {
                                this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos) : new LaserPath(startPos, pos);
                                sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float) (this.pos.cpy().sub(startPos).len() / 0.0001)));
                                sim.getActionLog().goToNextAction();
                                int oldHealth = character.getHealth();
                                character.setHealth(oldHealth - damage);
                                sim.getActionLog().addAction(new CharacterHitAction(character.getTeam(), character.getTeamPos(), oldHealth, character.getHealth()));
                                sim.getActionLog().goToNextAction();
                                return;
                            }
                        }
                    }
                }
                this.pos.add(dir);
                this.livingTime += 0.1;
            }
            this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos) : new LaserPath(startPos, pos);
            sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float)(this.pos.cpy().sub(startPos).len() / 0.0001)));
            return;
        } else if (this.type == Type.PARABLE) {
            Vector2 s = pos.cpy();
            Vector2 v = dir.cpy();
            v.set((float)(v.x * strength * 0.01), (float)(v.y * strength * 0.01));
            this.path = new ParablePath(s, v);
            while (livingTime < range) {
                if (this.pos.x / 16 >= this.sim.getState().getBoardSizeX() ||this.pos.y / 16 >= this.sim.getState().getBoardSizeY()
                        || this.pos.x <= 0 ||this.pos.y <= 0) {
                    sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float)(this.pos.cpy().sub(startPos).len() / 0.0001)));
                    return;
                }
                if (this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                    sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float)(this.pos.cpy().sub(startPos).len() / 0.0001)));
                    this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16).destroyTile();
                    return;
                }
                if (!((int)this.pos.x == startPos.x) && !((int)this.pos.y == startPos.y)) {
                    for (GameCharacter[] characters : this.sim.getState().getTeams()) {
                        for (GameCharacter character : characters) {
                            if ((int) character.getPlayerPos().x == (int) this.pos.x && (int) character.getPlayerPos().y == (int) this.pos.y) {
                                sim.getActionLog().addAction(new ProjectileAction(this.path, this.projectileType, (float) (this.pos.cpy().sub(startPos).len() / 0.0001)));
                                sim.getActionLog().goToNextAction();
                                int oldHealth = character.getHealth();
                                character.setHealth(oldHealth - damage);
                                sim.getActionLog().addAction(new CharacterHitAction(character.getTeam(), character.getTeamPos(), oldHealth, character.getHealth()));
                                sim.getActionLog().goToNextAction();
                                return;
                            }
                        }
                    }
                }
                pos.x += 1;
                pos.y = (v.y * ((-s.x + pos.x) / v.x)) - (g/2) * (float)Math.pow(((-s.x + pos.x) / v.x), 2) + s.y;
            }
        }

        Vector2 posCharBef = this.character.getPlayerPos();
        character.move(-1);
        sim.getActionLog().addAction(new CharacterMoveAction(posCharBef, character.getPlayerPos(), character.getTeam(), character.getTeamPos(), 0));
        sim.getActionLog().goToNextAction();

    }


}

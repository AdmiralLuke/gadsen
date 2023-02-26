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


    private final static float g = 9.81f * 8;

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
        this.pos.y += 7;
        this.pos.x += 7;
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
    Action move(Action head) {
        this.dir.nor();
        Vector2 startPos = this.pos.cpy();
        if (this.type == Type.LINEAR || this.type == Type.LIN_LASER) {
            while (livingTime < range) {
                if (this.pos.x / 16 >= this.sim.getState().getBoardSizeX() || this.pos.y / 16 >= this.sim.getState().getBoardSizeY()
                    || this.pos.x / 16 <= 0 ||this.pos.y / 16 <= 0) {
                    this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos, 0.1f) : new LaserPath(startPos, pos);

                    Action projectileAction = new ProjectileAction(this.path, this.projectileType, (this.pos.cpy().sub(startPos).len()), this.pos);
                    head.addChild(projectileAction);
                    return projectileAction;
                }
                if (this.sim.getState().getTile((int)((pos.x)/ 16), (int)((pos.y) / 16)) != null) {
                    this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos, 0.1f) : new LaserPath(startPos, pos);
                    ProjectileAction action = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
                    head.addChild(action);
                    this.sim.getState().getTile((int)(pos.x / 16), (int)(pos.y / 16)).onDestroy(action);
                    return action; // ToDo: maybe revisit this and return action from onDestroy
                }
                if (!(this.pos.x == startPos.x && this.pos.y == startPos.y)) {
                    for (GameCharacter[] characters : this.sim.getState().getTeams()) {
                        for (GameCharacter character : characters) {
                            if (character == null || this.character == character) {
                                continue;
                            }
                            if ((int)(character.getPlayerPos().x / 16)  == (int)(this.pos.x / 16)  && (int)(character.getPlayerPos().y / 16)  == (int)(this.pos.y / 16)) {
                                this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos, 0.1f) : new LaserPath(startPos, pos);
                                Action projectileAction = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
                                head.addChild(projectileAction);
                                int oldHealth = character.getHealth();
                                return character.setHealth(oldHealth - damage, projectileAction);
                            }
                        }
                    }
                }
                this.pos.add(dir);
                this.livingTime += 0.1;
            }
            this.path = this.type == Type.LINEAR ? new LinearPath(startPos, pos, 0.1f) : new LaserPath(startPos, pos);
            ProjectileAction projectileAction = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
            head.addChild(projectileAction);
            return projectileAction;
        } else if (this.type == Type.PARABLE) {
            Vector2 s = pos.cpy();
            Vector2 v = dir.cpy();
            v.set((float)(v.x * strength) * 400, (float)(v.y * strength) * 400);
            this.path = new ParablePath(s, v);
            while (livingTime < range) {
//                System.out.println("Pos: " + pos.x + ", " + pos.y);
                if (this.pos.x / 16 >= this.sim.getState().getBoardSizeX() ||this.pos.y / 16 >= this.sim.getState().getBoardSizeY()
                        || this.pos.x <= 0 ||this.pos.y <= 0) {
                    ProjectileAction projectileAction = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
                    head.addChild(projectileAction);
                    return projectileAction;
                }
                if (!((int)this.pos.x == startPos.x) && !((int)this.pos.y == startPos.y) && this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16) != null) {
                    ProjectileAction projectileAction = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
                    head.addChild(projectileAction);
                    this.sim.getState().getTile((int)pos.x / 16, (int)pos.y / 16).onDestroy(projectileAction);
                    return projectileAction;  // ToDo: maybe revisit this and return action from onDestroy
                }
                if (!((int)this.pos.x == startPos.x) && !((int)this.pos.y == startPos.y)) {
                    for (GameCharacter[] characters : this.sim.getState().getTeams()) {
                        for (GameCharacter character : characters) {
                            if (character == null || this.character == character) {
                                continue;
                            }
                            if ((int)(character.getPlayerPos().x / 16) == (int)(this.pos.x / 16) && (int)(character.getPlayerPos().y / 16) == (int)(this.pos.y / 16)) {
                                Action projectileAction = new ProjectileAction(this.path, this.projectileType, this.pos.cpy().sub(startPos).len(), this.pos);
                                int oldHealth = character.getHealth();
                                head.addChild(projectileAction);
                                return character.setHealth(oldHealth - damage, projectileAction);
                            }
                        }
                    }
                }
                pos.x += dir.x > 0 ? 1 : -1;
                pos.y = ((v.y * ((-s.x + pos.x) / v.x)) - (g/2) * (float)Math.pow(((-s.x + pos.x) / v.x), 2)) + s.y;
            }
        }
        Vector2 posCharBef = this.character.getPlayerPos();
        return character.walk(-1, head); //ToDo consider shooting direction; Implement separate function for recoil

    }


}

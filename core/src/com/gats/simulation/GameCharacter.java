package com.gats.simulation;
import com.badlogic.gdx.Game;
import com.gats.simulation.ActionLog;

import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert eine {@link GameCharacter Spielfigur} auf der Karte
 */
public class GameCharacter {

    private int posX;
    private int posY;
    private int health = 100;
    private int stamina = 100;

    private int team;
    private int teamPos;

    private Weapon[] weapons;

    GameCharacter(int x, int y, GameState state, int team, int teamPos) {
        new GameCharacter(x, y, state);
        this.team = team;
        this.teamPos = teamPos;
    }

    /**
     * spawnt Charakter an einer bestimmten Position
     * ActionLog wird durch Garbage Collection am Ende entfernt
     * @param x X-Koordinate
     * @param y Y-Koordinate
     */
    GameCharacter(int x, int y, GameState state) {
        this.move(x, y, null, state);
    }

    /**
     * Gibt die Lebensanzahl eines Spielers zurück (maximal 100)
     * @return Lebensanzahl des Spielers
     */
    public int getHealth() { return this.health; }
    public int getStamina() { return this.stamina; }
    void setHealth(int newHealth) { this.health = newHealth; }
    void setStamina(int newStamina) { this.stamina = newStamina; }

    /**
     * wählt Waffe n aus dem Inventar aus
     * @param n Waffe die gewählt werden soll
     * @return Waffe die gewählt wurde
     */
    public Weapon getWeapon(int n) {
        return weapons[n];
    }

    /**
     * gibt die Aktuelle Spielerposition aus
     * @return 2D Vektor mit x,y Position
     */
    public Vector2 getPlayerPos() {
        return new Vector2(posX, posY);
    }

    /**
     * bewegt den Charakter in eine bestimmte Richtung
     * @param dx Anzahl Schritte in x Richtung
     * @param dy Anzahl Schritte in y Richtung
     */
    ActionLog move(int dx, int dy, ActionLog log, GameState state) {
        Vector2 bef = new Vector2(posX, posY);
        this.posX += this.posX + dx >= 0 ? (this.posX + dx < state.getBoardSizeX() ? dx : state.getBoardSizeX() - 1 - this.posX) : -this.posX;
        this.posY += this.posY + dy >= 0 ? (this.posY + dy < state.getBoardSizeY() ? dy : state.getBoardSizeY() - 1 - this.posY) : -this.posY;
        if (log != null) {
            log.addAction(new CharacterMoveAction(bef, new Vector2(posX, posY), team, teamPos));
        }
        return log;
    }


    /**
     * bewegt den Spieler nach Rechts um ToDo
     * @return Übrig-Gebliebene Energie des Spielers
     */
    public int moveRight(GameState state) {
        if (this.stamina > 10) {
            // ToDo: wie viel bewegt er sich -> null mit Animator.animate(new ActionLog) ersetzen
            move(1,0, null, state);
            this.stamina -= 10; // ToDo
        }
        return this.stamina;
    }

    /**
     * bewegt den Spieler nach Links um ToDo
     * @return Rest-Energie des Spielers
     */
    public int moveLeft(GameState state) {
        if (this.stamina > 10) {
            // ToDo wie in moveRight()
            move(-1, 0, null, state);
            this.stamina -= 10; // ToDo
        }
        return this.stamina;
    }

}

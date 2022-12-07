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
    private int stamina = 48;

    private int team;
    private int teamPos;
    private GameState state;
    private Simulation sim;

    private Weapon[] weapons;


    /**
     * erstellt einen neuen Charakter
     * @param x X-Spawn Punkt
     * @param y Y-Spawn Punkt
     * @param state GameState in dem der Charakter existiert
     * @param team Team-Nummer
     * @param teamPos Nummer im Team
     * @param sim zugehörige Simulation
     */
    GameCharacter(int x, int y, GameState state, int team, int teamPos, Simulation sim) {
        this.posX = x;
        this.posY = y;
        this.state = state;
        this.team = team;
        this.teamPos = teamPos;
        this.sim = sim;
        initInventory();
    }


    /**
     * Gibt die Lebensanzahl eines Spielers zurück (maximal 100)
     * @return Lebensanzahl des Spielers
     */
    public int getHealth() { return this.health; }
    public int getStamina() { return this.stamina; }
    void setHealth(int newHealth) { this.health = newHealth; }
    void setStamina(int newStamina) { this.stamina = newStamina; }

    public int getTeam() {
        return team;
    }

    public int getTeamPos() {
        return teamPos;
    }

    /**
     * initialisiert das Inventar mit grundlegenden Waffen
     * @Weihnachtsaufgabe Initilisiert mit Keks und Zuckerstange (jeweils 50 Schuss)
     */
    protected void initInventory() {
        this.weapons = new Weapon[2];
        weapons[0] = new ChristmasWeapon(10, 40, 50, false, Weapon.Type.COOKIE,this.sim, this);
        weapons[1] = new ChristmasWeapon(20, 40, 50, false, Weapon.Type.SUGAR_CANE, this.sim, this);
    }

    /**
     * wählt Waffe n aus dem Inventar aus
     * @param n Waffe die gewählt werden soll
     * @return Waffe die gewählt wurde
     */
    public Weapon getWeapon(int n) {
        ChristmasWeapon wp =  (ChristmasWeapon)weapons[n];
        this.sim.getActionLog().goToNextAction();
        this.sim.getActionLog().addAction(new CharacterSwitchWeaponAction(this.team, this.teamPos, wp.getType()));
        return wp;
    }

    /**
     * gibt die Aktuelle Spielerposition aus
     * @return 2D Vektor mit x,y Position
     */
    public Vector2 getPlayerPos() {
        return new Vector2(posX, posY);
    }

    void setPosX(int posX) {
        this.posX = posX;
    }

    void setPosY(int posY) {
        this.posY = posY;
    }

    void fall() {
        Vector2 posBef = this.getPlayerPos().cpy();
        int fallen = 0;
        while (this.posY > 0 && this.state.getTile(posX, posY - 1) == null) {
            this.posY -= 1;
            fallen++;
        }
        int health = this.getHealth();
        if (this.posY == 0) {
            this.setHealth(0);
        } else {
            this.setHealth(fallen * 5);
        }
        this.sim.getActionLog().addAction(new CharacterFallAction(posBef, this.getPlayerPos(), team, teamPos, 10));
        this.sim.getActionLog().goToNextAction();
        this.sim.getActionLog().addAction(new CharacterHitAction(this.team, this.teamPos, health, this.getHealth()));
        if (this.getHealth() == 0) {
            sim.endTurn();
        }
    }

    /**
     * bewegt den Charakter in eine bestimmte Richtung
     * -> maximal bis er auf eine Tile stößt oder die Stamina leer ist
     * @param dx Anzahl Schritte in x Richtung
     */
    void move(int dx) {
        if (dx == 0) {
            return;
        }
        Vector2 bef = new Vector2(posX, posY);
        // this.posX += this.posX + dx >= 0 ? (this.posX + dx < state.getBoardSizeX() ? dx : state.getBoardSizeX() - 1 - this.posX) : -this.posX;

        if (this.posX + dx < 0) {
            dx = -posX;
        }
        if (this.posX + dx > state.getBoardSizeX()) {
            dx = state.getBoardSizeX() - posX;
        }

        if (dx < 0) {
            for (int i = 0; i >= dx; i--) {
                if (state.getTile(posX + i , posY - 1) == null) {
                    dx = i;
                    if (this.stamina < abs(dx)) {
                        dx = dx > 0 ? stamina : -stamina;
                    }
                    this.posX += dx;
                    Vector2 posAf = new Vector2(posX, posY);
                    this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 0));
                    this.fall();
                    return;
                }
                if (state.getTile(posX + i + 1, posY) != null) {
                    dx = i;
                    break;
                }
            }

        } else {
            for (int i = 0; i <= dx; i++) {
                if (state.getTile(posX + i, posY - 1) == null) {
                    dx = i;
                    if (this.stamina < abs(dx)) {
                        dx = dx > 0 ? stamina : -stamina;
                    }
                    this.posX += dx;
                    Vector2 posAf = new Vector2(posX, posY);

                    this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 0));
                    this.fall();
                    return;
                }
                if (state.getTile(posX + i + 1, posY) != null) {
                    dx = i;
                    break;
                }
            }
        }
        if (this.stamina < abs(dx)) {
            return;
        }
        this.posX += dx;
        Vector2 posAf = new Vector2(posX, posY);

        this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 10));
    }


    /**
     * berechnet den Betrag einer Zahl
     * @param n natürliche Zahl
     * @return Betrag von n
     */
    int abs(int n) {
        return n >= 0 ? n : -n;
    }

    /**
     * bewegt den Spieler nach in eine Richung
     * verbraucht Stamina
     */
    protected void moveDX(int dx) {
        if (this.sim.getActionLog().getRootAction() != this.sim.getActionLog().lastAddedAction) {
            this.sim.getActionLog().goToNextAction();
        }
        move(dx);
    }

}

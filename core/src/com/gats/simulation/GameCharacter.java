package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert eine {@link GameCharacter Spielfigur} auf der Karte
 */
public class GameCharacter {

    private int posX;
    private int posY;
    private int health = 100;
    private int stamina = 48;
    private boolean alreadyShooted = false;

    private int team;
    private int teamPos;
    private GameState state;
    private Simulation sim;
    private Vector2 dir = new Vector2(1,0);
    private float strength = 0.5f;

    private Weapon[] weapons;
    private int selectedWeapon = -1;


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
        this.stamina = 60;
        initInventory();
    }

    /**
     * Gibt den Waffen Typ der aktuell gewählten Waffe aus (als Enum)
     * @return Enum WeaponType
     */
    public WeaponType getSelectedWeapon() {
        if (selectedWeapon != -1) {
            return weapons[selectedWeapon].getType();
        } else {
            return WeaponType.NOT_SELECTED;
        }
    }

    /**
     * Wählt eine Waffe anhand des Typs aus
     * @param type Waffentyp
     */
    void selectWeapon(WeaponType type) {
        switch (type) {
            case COOKIE:
                selectedWeapon = 0;
                sim.getActionLog().goToNextAction();
                sim.getActionLog().addAction(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.COOKIE));
                break;
            case SUGAR_CANE:
                selectedWeapon = 1;
                sim.getActionLog().goToNextAction();
                sim.getActionLog().addAction(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.SUGAR_CANE));
                break;
            default:
                selectedWeapon = -1;
                sim.getActionLog().goToNextAction();
                sim.getActionLog().addAction(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.NOT_SELECTED));
        }
    }

    /**
     * Schießt mit der ausgewählten Waffe. Kann nur einmal pro Spielzug aufgerufen werden
     * @return true wenn erfolgreich abgeschossen
     */
    boolean shoot() {
        if (alreadyShooted) {
            return false;
        }
        if (selectedWeapon != -1) {
            weapons[selectedWeapon].shoot(dir, strength);
            alreadyShooted = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Bestimmt, ob der Charakter noch schießen darf
     * @param alreadyShooted true wenn er bereits geschossen hat
     */
    void setAlreadyShooted(boolean alreadyShooted) {
        this.alreadyShooted = alreadyShooted;
    }

    /**
     * Gibt die Lebensanzahl eines Charakters zurück (maximal 100)
     * @return Lebensanzahl des Charakters
     */
    public int getHealth() { return this.health; }

    /**
     * Gibt die Ausdaueranzahl des Charakters zurück
     * @return Ausdaueranzahl des Charakters
     */
    public int getStamina() { return this.stamina; }
    void setHealth(int newHealth) { this.health = newHealth; }
    void setStamina(int newStamina) { this.stamina = newStamina; }

    /**
     * Gibt die Teamnummer des Charakters zurück
     * @return Teamnummer des Charakters
     */
    public int getTeam() {
        return team;
    }

    /**
     * gibt die Position im Team zurück
     * @return Charakter-Position im Team
     */
    public int getTeamPos() {
        return teamPos;
    }



    /**
     * initialisiert das Inventar mit grundlegenden Waffen
     * @Weihnachtsaufgabe Initilisiert mit Keks und Zuckerstange (jeweils 50 Schuss)
     */
    protected void initInventory() {
        this.weapons = new Weapon[2];
        weapons[0] = new ChristmasWeapon(10, 40, 50, false, WeaponType.COOKIE,this.sim, this);
        weapons[1] = new ChristmasWeapon(20, 40, 4, false, WeaponType.SUGAR_CANE, this.sim, this);
    }

    /**
     * Gibt eine Waffe aus dem Inventar zurück.
     * @param n Index der Waffe, die gewählt werden soll.
     * @return Instanz der Waffe.
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

    /**
     * Gibt die Anzahl an Verfügbaren Waffen aus
     * @return Anzahl verfügbarer Waffen (unabhängig der Munition)
     */
    public int getWeaponAmount() {
        return weapons.length;
    }

    void setPosX(int posX) {
        this.posX = posX;
    }

    void setPosY(int posY) {
        this.posY = posY;
    }

    void resetStamina() {
        this.stamina = 60;
    }

    /**
     * Lässt den Charakter fallen
     */
    void fall() {
        Vector2 posBef = this.getPlayerPos().cpy();
        int fallen = 0;
        while (this.posY / 16 > 0 && this.state.getTile((int)((posX + (posX % 16)) / 16) , (int)(posY / 16) - 1) == null) {
            this.posY -= 16;
            fallen++;
        }
        if (fallen == 0) return;
        int health = this.getHealth();
        if (this.posY / 16 <= 0) {
            this.setHealth(0);
        } else {
            this.setHealth(getHealth() - fallen);
        }
        this.sim.getActionLog().addAction(new CharacterFallAction(posBef, this.getPlayerPos(), team, teamPos, 0.001f));
        this.sim.getActionLog().goToLast();
        this.sim.getActionLog().addAction(new CharacterHitAction(this.team, this.teamPos, health, this.getHealth()));
    }

    /**
     * bewegt den Charakter in eine bestimmte Richtung
     * -> maximal bis er auf eine Tile stößt oder die Stamina leer ist
     * @param dx Anzahl Schritte in x Richtung
     */
    void move(int dx) {
//        System.out.println("moving "+ dx);
        if (dx == 0) {
            return;
        }
        Vector2 bef = new Vector2(posX, posY);
        // this.posX += this.posX + dx >= 0 ? (this.posX + dx < state.getBoardSizeX() ? dx : state.getBoardSizeX() - 1 - this.posX) : -this.posX;

//        System.out.println("moved from "+ bef);
        if ((this.posX + dx) / 16 < 0) {
            dx = -posX;
        }
        if ((this.posX + dx) / 16 > state.getBoardSizeX()) {
            dx = state.getBoardSizeX() - posX;
        }

        if (dx < 0) {
            for (int i = 0; i >= dx; i--) {
                if (state.getTile((posX + i + 14) / 16 , ((posY) / 16) - 1) == null) {
                    dx = i;
                    if (this.stamina < abs(dx)) {
                        dx = dx > 0 ? stamina : -stamina;
                    }
                    this.posX += dx;
                    Vector2 posAf = new Vector2(posX, posY);

//                    System.out.println("moved to because there was hole"+ posAf);
                    this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 0.001f));
                    this.fall();
                    return;
                }
                if (state.getTile(((posX) / 16), posY / 16) != null) {
                    dx = i;
                    break;
                }
            }

        } else {
            for (int i = 0; i <= dx; i++) {
                if (state.getTile((posX + i) / 16, (int)(posY / 16) - 1) == null) {
                    dx = i;
                    if (this.stamina < abs(dx)) {
                        dx = dx > 0 ? stamina : -stamina;
                    }
                    this.posX += dx;
                    Vector2 posAf = new Vector2(posX, posY);

//                   System.out.println("moved to because there was hole"+ posAf);
                    this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 0.001f));
                    this.fall();
                    return;
                }
                if (state.getTile(((posX - (posX % 16)) / 16) + 1, posY / 16) != null) {
                    dx = i;
                    break;
                }
            }
        }
        if (this.stamina < abs(dx)) {
            return;
        }
        stamina -= abs(dx);
        this.posX += dx;
        Vector2 posAf = new Vector2(posX, posY);

        // System.out.println("moved to "+ posAf);
        this.sim.getActionLog().addAction(new CharacterMoveAction(bef, posAf, team, teamPos, 0.001f));
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

    /**
     *  Erstellt eine CharacterAimAction. Damit wird im Animator der AimIndicator verändert.
     * @param angle Winkel mit dem gerade gezielt wird.
     * @param strength Stärke des Zielens
     */
    protected void aim(Vector2 angle, float strength){
        strength = Math.abs(strength) % 1.01f;
        this.dir = angle;
        this.strength = strength;
        this.sim.getActionLog().addAction(new CharacterAimAction(this.team,this.teamPos, angle, strength));
    }

    float getStrength() {
        return strength;
    }

    Vector2 getDir() {
        return dir;
    }
}

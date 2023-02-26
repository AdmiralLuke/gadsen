package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Repräsentiert eine {@link GameCharacter Spielfigur} auf der Karte
 */
public class GameCharacter {

    private static final IntVector2 SIZE = new IntVector2(16, 16);

    private final IntRectangle boundingBox;


    //    private int posX;
//    private int posY;
    private int health = 100;
    private int stamina = 48;
    private boolean alreadyShot = false;

    private final int team;
    private final int teamPos;
    private final GameState state;
    private final Simulation sim;
    private Vector2 dir = new Vector2(1, 0);
    private float strength = 0.5f;

    private Weapon[] weapons;
    private int selectedWeapon = -1;


    /**
     * erstellt einen neuen Charakter
     *
     * @param x       X-Spawn Punkt
     * @param y       Y-Spawn Punkt
     * @param state   GameState in dem der Charakter existiert
     * @param team    Team-Nummer
     * @param teamPos Nummer im Team
     * @param sim     zugehörige Simulation
     */
    GameCharacter(int x, int y, GameState state, int team, int teamPos, Simulation sim) {
        this.boundingBox = new IntRectangle(x, y, SIZE.x, SIZE.y);
        this.state = state;
        this.team = team;
        this.teamPos = teamPos;
        this.sim = sim;
        this.stamina = 60;
        initInventory();
    }

    /**
     * Gibt den Waffen Typ der aktuell gewählten Waffe aus (als Enum)
     *
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
     *
     * @param type Waffentyp
     */
    void selectWeapon(WeaponType type, Action head) {
        switch (type) {
            case COOKIE:
                selectedWeapon = 0;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.COOKIE));
                break;
            case SUGAR_CANE:
                selectedWeapon = 1;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.SUGAR_CANE));
                break;
            default:
                selectedWeapon = -1;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.NOT_SELECTED));
        }
    }

    /**
     * Schießt mit der ausgewählten Waffe. Kann nur einmal pro Spielzug aufgerufen werden
     *
     * @return true wenn erfolgreich abgeschossen
     */
    boolean shoot(Action head) {
        if (alreadyShot) {
            return false;
        }
        if (selectedWeapon != -1) {
            weapons[selectedWeapon].shoot(dir, strength, head);
            alreadyShot = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Bestimmt, ob der Charakter noch schießen darf
     *
     * @param alreadyShot true wenn er bereits geschossen hat
     */
    void setAlreadyShot(boolean alreadyShot) {
        this.alreadyShot = alreadyShot;
    }

    /**
     * Gibt die Lebensanzahl eines Charakters zurück (maximal 100)
     *
     * @return Lebensanzahl des Charakters
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Gibt die Ausdaueranzahl des Charakters zurück
     *
     * @return Ausdaueranzahl des Charakters
     */
    public int getStamina() {
        return this.stamina;
    }

    Action setHealth(int newHealth, Action head) {
        if (newHealth == this.health) return head;
        Action lastAction;
        if (newHealth < this.health) {
            lastAction = new CharacterHitAction(team, teamPos, this.health, newHealth);
        } else {
            lastAction = new CharacterAction(team, teamPos, 0) {
            };
            //ToDo implement healAction
        }
        this.health = newHealth;
        head.addChild(lastAction);
        return lastAction;
    }

    void setStamina(int newStamina) {
        this.stamina = newStamina;
    }

    /**
     * Gibt die Teamnummer des Charakters zurück
     *
     * @return Teamnummer des Charakters
     */
    public int getTeam() {
        return team;
    }

    /**
     * gibt die Position im Team zurück
     *
     * @return Charakter-Position im Team
     */
    public int getTeamPos() {
        return teamPos;
    }


    /**
     * initialisiert das Inventar mit grundlegenden Waffen
     *
     * @Weihnachtsaufgabe Initilisiert mit Keks und Zuckerstange (jeweils 50 Schuss)
     */
    protected void initInventory() {
        this.weapons = new Weapon[2];
        weapons[0] = new ChristmasWeapon(10, 40, 50, false, WeaponType.COOKIE, this.sim, this);
        weapons[1] = new ChristmasWeapon(20, 40, 4, false, WeaponType.SUGAR_CANE, this.sim, this);
    }

    /**
     * Gibt eine Waffe aus dem Inventar zurück.
     *
     * @param n Index der Waffe, die gewählt werden soll.
     * @return Instanz der Waffe.
     */
    public Weapon getWeapon(int n) {
        return weapons[n];
    }

    /**
     * gibt die Aktuelle Spielerposition aus
     *
     * @return 2D Vektor mit x,y Position
     */
    public Vector2 getPlayerPos() {
        return new Vector2(boundingBox.x, boundingBox.y);
    }

    /**
     * Gibt die Anzahl an Verfügbaren Waffen aus
     *
     * @return Anzahl verfügbarer Waffen (unabhängig der Munition)
     */
    public int getWeaponAmount() {
        return weapons.length;
    }

    void setPosX(int posX) {
        this.boundingBox.x = posX;
    }

    void setPosY(int posY) {
        this.boundingBox.y = posY;
    }

    void resetStamina() {
        this.stamina = 60;
    }


    //ToDo: move to better place; maybe to be called directly from tile
    private boolean isSolidTile(int x, int y) {
        return this.state.getTile(x, y) != null;
    }

    boolean testVerticalCollision(int bottomTileY, int topTileY, int columnX) {
        boolean colliding = false;
        for (int y = bottomTileY; y <= topTileY; y++) {
            if (isSolidTile(columnX, y)) {
                colliding = true;
                break;
            }
        }
        return colliding;
    }

    boolean testHorizontalCollision(int leftTileX, int rightTileX, int rowY) {
        boolean colliding = false;
        for (int x = leftTileX; x <= rightTileX; x++) {
            if (isSolidTile(x, rowY)) {
                colliding = true;
                break;
            }
        }
        return colliding;
    }

    /**
     *
     * @param fallen gefallene Distanz in Welt-Koordinaten
     * @return Für diese Distanz erhaltener Schaden
     */
    public int getFallDmg(int fallen){
        return Math.max(0, (fallen - 48)/4 );
    }

    /**
     * Lässt den Charakter fallen
     */
    Action fall(Action head) {
        Vector2 posBef = this.getPlayerPos().cpy();
        int leftTileX = Simulation.convertToTileCoordsX(boundingBox.x);
        // Subtract one since its integer coords and boundingBox.x is already 1px wide on its own
        int rightTileX = Simulation.convertToTileCoordsX(boundingBox.x + boundingBox.width - 1);
        int bottomTileY = Simulation.convertToTileCoordsY(boundingBox.y);
        int fallen = 0;
        boolean collision = false;

        while (this.boundingBox.y > 0) {
            boundingBox.y -= 1;
            int nextBottomTileY = Simulation.convertToTileCoordsY(boundingBox.y);
            if (nextBottomTileY != bottomTileY) {
                //Test collisions on the right side
                if (testHorizontalCollision(leftTileX, rightTileX, nextBottomTileY)) {
                    //Detected collision on new Position, reverting
                    boundingBox.y += 1;
                    if (fallen == 0) return head;
                    collision = true;
                    break;
                }
            }
            fallen++;
        }

        Action fallAction = new CharacterFallAction(posBef, this.getPlayerPos(), team, teamPos, 0.001f);
        head.addChild(fallAction);
        if (collision) {
            return this.setHealth(getHealth() - getFallDmg(fallen), fallAction);
        }
        return this.setHealth(0, fallAction);
    }

    /**
     * bewegt den Charakter in eine bestimmte Richtung
     * -> maximal bis er auf eine Tile stößt oder die Stamina leer ist
     *
     * @param dx Anzahl Schritte in x Richtung
     */
    Action walk(int dx, Action head) {

        if (dx == 0) {
            return head;
        }

        Vector2 bef = getPlayerPos();
        int sign = dx < 0 ? -1 : 1;
        int distance = dx * sign;

        //Tile coordinates the character occupies
        int leftTileX = Simulation.convertToTileCoordsX(boundingBox.x);
        // Subtract one since its integer coords and boundingBox.x is already 1px wide on its own
        int rightTileX = Simulation.convertToTileCoordsX(boundingBox.x + boundingBox.width - 1);
        int bottomTileY = Simulation.convertToTileCoordsY(boundingBox.y);
        int topTileY = Simulation.convertToTileCoordsY(boundingBox.y + boundingBox.height - 1);

        //Y-Index of the floor
        int floorY = Simulation.convertToTileCoordsY(boundingBox.y - 1);
        //X-Index of the leftmost solid Tile the character is standing on
        int leftLimit = -1;
        //X-Index of the rightmost solid Tile the character is standing on
        int rightLimit = -1;

        //find the leftmost solid Tile the character is standing on
        for (int i = leftTileX; i <= rightTileX; i++) {
            if (isSolidTile(i, floorY)) {
                leftLimit = i;
                break;
            }
        }

        if (leftLimit == -1) {
            //the character was floating in the air
            //ToDo: log occurrence of invalid state
            return walk(dx, fall(head));
        }
        //find the rightmost solid Tile the character is standing on
        for (int i = rightTileX; i >= leftLimit; i--) {
            if (isSolidTile(i, floorY)) {
                rightLimit = i;
                break;
            }
        }

        boolean falling = false;
        int moved;
        for (moved = 0; moved < distance; moved++) {
            boundingBox.x += sign;
            int nextRightTileX = Simulation.convertToTileCoordsX(boundingBox.x + boundingBox.width - 1);
            int nextLeftTileX = Simulation.convertToTileCoordsX(boundingBox.x);
            if (sign > 0) {
                if (nextRightTileX != rightTileX) {
                    //Test collisions on the right side
                    if (testVerticalCollision(bottomTileY, topTileY, nextRightTileX)) {
                        //Detected collision on new Position, reverting
                        boundingBox.x -= sign;
                        break;
                    }
                    rightTileX = nextRightTileX;
                    if (isSolidTile(rightTileX, floorY)) rightLimit = rightTileX;
                }
                if (nextLeftTileX != leftTileX) {
                    leftTileX = nextLeftTileX;
                    if (leftLimit == rightLimit) {
                        //We stepped off the last tile
                        falling = true;
                        break;
                    }
                    //Recalculating left floor boundary
                    for (int i = leftTileX; i <= rightLimit; i++) {
                        if (isSolidTile(i, floorY)) {
                            leftLimit = i;
                            break;
                        }
                    }
                }
            } else {
                if (nextLeftTileX != leftTileX) {
                    if (testVerticalCollision(bottomTileY, topTileY, nextLeftTileX)) {
                        //Detected collision on new Position, reverting
                        boundingBox.x -= sign;
                        break;
                    }
                    leftTileX = nextLeftTileX;
                    if (isSolidTile(leftTileX, floorY)) leftLimit = leftTileX;
                }

                if (nextRightTileX != rightTileX) {
                    rightTileX = nextRightTileX;
                    if (leftLimit == rightLimit) {
                        //We stepped off the last tile
                        falling = true;
                        break;
                    }
                    //Recalculating right floor boundary
                    for (int i = rightTileX; i >= leftLimit; i--) {
                        if (isSolidTile(i, floorY)) {
                            rightLimit = i;
                            break;
                        }
                    }
                }
            }
        }

        Action lastAction = new CharacterMoveAction(bef, new Vector2(boundingBox.x, boundingBox.y), team, teamPos, 0.001f);
        head.addChild(lastAction);
        if (falling) {
            lastAction = walk(dx - moved, fall(lastAction));
        }
        return lastAction;
    }


    /**
     * berechnet den Betrag einer Zahl
     *
     * @param n natürliche Zahl
     * @return Betrag von n
     */
    int abs(int n) {
        return n >= 0 ? n : -n;
    }

    /**
     * bewegt den Spieler nach in eine Richtung
     * verbraucht Stamina
     */
    protected void moveDX(int dx) {
        walk(dx, this.sim.getActionLog().lastAddedAction);
    }

    /**
     * Erstellt eine CharacterAimAction. Damit wird im Animator der AimIndicator verändert.
     *
     * @param angle    Winkel mit dem gerade gezielt wird.
     * @param strength Stärke des Zielens
     */
    protected void aim(Vector2 angle, float strength, Action head) {
        strength = Math.abs(strength) % 1.01f;
        this.dir = angle;
        this.strength = strength;
        head.addChild(new CharacterAimAction(this.team, this.teamPos, angle, strength));
    }

    float getStrength() {
        return strength;
    }

    Vector2 getDir() {
        return dir;
    }
}

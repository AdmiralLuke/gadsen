package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

import com.gats.simulation.weapons.BaseProjectile;
import com.gats.simulation.weapons.Bounceable;
import com.gats.simulation.weapons.Explosive;
import com.gats.simulation.weapons.Weapon;
import com.gats.simulation.action.*;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Repräsentiert eine {@link GameCharacter Spielfigur} auf der Karte
 */
public class GameCharacter implements Serializable {

    private static final IntVector2 SIZE = new IntVector2(9, 15);

    private int[] damageReceived;

    public static Vector2 getSize() {
        return SIZE.toFloat();
    }

    private final IntRectangle boundingBox;


    //    private int posX;
//    private int posY;
    private int health = 100;
    private int stamina;
    private boolean alreadyShot = false;

    private final int team;
    private final int teamPos;
    private final GameState state;
    private transient final Simulation sim;
    private Vector2 dir = new Vector2(1, 0);
    private float strength = 0.5f;

    private com.gats.simulation.weapons.Weapon[] weapons;
    private int selectedWeapon = -1;


    /**
     * Creates a new Character within the simulation
     *
     * @param x       initial x coordinate of the Character in world-coordinates
     * @param y       initial y coordinate of the Character in world-coordinates
     * @param state   the GameState this Character exists within
     * @param team    team index of the Character
     * @param teamPos Characters index within its team
     * @param sim     the executing Simulation instance of the game
     */
    GameCharacter(int x, int y, GameState state, int team, int teamPos, Weapon[] inventory, int health, Simulation sim) {
        this.boundingBox = new IntRectangle(x, y, SIZE.x, SIZE.y);
        this.state = state;
        this.team = team;
        this.teamPos = teamPos;
        this.sim = sim;
        this.damageReceived = new int[state.getTeamCount()];
        this.health = health;
        resetStamina();
        this.weapons = inventory;
    }

    private GameCharacter(GameCharacter original, GameState newState) {
        boundingBox = new IntRectangle(original.boundingBox);
        health = original.health;
        stamina = original.stamina;
        alreadyShot = original.alreadyShot;
        team = original.team;
        teamPos = original.teamPos;
        state = newState;
        sim = null;
        dir = original.dir.cpy();
        strength = original.strength;
        weapons = new Weapon[original.weapons.length];
        for (int i = 0; i < original.weapons.length; i++) {
            weapons[i] = original.weapons[i].copy();
        }
        selectedWeapon = original.selectedWeapon;
    }

    /**
     * Gibt den Waffentyp der aktuell gewählten Waffe aus.
     * Verschiedene typen können leicht mittels eines switch() statements unterschieden werden.
     * Bei case werden hierfür die verschiedenen Werte von {@link WeaponType} angegeben.
     *
     * @return Waffentyp als Enum
     */
    public WeaponType getSelectedWeapon() {
        if (selectedWeapon != -1) {
            return weapons[selectedWeapon].getType();
        } else {
            return WeaponType.NOT_SELECTED;
        }
    }


    /**
     * Makes the Character equip the specified weapon.
     *
     * @param type Weapon-type as Enum
     * @param head the leading action of the caller
     */
    void selectWeapon(WeaponType type, Action head) {
        switch (type) {
            case WATERBOMB:
                selectedWeapon = 0;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.WATERBOMB));
                break;
            case WATER_PISTOL:
                selectedWeapon = 1;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.WATER_PISTOL));
                break;
            case MIOJLNIR:
                selectedWeapon = 2;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.MIOJLNIR));
                break;
            case GRENADE:
                selectedWeapon = 3;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.GRENADE));
                break;
            case WOOL:
                selectedWeapon = 4;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.WOOL));
                break;
            case CLOSE_COMBAT:
                selectedWeapon = 5;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.CLOSE_COMBAT));
                break;
            default:
                selectedWeapon = -1;
                head.addChild(new CharacterSwitchWeaponAction(team, teamPos, WeaponType.NOT_SELECTED));
        }
    }

    /**
     * Makes the Character shoot its equipped {@link Weapon}.
     * Will only trigger for the first Weapon each turn, that can currently be used by this Character
     *
     * @return true, only if the selected Weapon has been used successfully
     */
    boolean shoot(Action head) {
        if (alreadyShot) {
            return false;
        }
        if (selectedWeapon != -1) {
            weapons[selectedWeapon].shoot(head, dir, strength, this.getPlayerPos(), this);
            alreadyShot = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Resets the attribute, that prevents a Character from shooting twice
     */
    void resetAlreadyShot() {
        this.alreadyShot = false;
    }

    /**
     * Gibt die Lebensanzahl eines Charakters im ganzzahligen Intervall [0, 100] zurück
     *
     * @return Lebensanzahl des Charakters
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Gibt die Menge an Ausdauer des Charakters zurück.
     * Der Charakter startet mit 60 Stamina und jeder Schritt (+/- 1.0 in Welt-Koordinaten) kostet 1 Stamina.
     * Dabei ist eine {@link Tile Box} je 16 Welt-Koordinaten oder 1-Tile-Koordinate breit und hoch
     *
     * @return Menge an Ausdauer
     */
    public int getStamina() {
        return this.stamina;
    }

    /**
     * Will set this Characters health value to a specific value.
     * May produce appropriate {@link Action Actions} in the process, which will be directly or indirectly linked to the head received from the caller.
     *
     * @param newHealth the new health value
     * @param head      the leading action of the caller
     * @return the leading action for this function
     */
    Action setHealth(int newHealth, Action head, boolean environmental) {
        state.getSim().turnsWithoutAction = 0;
        if (newHealth == this.health || this.health <= 0) return head;
        Action lastAction;
        if (newHealth < this.health) {
            int activeTeam = sim.getActiveTeam();
            damageReceived[activeTeam] += health - newHealth;
            if (activeTeam != team) {
                state.addScore(activeTeam, environmental ? 1.5f : 1.0f * (health - Math.max(newHealth, 0)));
                if (newHealth <= 0 && health > 0) {
                    state.addScore(activeTeam, Simulation.SCORE_KILL);
                    for (int i = 0; i< damageReceived.length; i++){
                        if (i!=activeTeam && damageReceived[i]>=50)
                            state.addScore(activeTeam, Simulation.SCORE_ASSIST);
                    }
                }
            }
            lastAction = new CharacterHitAction(team, teamPos, this.health, newHealth);
        } else {
            state.addScore(team, (newHealth - health));
            lastAction = new CharacterHealAction(team, teamPos, this.health, newHealth) {
            };
        }
        this.health = newHealth;
        head.addChild(lastAction);
        return lastAction;
    }

    /**
     * Sets this Character's Stamina to a certain value
     *
     * @param newStamina New Stamina value for this Character
     */
    void setStamina(int newStamina) {
        this.stamina = newStamina;
    }

    /**
     * Gibt den Index (beginnend bei 0) des Teams zurück, zu dem dieser Charakter gehört.
     *
     * @return Teamindex des Charakters
     */
    public int getTeam() {
        return team;
    }

    /**
     * Gibt den Index (beginnend bei 0) des Charakters, innerhalb seines Teams zurück.
     *
     * @return Charakterindex im eigenen Team
     */
    public int getTeamPos() {
        return teamPos;
    }


    /**
     * Will initialise the Characters inventory with the default number and selection of WEapons.
     *
     * @Weihnachtsaufgabe Inventar wird initialisiert mit Keks (50 Schuss) und Zuckerstange (4 Schuss)
     */
    protected static Weapon[] initInventory(Simulation sim, int[] weaponCounts) {
        if (weaponCounts== null){
            weaponCounts = new int[0];
        }
        Weapon[] weapons = new Weapon[6];
        weapons[0] = new Weapon(new Explosive(new BaseProjectile(3, 0.6f, 0, sim, ProjectileAction.ProjectileType.WATERBOMB),2), weaponCounts.length>0?weaponCounts[0]:200, WeaponType.WATERBOMB, 10);
        weapons[1] = new Weapon(new BaseProjectile(5, 0f, 0, sim, ProjectileAction.ProjectileType.WATER), weaponCounts.length>1?weaponCounts[1]:200, WeaponType.WATER_PISTOL, 9);
        weapons[2] = new Weapon(new BaseProjectile(5, 0f, 0, sim, ProjectileAction.ProjectileType.MIOJLNIR), weaponCounts.length>2?weaponCounts[2]:200, WeaponType.MIOJLNIR, 13);
        weapons[3] = new Weapon(new Explosive(new BaseProjectile(10, 0.7f, 0, sim, ProjectileAction.ProjectileType.GRENADE), 3), weaponCounts.length>3?weaponCounts[3]:200, WeaponType.GRENADE, 10);
        weapons[4] = new Weapon(new Bounceable(new BaseProjectile(1, 0, 0, sim, ProjectileAction.ProjectileType.WOOL), 10, 0.8f), weaponCounts.length>4?weaponCounts[4]:200, WeaponType.WOOL, 15);
        weapons[5] = new Weapon(new BaseProjectile(10, 0.9f, 0, sim, ProjectileAction.ProjectileType.CLOSE_COMB), weaponCounts.length>5?weaponCounts[5]:200, WeaponType.CLOSE_COMBAT, 0.5f);
        return weapons;
    }

    /**
     * Gibt eine Waffe aus dem Inventar zurück.
     * Der Index muss aus dem ganzzahligen Intervall [0, getWeaponAmount() - 1] stammen.
     *
     * @param n Index der Waffe, die gewählt werden soll.
     * @return Instanz der Waffe.
     */
    public Weapon getWeapon(int n) {
        return weapons[n];
    }

    /**
     * Gibt die Spielerposition in Welt-Koordinaten aus.
     * Hierbei entspricht eine Welt-Koordinate exakt einem Pixel auf dem Bildschirm.
     * Die zurückgegebene Instanz ist eine Kopie der Position dieses Characters und kann beliebig verändert werden.
     *
     * @return position in Welt-Koordinaten
     */
    public Vector2 getPlayerPos() {
        return new Vector2(boundingBox.x, boundingBox.y);
    }

    /**
     * Gibt die Anzahl an verfügbaren Waffen aus.
     * Das Ergebnis is um 1 höher als der maximale Index, der von der {@link #getWeapon(int)} Function akzeptiert wird.
     *
     * @return Anzahl verfügbarer Waffen (unabhängig der Munition)
     */
    public int getWeaponAmount() {
        return weapons.length;
    }

    /**
     * Sets the X-component of this characters position to a specific value
     *
     * @param posX x coordinate of the Character in world-coordinates
     */
    void setPosX(int posX) {
        this.boundingBox.x = posX;
    }

    /**
     * Sets the Y-component of this characters position to a specific value
     *
     * @param posY y coordinate of the Character in world-coordinates
     */
    void setPosY(int posY) {
        this.boundingBox.y = posY;
    }

    /**
     * Resets the Stamina of this Character to its default value;
     */
    void resetStamina() {
        this.stamina = 60;
    }


    /**
     * Tests if the {@link Tile} at the specified tile-coordinates can be collided with.
     *
     * @param x x coordinate of the Tile in tile-coordinates
     * @param y y coordinate of the Tile in tile-coordinates
     * @return True, only if there is a solid Tile at the specified coordinates
     */
    private boolean isSolidTile(int x, int y) {
        Tile tile = this.state.getTile(x, y);
        return tile != null && tile.isSolid();
    }

    /**
     * Tests whether there exists at least one {@link Tile} within the specified interval
     * of the selected column that can be collided with.
     *
     * @param columnX     X-coordinate of the selected column in tile-coordinates
     * @param bottomTileY Y-coordinate of the lower bound of the interval in tile-coordinates
     * @param topTileY    Y-coordinate of the upper bound of the interval in tile-coordinates
     * @return True, if at least one solid Tile exists in the specified space
     */
    private boolean testVerticalCollision(int columnX, int bottomTileY, int topTileY) {
        boolean colliding = false;
        for (int y = bottomTileY; y <= topTileY; y++) {
            if (isSolidTile(columnX, y)) {
                colliding = true;
                break;
            }
        }
        return colliding;
    }

    /**
     * Tests whether there exists at least one {@link Tile} within the specified interval
     * of the selected row that can be collided with.
     *
     * @param leftTileX  X-coordinate of the lower bound of the interval in tile-coordinates
     * @param rightTileX X-coordinate of the upper bound of the interval in tile-coordinates
     * @param rowY       Y-coordinate of the selected row in tile-coordinates
     * @return True, if at least one solid Tile exists in the specified space
     */
    private boolean testHorizontalCollision(int leftTileX, int rightTileX, int rowY) {
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
     * Berechnet den Fallschaden, der durch das Fallen der angegebenen Distanz entstehen würde.
     *
     * @param distance gefallene Distanz in Welt-Koordinaten
     * @return Durch diese Distanz entstehender Schaden
     */
    public int getFallDmg(int distance) {
        return Math.max(0, (distance - 48) / 4);
    }

    /**
     * Makes the Character fall if it is currently suspended (without solid Tiles underneath).
     * May produce appropriate {@link Action Actions} in the process, which will be directly or indirectly linked to the head received from the caller.
     *
     * @param head the leading action of the caller
     * @return the leading action for this function
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

        Action fallAction = new CharacterFallAction(0.001f, team, teamPos, posBef, this.getPlayerPos());
        head.addChild(fallAction);
        if (collision) {
            return this.setHealth(getHealth() - getFallDmg(fallen), fallAction, true);
        }
        return this.setHealth(0, fallAction, true);
    }

    /**
     * Makes the Character walk |dx| steps into the direction specified by the sign of dx, where a positive value
     * indicates walking to the right and a negative value indicates walking to the left.
     * The Character may take fewer steps, if he collides with a wall or runs out of stamina, while walking.
     * If the Character encounters a gap, where he can fall down, he will do so and continue his remaining movement after the fall.
     * May produce appropriate {@link Action Actions} in the process, which will be directly or indirectly linked to the head received from the caller.
     *
     * @param dx   direction and amount of steps to take
     * @param head the leading action of the caller
     * @return the leading action for this function
     */
    Action walk(int dx, Action head) {

        if (dx == 0 || stamina <= 0 || health <= 0) {
            return head;
        }

        Vector2 bef = getPlayerPos();
        int sign = dx < 0 ? -1 : 1;
        int distance = dx * sign;

        //Tile coordinates the character occupies
        //Horizontal coordinates 0->left; 1->right
        int[] tileX = new int[2];
        int[] nextTileX = new int[2];
        tileX[0] = Simulation.convertToTileCoordsX(boundingBox.x);
        // Subtract one since its integer coords and boundingBox.x is already 1px wide on its own
        tileX[1] = Simulation.convertToTileCoordsX(boundingBox.x + boundingBox.width - 1);

        int bottomTileY = Simulation.convertToTileCoordsY(boundingBox.y);
        int topTileY = Simulation.convertToTileCoordsY(boundingBox.y + boundingBox.height - 1);

        //Y-Index of the floor
        int floorY = Simulation.convertToTileCoordsY(boundingBox.y - 1);
        int[] limit = new int[2];
        //X-Index of the leftmost solid Tile the character is standing on
        limit[0] = -1000;
        //X-Index of the rightmost solid Tile the character is standing on
        limit[1] = -1001;

        //find the leftmost solid Tile the character is standing on
        for (int i = tileX[0]; i <= tileX[1]; i++) {
            if (isSolidTile(i, floorY)) {
                limit[0] = i;
                break;
            }
        }

        if (limit[0] == -1000) {
            //the character was floating in the air
            //ToDo: log occurrence of invalid state
            System.out.println("left: " + tileX[0]);
            System.out.println("right: " + tileX[1]);
            return walk(dx, fall(head));
        }
        //find the rightmost solid Tile the character is standing on
        for (int i = tileX[1]; i >= limit[0]; i--) {
            if (isSolidTile(i, floorY)) {
                limit[1] = i;
                break;
            }
        }

        boolean falling = false;
        int moved;
        for (moved = 0; moved < distance; moved++) {
            if (stamina <= 0) break;
            boundingBox.x += sign;
            stamina--;
            nextTileX[0] = Simulation.convertToTileCoordsX(boundingBox.x);
            nextTileX[1] = Simulation.convertToTileCoordsX(boundingBox.x + boundingBox.width - 1);
            //Index in walking direction 0 when walking left; 1 when walking right
            int iid = (sign + 1) / 2;
            //Index against walking direction; opposite of iid
            int iad = (sign - 1) / (-2);

            //detect whether we are potentially entering a new column of tiles
            if (nextTileX[iid] != tileX[iid]) {
                //Test collisions on the side we are moving to
                if (testVerticalCollision(nextTileX[iid], bottomTileY, topTileY)) {
                    //Detected collision on new Position, reverting
                    boundingBox.x -= sign;
                    stamina++;
                    break;
                }
                //the leading side sucessfully entered the new column without collision
                tileX[iid] = nextTileX[iid];
                //If we find a solid tile in the floor of the new column update the floor boundary on the leading side
                if (isSolidTile(tileX[iid], floorY)) limit[iid] = tileX[iid];
            }
            //detect whether we potentially left a column of tiles on the end
            if (nextTileX[iad] != tileX[iad]) {
                tileX[iad] = nextTileX[iad];
                //If the floor we stepped off was also the last floor boundary in movement direction, we fell off
                if (limit[0] == limit[1]) {
                    //We stepped off the last tile
                    falling = true;
                    break;
                }
                //Searching for the floor boundary on the trailing end
                for (int i = tileX[iad]; i * sign <= limit[iid] * sign; i += sign) {
                    if (isSolidTile(i, floorY)) {
                        limit[iad] = i;
                        break;
                    }
                }
            }

        }
        //Movement completed for some reason, log action
        Action lastAction = new CharacterWalkAction(0, team, teamPos, bef, new Vector2(boundingBox.x, boundingBox.y));


        head.addChild(lastAction);

        if (falling) {
            //We detected a gap while walking, start falling after walk
            lastAction = walk(sign * (distance - moved - 1), fall(lastAction));
        }
        return lastAction;
    }

    /**
     * Sets the aiming-attributes of this Character. Will normalize the direction and confine the strength to the interval [0.0, 1.0].
     * If the direction is a zero-vector, aiming straight up (direction of x=0 and y=1) will be assumed.
     * Will produce a {@link CharacterAimAction} in the process, which will be directly linked to the head received from the caller.
     *
     * @param angle    direction this Character is supposed to aim in
     * @param strength strength of the shot between 0.0 and 1.0
     * @param head     the leading action of the caller
     * @return the leading action for this function
     */
    protected Action aim(Vector2 angle, float strength, Action head) {
        strength = Math.max(0.0f, Math.min(1.0f, strength));
        if (angle.len() == 0) {
            this.dir = new Vector2(0, 1);
        } else {
            this.dir = angle.nor();
        }
        this.strength = strength;
        Action aimAction = new CharacterAimAction(this.team, this.teamPos, angle, strength);
        head.addChild(aimAction);
        return aimAction;
    }

    /**
     * @return the strength this Character is aiming with
     */
    float getStrength() {
        return strength;
    }

    /**
     * @return the direction this Character is aiming in
     */
    Vector2 getDir() {
        return dir;
    }

    protected GameCharacter copy(GameState state) {
        return new GameCharacter(this, state);
    }

    @Override
    public String toString() {
        return "GameCharacter{" +
                "damageDealt=" + Arrays.toString(damageReceived) +
                ", boundingBox=" + boundingBox +
                ", health=" + health +
                ", stamina=" + stamina +
                ", alreadyShot=" + alreadyShot +
                ", team=" + team +
                ", teamPos=" + teamPos +
                ", dir=" + dir +
                ", strength=" + strength +
                ", weapons=" + Arrays.toString(weapons) +
                ", selectedWeapon=" + selectedWeapon +
                '}';
    }

    public boolean isAlive() {
        return health>0;
    }
}

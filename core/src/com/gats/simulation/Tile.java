package com.gats.simulation;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 * Repräsentiert eine Box aus denen die Karte aufgebaut ist.
 * Das genaue Verhalten insbesondere von Spezialboxen, wie z.B. Waffendrops wird durch erbende Klassen realisiert.
 */
public class Tile {
    protected static final IntVector2 tileSize = new IntVector2(16, 16);

    // Box als Ankerpunkt

    private boolean isAnchor;

    // Box hängt an einer Box oder an Verkettung von Boxen die an Anker hängt
    private boolean isAnchored;


    // Haltbarkeit der Box
    private int health = 250;

    private IntVector2 position;
    private GameState state;

    Tile right;
    Tile up;
    Tile down;
    Tile left;


    public int getType() {
        return isAnchor? 1 : 0;
    }

    public IntVector2 getPosition() {
        return this.position;
    }

    public Vector2 getFloatPosition() {
        return this.position.toFloat();
    }


    /**
     * Box wird nur aus Position erstellt. Definitiv kein Anker selbst
     *
     * @param x Position X
     * @param y Position Y
     */
    Tile(int x, int y, GameState state) {
        new Tile(x, y, false, state);
    }

    Tile(int x, int y, GameState state, boolean isAnchored) {
        new Tile(x, y, false, state);
        this.isAnchored = isAnchored;
    }

    /**
     * erweiterte Erstellung von Boxen
     *
     * @param x        Position X auf dem Spielbrett
     * @param y        Position Y auf dem Spielbrett
     * @param isAnchor Box kann als Anker makiert werden, sonst wird geschaut ob Nachbar Ankerpunkt ist
     */
    Tile(int x, int y, boolean isAnchor, GameState state) {
        this.isAnchor = isAnchor;
        this.isAnchored = isAnchor || checkIfAnchored(x, y, state);
        this.position = new IntVector2(x, y);
        this.state = state;
        if (isAnchored) {
            state.getBoard()[x][y] = this;
            sortIntoTree();
        } else {
            // Die Garbage Collection wird das schon löschen
            state.getBoard()[x][y] = null;
        }
    }

    /**
     * fügt eine Tile in die Graphenstruktur aus Tiles an
     */
    void sortIntoTree() {

        if (this.position.x > 0) {
            this.left = getTileAtPosition(position.x - 1, position.y, state);
        }
        if (this.position.y > 0) {
            this.down = getTileAtPosition(position.x, position.y - 1, state);
        }
        if (this.position.x < state.getBoardSizeY()) {
            this.right = getTileAtPosition(position.x + 1, position.y, state);
        }
        if (this.position.y < state.getBoardSizeY()) {
            this.up = getTileAtPosition(position.x, position.y + 1, state);
        }

        if (this.left != null) { this.left.right = this; }
        if (this.right != null) { this.right.left = this; }
        if (this.up != null) { this.up.down = this; }
        if (this.down != null) { this.down.up = this; }
    }

    /**
     * Konstruktor zum Klonen von Tiles
     */
    Tile(boolean isAnchor, boolean isAnchored, int health) {
        this.isAnchor = isAnchor;
        this.isAnchored = isAnchored;
        this.health = health;
    }

    /**
     * Schaut sich im Board Array an, ob Nachbarboxen verankert sind, oder selber Anker sind
     *
     * @return true wenn Nachbar anchored oder Anker ist
     *      x
     *     xax
     *      x
     * a = verankert bzw. ankernd
     * x = kann angehangen werden
     */
    private boolean checkIfAnchored(int x, int y, GameState state) {
        boolean isAnchor = false;
        if (x > 0) {
            isAnchor = state.getTile(x - 1,y) != null || state.getTile(x + 1,y) != null;
        } else if (x == 0) {
            isAnchor = state.getTile(x + 1,y) != null;
        }
        if (y > 0 && !isAnchor) {
            isAnchor = state.getTile(x,y - 1) != null || state.getTile(x,y + 1) != null;
        } else if (y == 0 & !isAnchor) {
            isAnchor = state.getTile(x,y + 1) != null;
        }
        return isAnchor;
    }

    /**
     * wenn die Box keinen Ankerpunkt hat, soll diese Simulation ausgeführt werden, bei der die Box solange fällt, bis sie im void
     * oder auf anderer Box landet
     */
    void onDestroy() {
        this.state.getSim().getActionLog().goToNextAction();
        ArrayList<Tile> rightList = null;
        ArrayList<Tile> upperList = null;
        ArrayList<Tile> lowerList = null;
        ArrayList<Tile> leftList = null;
        state.getBoard()[this.position.x][this.position.y] = null;
        this.state.getSim().getActionLog().addAction(new TileDestroyAction(this.position));
        if (hasRight()) rightList = right.convertGraphToList(new ArrayList<Tile>(), 0);
        if (hasUp()) upperList = up.convertGraphToList(new ArrayList<Tile>(), 1);
        if (hasDown()) lowerList = down.convertGraphToList(new ArrayList<Tile>(), 2);
        if (hasLeft()) leftList = left.convertGraphToList(new ArrayList<Tile>(), 3);


        if (rightList != null) checkForAnchor(rightList);
        if (leftList != null) checkForAnchor(leftList);
        if (upperList != null) checkForAnchor(upperList);
        if (lowerList != null) checkForAnchor(lowerList);
    }

    void checkForAnchor(ArrayList<Tile> list) {
        for (Tile tile : list) {
            if (tile.isAnchor) {
                return;
            }
        }
        for (Tile tile : list) {
            state.getBoard()[tile.position.x][tile.position.y] = null;
            tile.destroyTile();
        }
        list.clear();
    }

    void destroyTile() {
        IntVector2 posBef = this.position.cpy();
        while (getTileAtPosition(this.position.x, this.position.y, state) == null && this.position.y > 0) {
            this.position.add(0, -1);
            for (GameCharacter[] characters : state.getTeams()) {
                for (GameCharacter character : characters) {
                    if (character.getPlayerPos().x == this.position.x && character.getPlayerPos().y == this.position.y) {
                        LinearPath path = new LinearPath(posBef.toFloat().scl(tileSize.toFloat()), this.position.toFloat().scl(tileSize.toFloat()));
                        int tmpHealth = character.getHealth();
                        character.setHealth(tmpHealth - ((int)path.getEndTime() * 5));
                        Action tmpAction = new TileMoveAction(posBef, this.position, 2);
                        Action tmpDestAction = new TileDestroyAction(this.getPosition());
                        tmpDestAction.addChild(new CharacterHitAction(character.getTeam(), character.getTeamPos(), tmpHealth, character.getHealth()));
                        return;
                    }
                }
            }
        }
        float duration = 2000;
        Action tmpAction = new TileMoveAction(posBef, this.position, duration);
        Action tmpDestAction = new TileDestroyAction(this.getPosition());
        tmpAction.addChild(tmpDestAction);

        this.state.getSim().getActionLog().addAction(tmpAction);
    }

    /**
     *
     * @param tiles ArrayList mit rekursiv aufbauend verbundenen Tiles
     * @param direction Angabe woher man kommt (um Endlos zu vermeiden)
     *                  0 kommt von links
     *                  1 kommt von unten
     *                  2 kommt von oben
     *                  3 kommt von rechts
     * @return ArrayList mit allen verbunden Tiles
     */
    public ArrayList<Tile> convertGraphToList(ArrayList<Tile> tiles, int direction) {
        tiles.add(this);
        if (this.hasUp() && !(direction == 2)) {
            up.convertGraphToList(tiles, 1);
        }
        if (this.hasDown() && !(direction == 1)) {
            down.convertGraphToList(tiles, 2);
        }
        if (this.hasLeft() && !(direction == 0)) {
            left.convertGraphToList(tiles, 3);
        }
        if (this.hasRight() && !(direction == 3)) {
            right.convertGraphToList(tiles, 0);
        }
        return tiles;
    }

    Tile getTileAtPosition(int x, int y, GameState state) {
        return state.getTile(x, y);
    }

    /**
     * vergleicht 2 Tiles anhand der Position miteinander
     * @param t weitere Tile mit der verglichen werden soll
     * @return true wenn gleiche Position sonst false
     */
    public boolean equals(Tile t) {
        return this.position.equals(t.position);
    }

    @Override
    protected Tile clone() throws CloneNotSupportedException {
        super.clone();
        return new Tile(this.isAnchor, this.isAnchored, this.health);
    }

    /**
     * @return neuen Clon einer Tile
     */
    public Tile returnClone() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "isAnchor=" + isAnchor +
                ", isAnchored=" + isAnchored +
                ", health=" + health +
                ", position" + position +
                '}';
    }

    boolean hasRight() { return right != null; }
    boolean hasLeft() { return left != null; }
    boolean hasUp() { return up != null; }
    boolean hasDown() { return down != null; }
}

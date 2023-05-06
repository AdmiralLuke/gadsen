package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.TileDestroyAction;
import com.gats.simulation.action.TileMoveAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

/**
 * Represents one of the Tiles the map is made of.
 * Special behaviors of certain Tile-Types will be implemented by sub-classes
 */
public class Tile implements Serializable, Cloneable {

    public static final int TileSizeX = 16;
    public static final int TileSizeY = 16;

    //The dimensions of a Tile in World-coordinates
    protected static final IntVector2 TileSize = new IntVector2(TileSizeX, TileSizeY);


    // Box als Ankerpunkt
    private boolean isAnchor;

    private final boolean isSolid;

    // Box hängt an einer Box oder an Verkettung von Boxen die an Anker hängt
    //ToDo: box should always be anchored, or removed from the map otherwise
    private boolean isAnchored;


    // Haltbarkeit der Box
    private int health = 250;

    private IntVector2 position;
    private GameState state;

    //ToDo what is that? This redundancy could seriously screw us
    Tile right;
    Tile up;
    Tile down;
    Tile left;


    public int getType() {
        return isAnchor ? 1 : 0;
    }

    /**
     * @return Position der Box im Spielbrett-Grid
     */
    public IntVector2 getPosition() {
        return this.position.cpy();
    }

    /**
     * Gibt die Position der Box in Welt-Koordinaten zurück
     *
     * @return Untere Linke Ecke der Box
     */
    public Vector2 getWorldPosition() {
        return this.position.toFloat().scl(TileSize.x, TileSize.y);
    }

    /**
     * Gibt die dimension einer Box zurück
     * @return Box-dimension als ganzzahliger 2D Vektor
     */
    public IntVector2 getTileSize(){return TileSize.cpy();}

    /**
     * Box wird nur aus Position erstellt. Definitiv kein Anker selbst
     *
     * @param x Position X
     * @param y Position Y
     */
    Tile(int x, int y, GameState state) {
        this.position = new IntVector2(x, y);
        this.isAnchor = false;
        this.state = state;
        this.isSolid = true;
        this.isAnchored = true;
        if (isAnchored) {
            state.getBoard()[x][y] = this;
            sortIntoTree();
        } else {
            // Die Garbage Collection wird das schon löschen
            state.getBoard()[x][y] = null;
        }
    }

    Tile(int x, int y, GameState state, boolean isAnchored) {
        this.position = new IntVector2(x, y);
        this.isAnchor = false;
        this.state = state;
        this.isSolid = true;
        this.isAnchored = isAnchored;
        if (isAnchored) {
            state.getBoard()[x][y] = this;
            sortIntoTree();
        } else {
            // Die Garbage Collection wird das schon löschen
            state.getBoard()[x][y] = null;
        }
    }

    private Tile(Tile original, GameState newState){
         isAnchor = original.isAnchor;

         isSolid = original.isSolid;

         isAnchored = original.isAnchored;

         health = original.health;

         position = original.position.cpy();

         state = newState;
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
        this.isSolid = true;
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

        if (this.left != null) {
            this.left.right = this;
        }
        if (this.right != null) {
            this.right.left = this;
        }
        if (this.up != null) {
            this.up.down = this;
        }
        if (this.down != null) {
            this.down.up = this;
        }


    }

    /**
     * Konstruktor zum Klonen von Tiles
     */
    Tile(boolean isAnchor, boolean isAnchored, int health) {
        this.isAnchor = isAnchor;
        this.isAnchored = isAnchored;
        this.isSolid = true;
        this.health = health;
    }

    /**
     * Schaut sich im Board Array an, ob Nachbarboxen verankert sind, oder selber Anker sind
     *
     * @return true wenn Nachbar anchored oder Anker ist
     * x
     * xax
     * x
     * a = verankert bzw. ankernd
     * x = kann angehangen werden
     */
    private boolean checkIfAnchored(int x, int y, GameState state) {
        boolean isAnchor = false;
        if (x > 0) {
            isAnchor = state.getTile(x - 1, y) != null || state.getTile(x + 1, y) != null;
        } else if (x == 0) {
            isAnchor = state.getTile(x + 1, y) != null;
        }
        if (y > 0 && !isAnchor) {
            isAnchor = state.getTile(x, y - 1) != null || state.getTile(x, y + 1) != null;
        } else if (y == 0 & !isAnchor) {
            isAnchor = state.getTile(x, y + 1) != null;
        }
        return isAnchor;
    }

    /**
     * entfernt eine Tile aus dem Graphen (und alle Referenzen)
     * GarbageColleciton goes huii
     */
    //ToDo this should never be public
    public void deleteFromGraph() {
        if (this.right != null) this.right.left = null;
        if (this.left != null) this.left.right = null;
        if (this.up != null) this.up.down = null;
        if (this.down != null) this.down.up = null;
    }

    /**
     * wenn die Box keinen Ankerpunkt hat, soll diese Simulation ausgeführt werden, bei der die Box solange fällt, bis sie im void
     * oder auf anderer Box landet
     */
    public Action onDestroy(Action head) {
        state.getSim().turnsWithoutAction = 0;
        ArrayList<Tile> rightList = new ArrayList<>();
        ArrayList<Tile> upperList = new ArrayList<>();
        ArrayList<Tile> lowerList = new ArrayList<>();
        ArrayList<Tile> leftList = new ArrayList<>();

        boolean[][] mapUp = new boolean[state.getBoardSizeX()][state.getBoardSizeY()];
        boolean[][] mapDown = new boolean[state.getBoardSizeX()][state.getBoardSizeY()];
        boolean[][] mapLeft = new boolean[state.getBoardSizeX()][state.getBoardSizeY()];
        boolean[][] mapRight = new boolean[state.getBoardSizeX()][state.getBoardSizeY()];



        state.getBoard()[this.position.x][this.position.y] = null;
        this.deleteFromGraph();
        TileDestroyAction destroyAction = new TileDestroyAction(this.position);
        head.addChild(destroyAction);
        if (hasRight())right.convertGraphToList(rightList, mapRight);
        if (hasUp()) up.convertGraphToList(upperList, mapUp);
        if (hasDown()) down.convertGraphToList(lowerList, mapDown);
        if (hasLeft()) left.convertGraphToList(leftList, mapLeft);


        if (!rightList.isEmpty()) checkForAnchor(rightList, head);
        if (!leftList.isEmpty()) checkForAnchor(leftList, head);
        if (!upperList.isEmpty()) checkForAnchor(upperList, head);
        if (!lowerList.isEmpty()) checkForAnchor(lowerList, head);
        for (GameCharacter[] characters : this.state.getTeams()) {
            for (GameCharacter character : characters) {
                if (character != null) {
                    character.fall(head);
                }
            }
        }

        return destroyAction;

    }

    void checkForAnchor(ArrayList<Tile> list, Action head) {
        for (Tile tile : list) {
            if (tile.isAnchor) {
                return;
            }
        }
        for (Tile tile : list) {
            state.getBoard()[tile.position.x][tile.position.y] = null;
            tile.destroyTile(head);
        }
        list.clear();
    }

    Action destroyTile(Action head) {
        IntVector2 posBef = this.position.cpy();
        int fallen = 0;
        while (getTileAtPosition(this.position.x, this.position.y - 1, state) == null && this.position.y > 0) {
            fallen++;
            this.position.add(0, -1);
            for (GameCharacter[] characters : state.getTeams()) {
                for (GameCharacter character : characters) {
                    if (character == null) {
                        continue;
                    }
                    if ((int) (character.getPlayerPos().x / 16) == this.position.x && (int) (character.getPlayerPos().y / 16) == this.position.y) {
                        //ToDo: fix, what happens when multiple characters are hit?
                        //LinearPath path = new LinearPath(posBef.toFloat().scl(tileSize.toFloat()), this.position.toFloat().scl(tileSize.toFloat()), 0.1f);
                        int oldHealth = character.getHealth();
                        Action moveAction = new TileMoveAction(posBef, this.position, 1f);
                        head.addChild(moveAction);
                        Action destroyAction = new TileDestroyAction(this.getPosition());
                        character.setHealth(oldHealth - fallen * 4, moveAction, true);
                        moveAction.addChild(destroyAction);
                        return destroyAction;
                    }
                }
            }
        }
        float duration = 1f;
        Action tileMoveAction = new TileMoveAction(posBef, this.position, duration);
        head.addChild(tileMoveAction);
        Action tileDestAction = new TileDestroyAction(this.getPosition());
        tileMoveAction.addChild(tileDestAction);

        return tileDestAction;
    }

    public Action destroyTileDirect(Action head) {
        this.deleteFromGraph();
        state.getBoard()[this.getPosition().x][this.getPosition().y] = null;
        Action tileDestAction = new TileDestroyAction(this.getPosition());
        head.addChild(tileDestAction);
        return tileDestAction;
    }

    /**
     * @param tiles ArrayList mit rekursiv aufbauend verbundenen Tiles
     * @param map   lookup-map um bereits besuchte Tiles zu markieren
     * @return ArrayList mit allen verbunden Tiles
     */
    protected boolean convertGraphToList(ArrayList<Tile> tiles, boolean[][] map) {
        if (tiles.contains(this)) return false;
        tiles.add(this);
        IntVector2 pos = this.getPosition();
        map[pos.x][pos.y] = true;
        if (this.isAnchor) return true;
        if (this.hasUp() && !map[pos.x][pos.y + 1]) {
            if (up.convertGraphToList(tiles, map)) return true;
        }
        if (pos.y > 0) {
            if (this.hasDown() && !map[pos.x][pos.y - 1]) {
                if (down.convertGraphToList(tiles, map)) return true;
            }
        }
        if (pos.x > 0) {
            if (this.hasLeft() && !map[pos.x - 1][pos.y]) {
                if (left.convertGraphToList(tiles, map)) return true;
            }
        }
        if (this.hasRight() && !map[pos.x + 1][pos.y]) {
            if (right.convertGraphToList(tiles, map)) return true;
        }
        return false;
    }

    Tile getTileAtPosition(int x, int y, GameState state) {
        return state.getTile(x, y);
    }

    /**
     * vergleicht 2 Tiles anhand der Position miteinander
     *
     * @param t weitere Tile mit der verglichen werden soll
     * @return true wenn gleiche Position sonst false
     */
    public boolean equals(Tile t) {
        return this.position.equals(t.position);
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

    boolean hasRight() {
        return right != null;
    }

    boolean hasLeft() {
        return left != null;
    }

    boolean hasUp() {
        return up != null;
    }

    boolean hasDown() {
        return down != null;
    }

    public boolean isSolid() {
        return isSolid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        Tile tile = (Tile) o;
        return isAnchor == tile.isAnchor && isAnchored == tile.isAnchored && getPosition().equals(tile.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAnchor, isAnchored, getPosition());
    }

    void setAnchor(boolean anchor) {
        this.isAnchor = anchor;
    }

    protected Tile copy(GameState state){
        return new Tile(this, state);
    }
}

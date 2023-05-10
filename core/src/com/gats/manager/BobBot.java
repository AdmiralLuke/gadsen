package com.gats.manager;
// package.bots;
// import com.gats.manager

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.simulation.Tile;
import com.gats.simulation.WeaponType;

import java.util.ArrayList;

public class BobBot extends Bot {

    /**
     * Hier gebt ihr euren Namen an
     * @return Format: Vorname Nachname
     */
    @Override
    public String getStudentName() {
        return "Mio Goodman";
    }

    /**
     * Hier gebt ihr eure Matrikelnummer an
     * @return eure Matrikelnummer
     */
    @Override
    public int getMatrikel() {
        return 42069;
    }

    /**
     * Hier könnt ihr eueren Bot einen (kreativen) Namen geben
     * @return Name des Bots
     */
    @Override
    public String getName() {
        return "Bob der Abreissmeister";
    }

    /**
     * Diese Methode wird beim Laden der Map aufgerufen. Ideal um gegebenfalls Werte zu initialisieren
     * @param state Der {@link GameState Spielzustand} zu Beginn des Spiels
     */
    @Override
    protected void init(GameState state) {

    }

    public ArrayList<Tile> healthBoxes = new ArrayList<>();
    public ArrayList<Tile> anchorTiles = new ArrayList<>();

    /**
     * Diese Methode beschreibt den Zug den eure Gadse macht
     * @param state Der {@link GameState Spielzustand} während des Zuges -> Spielinformationen
     * @param controller Der {@link Controller Controller}, zum Charakter gehört, welcher am Zug ist -> Charaktersteuerung
     */
    @Override
    protected void executeTurn(GameState state, Controller controller) {
        if (controller.getGameCharacter().getHealth() == 100) {
            int team = controller.getGameCharacter().getTeam();
            for (int i = 0; i < state.getTeamCount(); i++) {
                if (i != team) {
                    for (int j = 0; j < state.getCharactersPerTeam(); j++) {
                        GameCharacter character = state.getCharacterFromTeams(i, j);
                        if (character.isAlive()) {
                            Vector2 an = getAnchorBox(state, character.getPlayerPos());
                            Vector2 sP = controller.getGameCharacter().getPlayerPos();
                            Vector2 dir = calcParableDir(sP, an, 1.0f);
                            controller.selectWeapon(WeaponType.WATER_PISTOL);
                            controller.aim(dir, 1.0f);
                            controller.shoot();
                        }
                    }
                }
            }
        } else {
            Vector2 wpBox = getHealthBox(state, controller.getGameCharacter().getPlayerPos());
            Vector2 sP = controller.getGameCharacter().getPlayerPos();
            Vector2 dir = calcParableDir(sP, wpBox, 1.0f);
            controller.selectWeapon(WeaponType.WATER_PISTOL);
            controller.aim(dir, 1.0f);
            controller.shoot();
        }
    }

    private ArrayList<Tile> findBoxType(GameState state, Tile.TileType type) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                if (state.getTile(i, j) != null && state.getTile(i, j).getTileType() == type) {
                    tiles.add(state.getTile(i, j)) ;
                }
            }
        }
        return tiles;
    }

    private ArrayList<Tile> findAnchor(GameState state) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                if (state.getTile(i, j) != null && state.getTile(i, j).isAnchor()) {
                    tiles.add(state.getTile(i, j)) ;
                }
            }
        }
        return tiles;
    }

    private Vector2 getHealthBox(GameState state, Vector2 position) {
        healthBoxes = findBoxType(state, Tile.TileType.HEALTH_BOX);
        Tile nearest = healthBoxes.get(0);
        float nD = Float.MAX_VALUE;
        for (Tile t : healthBoxes) {
            float d = Math.abs(t.getWorldPosition().sub(position.cpy()).len());
            if (d < nD) {
                nD = d;
                nearest = t;
            }
        }
        return nearest.getWorldPosition();
    }

    private Vector2 getAnchorBox(GameState state, Vector2 position) {
        anchorTiles = findAnchor(state);
        Tile n = anchorTiles.get(0);
        float nD = Float.MIN_VALUE;
        for (Tile t : anchorTiles) {
            float d = Math.abs(t.getWorldPosition().sub(position.cpy()).len());
            if (d < nD) {
                nD = d;
                n = t;
            }
        }
        return n.getWorldPosition();
    }

    private static Vector2 calcParableDir(Vector2 startPos, Vector2 endPos, float strength) {
        float dx = endPos.x - startPos.x;
        float dy = endPos.y - startPos.y;
        if (dy < 0 || dy > 16) endPos.y += 16; dy = endPos.y - startPos.y;

        strength -= 0.2;

        float th = (float) ((dy / dx) + ((9.81 * 8) * dx) / (2 * (400 * strength) * (400 * strength) * Math.cos(Math.atan(dy / dx))));
        float arcTh = (float)Math.atan(th);

        float thDeg = (float)Math.toDegrees(arcTh);
        return dx < 0 ? new Vector2(-1, 0).rotateDeg(thDeg) : new Vector2(1, 0).rotateDeg(thDeg);
    }
}



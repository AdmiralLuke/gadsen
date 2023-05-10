package com.gats.manager;
// package.bots;
// import com.gats.manager

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.simulation.Tile;
import com.gats.simulation.WeaponType;

import java.util.ArrayList;

public class KamigadseBot extends Bot {

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
        return "Kamigadse";
    }

    /**
     * Diese Methode wird beim Laden der Map aufgerufen. Ideal um gegebenfalls Werte zu initialisieren
     * @param state Der {@link GameState Spielzustand} zu Beginn des Spiels
     */
    @Override
    protected void init(GameState state) {
        weaponTiles = new ArrayList<>();
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                if (state.getTile(i, j) != null && state.getTile(i, j).getTileType() == Tile.TileType.WEAPON_BOX) {
                    weaponTiles.add(state.getTile(i, j)) ;
                }
            }
        }
    }

    public ArrayList<Tile> weaponTiles = new ArrayList<>();

    /**
     * Diese Methode beschreibt den Zug den eure Gadse macht
     * @param state Der {@link GameState Spielzustand} während des Zuges -> Spielinformationen
     * @param controller Der {@link Controller Controller}, zum Charakter gehört, welcher am Zug ist -> Charaktersteuerung
     */
    @Override
    protected void executeTurn(GameState state, Controller controller) {
        int team = controller.getGameCharacter().getTeam();
        if (controller.getGameCharacter().getWeapon(3).getAmmo() > 0) {
            for (int i = 0; i < state.getTeamCount(); i++) {
                if (i != team) {
                    for (int j = 0; j < state.getCharactersPerTeam(); j++) {
                        GameCharacter character = state.getCharacterFromTeams(i, j);
                        if (character.isAlive()) {
                            Vector2 st = controller.getGameCharacter().getPlayerPos();
                            Vector2 e = character.getPlayerPos();
                            Vector2 dir = calcParableDir(st, e, 1f);
                            if (Float.isNaN(dir.x)) dir = new Vector2(1, 0);
                            controller.selectWeapon(WeaponType.GRENADE);
                            controller.aim(dir, 1f);
                            controller.shoot();
                            return;
                            // System.out.println("try to shoot from " + st + " to " + e + " in " + dir);
                        }
                    }
                }
            }
        } else {
            Vector2 wpBox = getWeaponBox(state, controller.getGameCharacter().getPlayerPos());
            Vector2 sP = controller.getGameCharacter().getPlayerPos();
            Vector2 dir = calcParableDir(sP, wpBox, 1.0f);
            controller.selectWeapon(WeaponType.WATER_PISTOL);
            controller.aim(dir, 1.0f);
            controller.shoot();
        }
    }

    private void findWeaponBoxes(GameState state) {
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                if (state.getTile(i, j) != null && state.getTile(i, j).getTileType() == Tile.TileType.WEAPON_BOX) {
                    weaponTiles.add(state.getTile(i, j)) ;
                }
            }
        }
    }

    private Vector2 getWeaponBox(GameState state, Vector2 position) {
        findWeaponBoxes(state);
        Tile nearest = weaponTiles.get(0);
        float nD = Float.MAX_VALUE;
        for (Tile t : weaponTiles) {
            float d = Math.abs(t.getWorldPosition().sub(position.cpy()).len());
            if (d < nD) {
                nD = d;
                nearest = t;
            }
        }

        return nearest.getWorldPosition();
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



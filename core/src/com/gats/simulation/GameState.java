package com.gats.simulation;

/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 * wie z.B. Spielmodus, {@link GameCharacter Spielfiguren} und Zustand der Map.
 * Diese Daten sind meist in weiteren Klassen wie z.B. {@link Tile} gekapselt
 */
public class GameState {

    private static final int GAME_MODE_NORMAL = 0;

    private int gameMode = GAME_MODE_NORMAL;
    private Tile[][] map;
    private GameCharacter[] characters;

    GameState(int gameMode, String mapName) {
        this.gameMode = gameMode;
    }

    private void loadMap(String mapName){
    }

}

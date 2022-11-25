package com.gats.simulation;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 * wie z.B. Spielmodus, {@link GameCharacter Spielfiguren} und Zustand der Map.
 * Diese Daten sind meist in weiteren Klassen wie z.B. {@link Tile} gekapselt
 */
public class GameState {

    // Spielbrett
    // x - Spalten
    // y - Zeile
    private Tile[][] board;

    private int width;
    private int height;
    private static final int GAME_MODE_NORMAL = 0;


    private int gameMode = GAME_MODE_NORMAL;

    private int activeTeam = 0;

    // Teams   Anzahl Teams x Anzahl Player
    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;


    GameState(int gameMode, String mapName) {
        this.gameMode = gameMode;
        this.teamCount = 2;
        this.charactersPerTeam = 1;
        loadMap(mapName);
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
        this.initTeam();
    }

    GameState(int gameMode, String mapName, int teamCount, int charactersPerTeam) {
        this.gameMode = gameMode;
        loadMap(mapName);
        this.teamCount = teamCount;
        this.charactersPerTeam = charactersPerTeam;
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
        this.initTeam();
    }

    /**
     * spawns player
     */
    void initTeam() {
        for (int i = 0; i < this.teams.length; i++) {
            for (int j = 0; j < this.teams[0].length; j++) {
                this.teams[i][j] = new GameCharacter(i, j, this);
            }
        }
    }

    private void loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        width = map.get("width").asInt();
        height = map.get("height").asInt();
        board = new Tile[width][height];

        JsonValue tileData = map.get("layers").get(0).get("data");

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height-j -1) * width).asInt();
                switch (type) {
                    case 1:
                        board[i][j] = new Tile(i, j, true, this);
                        break;
                    case 2:
                        board[i][j] = new Tile(i, j, this, true);
                        break;
                }
            }
        }
    }

    /**
     * Gibt einen bestimmten Spieler aus einem bestimmten Team zurück
     *
     * @param team   Team-Nummer
     * @param member Spieler-Nummer im Team
     * @return ausgewählter Spieler im ausgewählten Team
     */
    public GameCharacter getCharacterFromTeams(int team, int member) {
        return teams[team][member];
    }

    public int getTeamCount() {
        return teamCount;
    }

    public int getCharactersPerTeam() {
        return charactersPerTeam;
    }



    /**
     * Board for devs (changes are permanent)
     * @return board
     */
    Tile[][] getBoard() {
        return board;
    }

    /**
     * Abfrage von Tiles von einem GameState
     *
     * @return Kopie eines Tiles an einer bestimmten Stelle
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x > 249 || y > 249) return null;
        return board[x][y];
    }

    /**
     * @return X Size of the map in tiles
     */
    public int getBoardSizeX() {
        return board.length;
    }

    /**
     * @return Y Size of the map in tiles
     */
    public int getBoardSizeY() {
        return board.length == 0 ? 0 : board[0].length;
    }

    GameCharacter[][] spawnCharacter(int amountTeams, int amountPlayers) {
        GameCharacter[][] characters = new GameCharacter[amountTeams][amountPlayers];
        for (int i = 0; i < amountTeams; i++) {
            for (int j = 0; j < amountPlayers; j++) {
                int randX = (int)(Math.random() * 250);
                int randY = (int)(Math.random() * 250);
                if (getTile(randX, randY) != null) {
                    j--;
                } else {
                    characters[i][j] = new GameCharacter(randX, randY, this);
                }
            }
        }
        return characters;
    }
}

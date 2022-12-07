package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayDeque;


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
    private static final int GAME_MODE_CHRISTMAS = 1;

    private int gameMode = GAME_MODE_NORMAL;

    private int activeTeam = 0;

    // Teams   Anzahl Teams x Anzahl Player
    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;
    private ArrayDeque<Vector2> turn;
    private boolean active;
    private Simulation sim;

    GameState(int gameMode, String mapName, Simulation sim) {
        new GameState(gameMode, mapName, 2, 1, sim);
    }

    /**
     * Erstellt einen GameState
     * @param gameMode
     * @param mapName
     * @param teamCount
     * @param charactersPerTeam
     * @param sim
     */
    GameState(int gameMode, String mapName, int teamCount, int charactersPerTeam, Simulation sim) {
        this.gameMode = gameMode;
        this.active = true;
        this.sim = sim;
        loadMap(mapName);
        this.teamCount = teamCount;
        this.charactersPerTeam = charactersPerTeam;
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
    }

    /**
     * spawns player
     */
    void initTeam() {
        this.turn = new ArrayDeque<>(this.teams.length * this.teams[0].length);
        this.teams = spawnCharacter(this.teams.length, this.teams[0].length);
        // Vector der Queue: (x = Team Nummer | y = Character Nummer im Team)
        for (int i = 0; i < this.teams.length; i++) {
            for (int j = 0; j < this.teams[0].length; j++) {
                turn.push(new Vector2(this.teams[i][j].getTeam(), this.teams[i][j].getTeamPos()));
            }
        }
    }

    protected boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected ArrayDeque<Vector2> getTurn() {
        return turn;
    }

    protected Simulation getSim() {
        return sim;
    }

    protected GameCharacter[][] getTeams() {
        return teams;
    }

    /**
     * Lädt eine Map aus dem Assets Ordner
     * Annahme: Alle Tiles auf der Map sind verankert
     * @param mapName Map im Json Format aus dem Assets Ordner
     */
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
        if (x < 0 || y < 0 || x > getBoardSizeX() || y > getBoardSizeY()) return null;
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

    /**
     * Spawnt Spieler an zufälligen Positionen
     * @param amountTeams Anzahl an Teams
     * @param amountPlayers Anzahl Spieler pro Team
     * @return Team Array
     */
    GameCharacter[][] spawnCharacter(int amountTeams, int amountPlayers) {
        GameCharacter[][] characters = new GameCharacter[amountTeams][amountPlayers];
        for (int i = 0; i < amountTeams; i++) {
            for (int j = 0; j < amountPlayers; j++) {
                int randX = (int)(Math.random() * getBoardSizeX());
                int randY = (int)(Math.random() * getBoardSizeY());
                if (getTile(randX, randY) != null) {
                    j--;
                } else {
                    characters[i][j] = new GameCharacter(randX, randY, this, i, j, sim);
                }
            }
        }
        return characters;
    }
}

package com.gats.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Paths;
import java.util.*;


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
    public static final int GAME_MODE_NORMAL = 0;
    public static final int GAME_MODE_CHRISTMAS = 1;

    private int gameMode = GAME_MODE_NORMAL;

    // Teams   Anzahl Teams x Anzahl Player
    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;
    private final ArrayDeque<IntVector2> turn = new ArrayDeque<>();
    private boolean active;
    private Simulation sim;

    GameState(int gameMode, String mapName, Simulation sim) {
        new GameState(gameMode, mapName, 2, 1, sim);
    }

    /**
     * Erstellt einen GameState
     *
     * @param gameMode Modus
     * @param mapName Map Name
     * @param teamCount Anzahl Teams
     * @param charactersPerTeam Anzahl Charaktere pro Team
     * @param sim Simulation
     */
    GameState(int gameMode, String mapName, int teamCount, int charactersPerTeam, Simulation sim) {
        this.gameMode = gameMode;
        List<IntVector2> spawnpoints = loadMap(mapName);
        this.teamCount = teamCount;
        this.charactersPerTeam = charactersPerTeam;
        this.active = true;
        this.sim = sim;
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
        this.initTeam(spawnpoints);

    }

    int getGameMode() {
        return gameMode;
    }

    /**
     * spawns player
     */
    void initTeam(List<IntVector2> spawnpoints) {
        if (gameMode == GAME_MODE_CHRISTMAS) {
            spawnpoints.sort(Comparator.comparingInt(v -> v.x));
            for (int i = 0; i < 4; i++) {
                IntVector2 pos = spawnpoints.get(i).scl(Tile.TileSize);
                this.teams[i][0] = new GameCharacter(pos.x, pos.y, this, i, 0, sim);
                turn.add(new IntVector2(i, 0));
            }
            return;
        }
        int pointCount = spawnpoints.size();
        if (pointCount < teamCount * charactersPerTeam)
            throw new RuntimeException(String.format(
                    "Requested %d x %d Characters, but the selected map has only %d spawning locations",
                    teamCount, charactersPerTeam, pointCount));
        Random rnd = new Random();
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < charactersPerTeam; j++) {
                int index = rnd.nextInt(pointCount--);
                IntVector2 pos = spawnpoints.remove(index).scl(Tile.TileSize);
                this.teams[i][j] = new GameCharacter(pos.x, pos.y, this, i, j, sim);
            }
        }
        // Vector der Queue: (x = Team Nummer | y = Character Nummer im Team)
        for (int j = 0; j < charactersPerTeam; j++) {
            for (int i = 0; i < teamCount; i++) {
                turn.add(new IntVector2(i, j));
            }
        }
    }

    //ToDo migrate to Simulation
    /**
     * Gibt zurück, ob das Spiel noch läuft. Ist während der Ausführung immer true und wird nur für interne Zwecke verwendet
     * @return True, wenn das Spiel noch nicht beendet ist
     */
    public boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected ArrayDeque<IntVector2> getTurn() {
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
     *
     * @param mapName Map im Json Format aus dem Assets Ordner
     */
    private List<IntVector2> loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map;
        try{
            //attempt to load map from jar
            map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        }catch (Exception e){
            map =null;
        }
        if(map ==null){
           try {
               //attempt to load map from external maps dir
               map = reader.parse(new FileHandle(Paths.get("./maps/"+mapName+".json").toFile()));
           }catch (Exception e){
             throw new RuntimeException("Could not find or load map:"+mapName);
           }
        }

        width = map.get("width").asInt();
        height = map.get("height").asInt();
        board = new Tile[width][height];

        JsonValue tileData = map.get("layers").get(0).get("data");

        List<IntVector2> spawnpoints = new LinkedList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                switch (type) {
                    case 1:
                        board[i][j] = new Tile(i, j, true, this);
                        break;
                    case 2:
                        board[i][j] = new Tile(i, j, this, true);
                        break;
                    case 3:
                        spawnpoints.add(new IntVector2(i, j));
                }
            }
        }
        return spawnpoints;
    }

    /**
     * Gibt einen bestimmten Charakter aus einem bestimmten Team zurück
     *
     * @param team   Team-Index
     * @param member Charakter-Index im Team
     * @return ausgewählter Charakter im ausgewählten Team
     */
    public GameCharacter getCharacterFromTeams(int team, int member) {
        return teams[team][member];
    }

    /**
     * @return Anzahl der Teams/Spieler
     */
    public int getTeamCount() {
        return teamCount;
    }

    /**
     * @return Anzahl der Charaktere pro Team
     */
    public int getCharactersPerTeam() {
        return charactersPerTeam;
    }


    /**
     * Board for devs (changes are permanent)
     *
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
     * @return Horizontale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeX() {
        return width;
    }

    /**
     * @return Vertikale Größe des Spielfeldes in #Boxen
     */
    public int getBoardSizeY() {
        return height;
    }

    //ToDo: discuss removal
    /**
     * Spawnt Spieler an zufälligen Positionen
     *
     * @param amountTeams   Anzahl an Teams
     * @param amountPlayers Anzahl Spieler pro Team
     * @return Team Array
     */
    GameCharacter[][] spawnCharacter(int amountTeams, int amountPlayers) {
        GameCharacter[][] characters = new GameCharacter[amountTeams][amountPlayers];
        for (int i = 0; i < amountTeams; i++) {
            for (int j = 0; j < amountPlayers; j++) {
                int randX = (int) (Math.random() * getBoardSizeX());
                int randY = (int) (Math.random() * getBoardSizeY());
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

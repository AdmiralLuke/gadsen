package com.gats.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gats.manager.Timer;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;


/**
 * Repräsentiert ein laufendes Spiel mit allen dazugehörigen Daten
 * wie z.B. Spielmodus, {@link GameCharacter Spielfiguren} und Zustand der Map.
 * Diese Daten sind meist in weiteren Klassen wie z.B. {@link Tile}, {@link GameCharacter}, {@link com.gats.simulation.weapons.Weapon} etc. gekapselt.
 * Diese Datenstruktur (zusammen mit den weiteren Klassen, auf die ihr hier Zugriff erhaltet) bietet euch alle Daten,
 * die ihr benötigt und erhalten könnt, um Entscheidungen in eurem Spielzug zu treffen.
 * Diese Entscheidungen gebt ihr anschliessend über die {@link com.gats.manager.Controller Controller}-Instanz an,
 * die ihr parallel zu diesem GameState erhalten habt. Mehr Infos dazu findet ihr in der Dokumentation des {@link com.gats.manager.Controller}.
 * Alle Informationen, die für euch direkt relevant sind, sind in Deutsch verfasst. Solltet ihr also auf
 * Dokumentation stoßen, welche in Englisch verfasst ist, seid ihr auf unsere interne Dokumentation für
 * Funktionen, auf die ihr keinen Zugriff habt, gestoßen.
 * Natürlich könnt ihr diese trotzdem durchlesen, wenn euch interessiert wie unser Spiel von innen
 * funktioniert oder ihr euch anschauen möchtet, wie wir bestimmte Probleme gelöst haben.
 */
public class GameState implements Serializable {

    // Spielbrett
    // x - Spalten
    // y - Zeile
    private Tile[][] board;

    private int width;
    private int height;

    public float[] getScores() {
        //ToDo implement
        return new float[4];
    }

    public GameState copy() {
        return new GameState(this);
    }

    private GameState(GameState original) {
        board = new Tile[original.width][original.height];
        Tile[][] tiles = original.board;
        for (int i = 0; i < tiles.length; i++) {
            Tile[] row = tiles[i];
            for (int j = 0; j < row.length; j++) {
                board[i][j] = row[j].copy(this);
            }
        }
        width = original.width;
        height = original.height;
        gameMode = original.gameMode;
        turnTimer = original.turnTimer;
        teams = new GameCharacter[original.teamCount][original.charactersPerTeam];
        GameCharacter[][] gameCharacters = original.teams;
        for (int i = 0; i < gameCharacters.length; i++) {
            GameCharacter[] team = gameCharacters[i];
            for (int j = 0; j < team.length; j++) {
                teams[i][j] = team[j].copy(this);
            }
        }
        teamCount = original.teamCount;
        charactersPerTeam = original.charactersPerTeam;
        turn = null;
        active = original.active;
        sim = null;
    }

    public enum GameMode {
        Normal,
        Campaign,

        Exam_Admission,
        Tournament_Phase_1,
        Tournament_Phase_2,
        Christmas
    }

    private GameMode gameMode = GameMode.Normal;


    private transient Timer turnTimer;

    // Teams   Anzahl Teams x Anzahl Player
    private GameCharacter[][] teams;

    private int teamCount;
    private int charactersPerTeam;
    private ArrayDeque<IntVector2> turn;
    private boolean active;
    private transient Simulation sim;


    /**
     * Creates a new GameState for the specified attributes.
     *
     * @param gameMode          selected game mode
     * @param mapName           name of the selected map as String
     * @param teamCount         number of teams/players
     * @param charactersPerTeam number of Characters per team
     * @param sim               the respective simulation instance
     */
    GameState(GameMode gameMode, String mapName, int teamCount, int charactersPerTeam, Simulation sim) {
        this.gameMode = gameMode;
        List<IntVector2> spawnpoints = loadMap(mapName);
        this.teamCount = teamCount;
        this.charactersPerTeam = charactersPerTeam;
        this.active = true;
        this.sim = sim;
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
        this.initTeam(spawnpoints);
        this.turn = new ArrayDeque<>();
    }

    /**
     * Gibt den Spiel-Modus des laufenden Spiels zurück.
     *
     * @return Spiel-Modus als int
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Spawns players randomly distributed over the possible spawn-location, specified by the map.
     */
    void initTeam(List<IntVector2> spawnpoints) {
        if (gameMode == GameMode.Christmas) {
            //ToDo: remove Christmas Mode
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
     * Return whether the Game is still active.
     *
     * @return True, if the game is still in progress.
     */
    public boolean isActive() {
        return active;
    }

    //ToDo migrate to Simulation
    protected void deactivate() {
        this.active = false;
    }

    /**
     * @return the Queue that saves the order Characters may act in
     */
    protected ArrayDeque<IntVector2> getTurn() {
        return turn;
    }

    /**
     * @return the respective simulation instance
     */
    protected Simulation getSim() {
        return sim;
    }

    /**
     * @return the 2D array containing all Characters sorted by their team and index within the team
     */
    protected GameCharacter[][] getTeams() {
        return teams;
    }

    /**
     * ToDo: move to separate class
     * Loads a Map from the asset-directory
     * Assumes that all Tiles on the map are directly or indirectly anchored.
     * The Map file has t be encoded in JSON.
     *
     * @param mapName Name of the map without type as String
     */
    private List<IntVector2> loadMap(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map;
        try {
            //attempt to load map from jar
            map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        } catch (Exception e) {
            map = null;
        }
        if (map == null) {
            try {
                //attempt to load map from external maps dir
                map = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
            } catch (Exception e) {
                throw new RuntimeException("Could not find or load map:" + mapName);
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
     * @param team   Index des Teams zu dem der Charakter gehört
     * @param member Index des Charakters im Team
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
     * @return The 2D array that saves all Tiles
     */
    Tile[][] getBoard() {
        return board;
    }

    /**
     * Gibt die {@link Tile Box} an einer bestimmten Position zurück
     *
     * @param x X-Komponente der Position in Tile-Koordinaten
     * @param y Y-Komponente der Position in Tile-Koordinaten
     * @return Box an der gewählten Position
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= getBoardSizeX() || y >= getBoardSizeY()) return null;
        return board[x][y];
    }

    public Tile getTile(IntVector2 pos) {
        return getTile(pos.x, pos.y);
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

    public Timer getTurnTimer() {
        return turnTimer;
    }

    protected void setTurnTimer(Timer turnTimer) {
        this.turnTimer = turnTimer;
    }


}

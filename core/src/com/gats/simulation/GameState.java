package com.gats.simulation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gats.manager.Timer;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.ScoreAction;
import com.gats.simulation.campaign.CampaignResources;
import com.gats.simulation.weapons.Weapon;

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

    private final boolean winnerTakesAll;

    private final float[] scores;

    private final int[] weaponBoxCycle;

    public float[] getScores() {
        return Arrays.copyOf(scores, scores.length);
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
                board[i][j] = row[j] == null ? null : row[j].copy(this);
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
                teams[i][j] = team[j] == null ? null : team[j].copy(this);
            }
        }
        weaponBoxCycle = new int[original.teamCount];
        System.arraycopy(original.weaponBoxCycle, 0, weaponBoxCycle, 0, original.teamCount);
        winnerTakesAll = original.winnerTakesAll;
        teamCount = original.teamCount;
        charactersPerTeam = original.charactersPerTeam;
        turn = null;
        active = original.active;
        sim = null;
        scores = Arrays.copyOf(original.scores, original.scores.length);
    }

    public enum GameMode {
        Normal,
        Campaign,

        Exam_Admission,
        Tournament_Phase_1,
        Tournament_Phase_2,
        Replay
    }

    private final GameMode gameMode;
    private String mapName;


    private transient Timer turnTimer;

    // Teams   Anzahl Teams x Anzahl Player
    private final GameCharacter[][] teams;

    private final int teamCount;
    private final int charactersPerTeam;
    private final ArrayDeque<IntVector2> turn;
    private boolean active;
    private final transient Simulation sim;


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
        this.mapName = mapName;
        List<List<IntVector2>> spawnpoints = loadMap(gameMode == GameMode.Campaign ? "campaign/" + mapName : mapName);
        this.teamCount = teamCount;
        this.charactersPerTeam = charactersPerTeam;
        this.active = true;
        this.sim = sim;
        this.teams = new GameCharacter[teamCount][charactersPerTeam];
        this.turn = new ArrayDeque<>();
        this.weaponBoxCycle = new int[teamCount];
        this.scores = new float[teamCount];
        this.winnerTakesAll = gameMode == GameMode.Campaign || gameMode == GameMode.Tournament_Phase_2;
        this.initTeam(spawnpoints);
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
    void initTeam(List<List<IntVector2>> spawnpoints) {

        int typeCount = spawnpoints.size();
        if (typeCount < teamCount)
            throw new RuntimeException(String.format(
                    "Requested %d Teams, but the selected map only supports %d different teams",
                    teamCount, typeCount));
        Random rnd = new Random();
        ArrayList<int[]> weapons;
        ArrayList<int[]> health;
        if (gameMode == GameMode.Campaign) {
            weapons = CampaignResources.getWeaponCounts(this.mapName);
            health = CampaignResources.getHealth(this.mapName);
        } else {
            weapons = new ArrayList<>();
            health = new ArrayList<>();
        }
        for (int i = 0; i < teamCount; i++) {
            Weapon[] inventory = GameCharacter.initInventory(sim, weapons.size() > i ? weapons.get(i) : null, teamCount, charactersPerTeam);
            int[] characterHealth = health.size() > i ? health.get(i) : new int[0];
            List<IntVector2> teamSpawns = spawnpoints.get(i);
            if (teamSpawns.size() < charactersPerTeam)
                throw new RuntimeException(String.format(
                        "Requested %d Characters, but the selected map only supports %d different characters for team %d",
                        charactersPerTeam, teamSpawns.size(), i));
            for (int j = 0; j < charactersPerTeam; j++) {
                IntVector2 pos = teamSpawns.get(j).scl(Tile.TileSize).add(Math.round((16 - GameCharacter.getSize().x) / 2), 0);
                this.teams[i][j] = new GameCharacter(pos.x, pos.y, this, i, j, inventory, characterHealth.length > j ? characterHealth[j] : 100, sim);
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

    protected Action addScore(Action head, int team, float score) {
        if (!winnerTakesAll) {

            scores[team] += score;
        } else if (score == Simulation.SCORE_WIN[0]) {
            scores[team] = 1;
        }else
            return head;
        ScoreAction scoreAction = new ScoreAction(0, team, -1, scores[team]);
        head.addChild(scoreAction);
        return scoreAction;
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
    private List<List<IntVector2>> loadMap(String mapName) {
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

       // List<List<IntVector2>> spawnpoints = new LinkedList<>();
        Map<Integer,List<IntVector2>> teams=new TreeMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int type = tileData.get(i + (height - j - 1) * width).asInt();
                if (type > 100) {
                    //int team = type - 101; //teams starting at 0
                    if(teams.containsKey(type)){
                        teams.get(type).add(new IntVector2(i,j));
                    }
                    else{teams.put(type,new LinkedList<>());teams.get(type).add(new IntVector2(i,j));}
                    //while (spawnpoints.size() <= team)
                    //    spawnpoints.add(new LinkedList<>()); //Increase list of spawnpoints as necessary
                    //spawnpoints.get(team).add(new IntVector2(i, j)); // Add current tile
                } else
                    switch (type) {
                        case 0:
                            break;
                        case 1:
                            board[i][j] = new Tile(i, j, this, true);
                            break;
                        case 4:
                            board[i][j] = new Tile(i, j, this, false, Tile.TileType.WEAPON_BOX);
                            break;
                        case 5:
                            board[i][j] = new Tile(i, j, this, true, Tile.TileType.WEAPON_BOX);
                            break;
                        case 6:
                            board[i][j] = new Tile(i, j, this, false, Tile.TileType.HEALTH_BOX);
                            break;
                        default:
                            board[i][j] = new Tile(i, j, this, false);
                    }
            }
        }

        List<List<IntVector2>> spawns = new LinkedList<>(teams.values());

        return spawns;

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

    public int getWeaponBoxCycle(int team) {
        return weaponBoxCycle[team];
    }

    public static int getWeaponFromCycleIndex(int index) {
        if (index < 0) return -1;
        index = index % 10;
        switch (index) {
            case 0:
            case 4:
                return WeaponType.WOOL.ordinal();
            case 9:
                return WeaponType.MIOJLNIR.ordinal();
            case 1:
            case 5:
            case 7:
                return WeaponType.WATERBOMB.ordinal();
            case 3:
            case 6:
                return WeaponType.CLOSE_COMBAT.ordinal();
            case 2:
            case 8:
                return WeaponType.GRENADE.ordinal();
        }
        return -1;
    }

    protected int cycleWeapon(int team) {
        int result = weaponBoxCycle[team];
        weaponBoxCycle[team] = (weaponBoxCycle[team] + 1) % 10;
        return result;
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

    public Timer getTurnTimer() {
        return turnTimer;
    }

    protected void setTurnTimer(Timer turnTimer) {
        this.turnTimer = turnTimer;
    }


}

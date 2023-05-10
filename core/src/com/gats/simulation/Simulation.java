package com.gats.simulation;

import com.gats.manager.Timer;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.GameOverAction;
import com.gats.simulation.action.InitAction;
import com.gats.simulation.action.TurnStartAction;
import com.gats.simulation.GameState.GameMode;

import java.util.*;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {

    public static final float SCORE_KILL = 50;
    public static final float SCORE_ELIMINATION = 50;
    public static final float[] SCORE_WIN = new float[]{200, 100, 50};
    public static final float SCORE_ASSIST = 25;
    private final GameState gameState;
    private ActionLog actionLog;
    private final Wrapper wrapper;

    private int remainingTeams;
    int turnsWithoutAction = 0;

    /**
     * erstellt eine neue Simulation
     *
     * @param gameMode Modus in dem gespielt wird
     * @param mapName  Map auf der gespielt wird
     * @param teamAm   Anzahl Teams
     * @param teamSize Anzahl Charaktere pro Team
     */
    public Simulation(GameMode gameMode, String mapName, int teamAm, int teamSize) {
        gameState = new GameState(gameMode, mapName, teamAm, teamSize, this);
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        actionLog = new ActionLog(new TurnStartAction(0, turnChar.x, turnChar.y));
        remainingTeams = teamAm;
        this.wrapper = new Wrapper(gameState.getTeams());
    }

    public static IntVector2 convertToTileCoords(IntVector2 worldCoords) {
        return new IntVector2(convertToTileCoordsX(worldCoords.x), convertToTileCoordsY(worldCoords.y));
    }

    public Wrapper getWrapper() {
        return this.wrapper;
    }

    public static int convertToTileCoordsX(int x) {
        return x / 16;
    }

    public static int convertToTileCoordsY(int y) {
        return y / 16;
    }

    /**
     * gibt den aktuellen GameState zurück
     *
     * @return aktueller GameState
     */
    public GameState getState() {
        return gameState;
    }

    ActionLog getActionLog() {
        return actionLog;
    }

    public void setTurnTimer(Timer timer) {
        gameState.setTurnTimer(timer);
    }

    public GameCharacterController getController() {
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        return new GameCharacterController(gameState.getCharacterFromTeams(turnChar.x, turnChar.y), gameState);
    }


    public ActionLog endTurn() {
        turnsWithoutAction++;

        int activeTeam = getActiveTeam();

        ArrayDeque<IntVector2> turn = gameState.getTurn();
        int teamCount = gameState.getTeamCount();
        int[] remainingCharacters = new int[teamCount];
        boolean[] lostChar = new boolean[teamCount];

        turn.add(turn.pop());

        for (Iterator<IntVector2> iterator = turn.iterator(); iterator.hasNext(); ) {
            IntVector2 cur = iterator.next();
            if (gameState.getCharacterFromTeams(cur.x, cur.y).isAlive()) {
                remainingCharacters[cur.x]++;
            } else {
                iterator.remove();
                lostChar[cur.x] = true;
            }
        }

        boolean[] eliminated = new boolean[teamCount];
        int teamKills = 0;
        int remaining = 0;

        for (int i = 0; i < teamCount; i++) {
            if (lostChar[i] && remainingCharacters[i] == 0) {
                teamKills++;
                eliminated[i] = true;
                remainingTeams--;
                //Reward points for team eliminations
                if (i != activeTeam) {
                    gameState.addScore(activeTeam, SCORE_ELIMINATION);
                }
            } else if (remainingCharacters[i] > 0) remaining++;
        }

        //Reward points for placement
        //Example 1: 2 remaining teams; both die in the same round => each team receives (SCORE_WIN + SCORE_SECOND)/2f
        //Example 2: 3 remaining teams; 2 die in the same round; one remains => eliminates teams receive (SCORE_SECOND + SCORE_THIRD)/2f each; winning Team receives SCORE_WIN
        //Example 3: 3 remaining teams; all die in the same round => each team receives (SCORE_WIN + SCORE_SECOND + SCORE_THIRD)/3f
        //Example 4: 4 remaining teams; 2 die in the same round => each eliminated team receives (SCORE_THIRD)/2f; remaining teams continue playing
        if (teamKills > 0) {
            float scoreSum = 0;
            for (int i = remaining; i < SCORE_WIN.length; i++) {
                scoreSum += SCORE_WIN[i];
            }
            float score = scoreSum/teamKills;
            for (int i = 0; i < teamCount; i++) {
                if (eliminated[i]) gameState.addScore(i, score);
            }
        }

        if (remainingTeams <= 1 || turnsWithoutAction >= gameState.getTeamCount() * 10) {

            if (remainingTeams == 1) {
                //Reward score to surviving winner
                for (int i = 0; i < teamCount; i++) {
                    if (remainingCharacters[i] > 0) {
                        gameState.addScore(i, SCORE_WIN[0]);
                        actionLog.getRootAction().addChild(new GameOverAction(i));
                        break;
                    }
                }
            }else{
                actionLog.getRootAction().addChild(new GameOverAction(-1));
            }
            //End game
            gameState.deactivate();
            return this.actionLog;
        }

        IntVector2 turnChar = gameState.getTurn().peek();
        ActionLog lastTurn = this.actionLog;
        assert turnChar != null;
        gameState.getCharacterFromTeams(turnChar.x, turnChar.y).resetAlreadyShot();
        this.actionLog = new ActionLog(new TurnStartAction(0, turnChar.x, turnChar.y));
        return lastTurn;

    }

    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }

    public int getActiveTeam() {
        return Objects.requireNonNull(gameState.getTurn().peek()).x;
    }

    public boolean isActingCharacterAlive() {
        IntVector2 character = gameState.getTurn().peek();
        if (character == null) return false;
        return gameState.getCharacterFromTeams(character.x, character.y).isAlive();
    }
}

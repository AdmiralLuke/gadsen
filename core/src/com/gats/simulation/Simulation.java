package com.gats.simulation;

import com.gats.manager.Timer;
import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.GameOverAction;
import com.gats.simulation.action.InitAction;
import com.gats.simulation.action.TurnStartAction;
import com.gats.simulation.GameState.GameMode;

import java.util.Objects;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {
    private final GameState gameState;
    private ActionLog actionLog;
    private Wrapper wrapper;

    /**
     * erstellt eine neue Simulation
     * @param gameMode Modus in dem gespielt wird
     * @param mapName Map auf der gespielt wird
     * @param teamAm Anzahl Teams
     * @param teamSize Anzahl Charaktere pro Team
     */
    public Simulation(GameMode gameMode, String mapName, int teamAm, int teamSize){
        gameState = new GameState(gameMode,mapName, teamAm, teamSize, this);
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        actionLog = new ActionLog(new TurnStartAction(0, turnChar.x, turnChar.y));
        if (gameMode == GameMode.Christmas) {
            gameState.getTeams()[1][0].setHealth(1, actionLog.getRootAction());
            gameState.getTeams()[2][0].setHealth(1, actionLog.getRootAction());
            gameState.getTeams()[3][0].setHealth(1, actionLog.getRootAction());
        }
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
     * @return aktueller GameState
     */
    public GameState getState() {
        return gameState;
    }
    ActionLog getActionLog(){
        return actionLog;
    }

    public
    void setTurnTimer(Timer timer){
        gameState.setTurnTimer(timer);
    }

    public GameCharacterController getController() {
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        return new GameCharacterController(gameState.getCharacterFromTeams(turnChar.x,turnChar.y), gameState);
    }


    public ActionLog endTurn() {
        if (this.gameState.getGameMode() == GameMode.Christmas && this.gameState.getTeams()[0][0].getHealth() <= 0) {
            this.actionLog.getRootAction().addChild(new GameOverAction(1));
            gameState.deactivate();
            return this.actionLog;
        }

        if (gameState.getTurn().size() <= 1) {
            //ToDo: Fix edge cases:
            //If no players are alive the game crashes
            //If a team survives with multiple characters, the game doesnt end
            this.actionLog.getRootAction().addChild(new GameOverAction(Objects.requireNonNull(this.gameState.getTurn().peek()).y));
            gameState.deactivate();
            return this.actionLog;
        }

        IntVector2 lastChar = gameState.getTurn().pop();
        if (gameState.getCharacterFromTeams(lastChar.x, lastChar.y).getHealth() > 0) {
            gameState.getTurn().add(lastChar);
        } else {
            gameState.getTeams()[lastChar.x][lastChar.y] = null;
        }
        IntVector2 nextChar = gameState.getTurn().peek();

        while (nextChar != null && gameState.getCharacterFromTeams(nextChar.x, nextChar.y).getHealth() <= 0) {
            gameState.getTurn().pop();

            gameState.getTeams()[ nextChar.x][ nextChar.y] = null;

            nextChar = gameState.getTurn().peek();
        }
        if (nextChar == null) throw new NullPointerException("Turn dequeue returned null");

        gameState.getCharacterFromTeams(nextChar.x, nextChar.y).resetStamina();
        gameState.getCharacterFromTeams(nextChar.x, nextChar.y).resetAlreadyShot();

        IntVector2 turnChar = gameState.getTurn().peek();
        ActionLog lastTurn = this.actionLog;
        assert turnChar != null;
        this.actionLog = new ActionLog(new TurnStartAction(0, turnChar.x, turnChar.y));
        return lastTurn;
    }

    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }

}

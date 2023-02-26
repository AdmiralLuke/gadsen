package com.gats.simulation;

import com.gats.simulation.action.ActionLog;
import com.gats.simulation.action.GameOverAction;
import com.gats.simulation.action.InitAction;
import com.gats.simulation.action.TurnStartAction;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {
    private GameState gameState;
    private ActionLog actionLog;

    /**
     * erstellt eine neue Simulation
     * @param gameMode Modus in dem gespielt wird
     * @param mapName Map auf der gespielt wird
     * @param teamAm Anzahl Teams
     * @param teamSize Anzahl Charaktere pro Team
     */
    public Simulation(int gameMode,String mapName, int teamAm, int teamSize){
        gameState = new GameState(gameMode,mapName, teamAm, teamSize, this);
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        actionLog = new ActionLog(new TurnStartAction(turnChar.x, turnChar.y,0));
        if (gameMode == GameState.GAME_MODE_CHRISTMAS) {
            gameState.getTeams()[1][0].setHealth(1, actionLog.getRootAction());
            gameState.getTeams()[2][0].setHealth(1, actionLog.getRootAction());
            gameState.getTeams()[3][0].setHealth(1, actionLog.getRootAction());
        }
    }

    public static IntVector2 convertToTileCoords(IntVector2 worldCoords) {
        return new IntVector2(convertToTileCoordsX(worldCoords.x), convertToTileCoordsY(worldCoords.y));
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


    public GameCharacterController getController() {
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        return new GameCharacterController(gameState.getCharacterFromTeams(turnChar.x,turnChar.y), gameState);
    }


    public ActionLog endTurn() {
        if (this.gameState.getGameMode() == GameState.GAME_MODE_CHRISTMAS && this.gameState.getTeams()[0][0].getHealth() <= 0) {
            this.actionLog.getRootAction().addChild(new GameOverAction(1));
            gameState.setActive(false);
            return this.actionLog;
        }

        if (gameState.getTurn().size() <= 1) {
            this.actionLog.getRootAction().addChild(new GameOverAction(this.gameState.getTurn().peek().y));
            gameState.setActive(false);
            return this.actionLog;
        }

        IntVector2 lastChar = gameState.getTurn().pop();
        if (gameState.getCharacterFromTeams(lastChar.x, lastChar.y).getHealth() > 0) {
            gameState.getTurn().add(lastChar);
        } else {
            gameState.getTeams()[lastChar.x][lastChar.y] = null;
        }
        IntVector2 nextChar = gameState.getTurn().peek();

        while (gameState.getCharacterFromTeams(nextChar.x, nextChar.y).getHealth() <= 0) {
            gameState.getTurn().pop();

            gameState.getTeams()[ nextChar.x][ nextChar.y] = null;

            nextChar = gameState.getTurn().peek();
        }
        gameState.getCharacterFromTeams(nextChar.x, nextChar.y).resetStamina();
        gameState.getCharacterFromTeams(nextChar.x, nextChar.y).setAlreadyShot(false);

        IntVector2 turnChar = gameState.getTurn().peek();
        ActionLog lastTurn = this.actionLog;
        assert turnChar != null;
        this.actionLog = new ActionLog(new TurnStartAction(turnChar.x, turnChar.y, 0));
        return lastTurn;
    }

    public ActionLog clearAndReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }

}

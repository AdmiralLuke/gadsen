package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {
    private GameState gameState;
    private ActionLog actionLog;

    //hud stage übergeben für die inputs?
    public Simulation(int gameMode,String mapName, int teamAm, int teamSize){
        gameState = new GameState(gameMode,mapName, teamAm, teamSize, this);
        Vector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        actionLog = new ActionLog(new TurnStartAction((int)turnChar.x,(int)turnChar.y,0));
    }
    public GameState getState() {
        return gameState;
    }
    public ActionLog getActionLog(){
        return actionLog;
    }


    public GameCharacterController getController() {
        Vector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        return new GameCharacterController(gameState.getCharacterFromTeams((int)turnChar.x,(int)turnChar.y), gameState);
    }


    public ActionLog endTurn() {
        Vector2 lastChar = gameState.getTurn().pop();
        gameState.getTurn().add(lastChar);
        // ToDo: check for dead character
        return clearAndReturnActionLog();
    }

    public ActionLog clearAndReturnActionLog() {
        Vector2 turnChar = gameState.getTurn().peek();
        ActionLog tmp = this.actionLog;
        assert turnChar != null;
        this.actionLog = new ActionLog(new TurnStartAction((int)turnChar.x, (int)turnChar.y, 0));
        return tmp;
    }
}

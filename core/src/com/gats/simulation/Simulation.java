package com.gats.simulation;

/**
 * Enthält die Logik, welche die Spielmechaniken bestimmt.
 * Während die Simulation läuft werden alle Ereignisse in ActionLogs festgehalten, die anschließend durch das animation package dargestellt werden können.
 */
public class Simulation {
    private GameState gameState;
    private ActionLog actionLog;

    //hud stage übergeben für die inputs?
    public Simulation(int gameMode,String mapName){
        actionLog = new ActionLog();
        gameState = new GameState(gameMode,mapName);
    }
    public GameState getState() {
        return gameState;
    }
    public ActionLog getActionLog(){
        return actionLog;
    }
}

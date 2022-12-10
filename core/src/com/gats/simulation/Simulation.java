package com.gats.simulation;

import com.badlogic.gdx.math.Vector2;

import com.gats.ui.GADS;
import com.gats.ui.GameSettings;
import com.gats.ui.MenuScreen;

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
        gameState.initTeam();
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
        if (gameState.getTurn().size() <= 1) {
            gameState.setActive(false);
            return this.actionLog;
        }
        Vector2 lastChar = gameState.getTurn().pop();
        if (gameState.getCharacterFromTeams((int)lastChar.x, (int)lastChar.y).getHealth() > 0) {
            gameState.getTurn().push(lastChar);
        } else {
            gameState.getTeams()[(int)lastChar.x][(int)lastChar.y] = null;
        }
        Vector2 nextChar = gameState.getTurn().peek();

        while (gameState.getCharacterFromTeams((int)nextChar.x, (int)nextChar.y).getHealth() <= 0) {
            gameState.getTurn().pop();

            gameState.getTeams()[(int) nextChar.x][(int) nextChar.y] = null;

            nextChar = gameState.getTurn().peek();
        }
        gameState.getCharacterFromTeams((int)nextChar.x, (int)nextChar.y).resetStamina();
        return clearAndReturnActionLog();
    }

    public ActionLog clearAndReturnActionLog() {
        Vector2 turnChar = gameState.getTurn().peek();
        ActionLog tmp = this.actionLog;
        assert turnChar != null;
        this.actionLog = new ActionLog(new TurnStartAction((int)turnChar.x, (int)turnChar.y, 0));
        return tmp;
    }

    public ActionLog clearReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }
}

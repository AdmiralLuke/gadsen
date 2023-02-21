package com.gats.simulation;

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
        if (gameMode == GameState.GAME_MODE_CHRISTMAS) {
            gameState.getTeams()[1][0].setHealth(1);
            gameState.getTeams()[2][0].setHealth(1);
            gameState.getTeams()[3][0].setHealth(1);
        }
        IntVector2 turnChar = gameState.getTurn().peek();
        assert turnChar != null;
        actionLog = new ActionLog(new TurnStartAction(turnChar.x, turnChar.y,0));
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
            this.actionLog.addAction(new GameOverAction(1));
            gameState.setActive(false);
            return this.actionLog;
        }

        if (gameState.getTurn().size() <= 1) {
            this.actionLog.addAction(new GameOverAction(this.gameState.getTurn().peek().y));
            gameState.setActive(false);
            return this.actionLog;
        }

        for (GameCharacter[] characters : this.gameState.getTeams()) {
            for (GameCharacter character : characters) {
                if (character != null) {
                    character.fall();
                }
            }
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
        gameState.getCharacterFromTeams(nextChar.x, nextChar.y).setAlreadyShooted(false);
        return clearAndReturnActionLog();
    }

    public ActionLog clearAndReturnActionLog() {
        IntVector2 turnChar = gameState.getTurn().peek();
        ActionLog tmp = this.actionLog;
        assert turnChar != null;
        this.actionLog = new ActionLog(new TurnStartAction(turnChar.x, turnChar.y, 0));
        return tmp;
    }

    public ActionLog clearReturnActionLog() {
        ActionLog tmp = this.actionLog;
        this.actionLog = new ActionLog(new InitAction());
        return tmp;
    }
}

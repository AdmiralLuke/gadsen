package com.gats.ui;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gats.manager.RunConfiguration;

import java.util.LinkedList;

public class GameSettings {

    private int amountTeams = 2;
    private int teamSize = 2;
    private String mapName = "default";
    private int gameMode = 0;
    private String[] availableMaps = {"no Maps loaded"};
    private String[] availableGameMode = {"no Modes loaded"};
    //Todo bots von der einbindung entnehmen
    private String[] availableBots = {"Human", "TestBot", "Christmas Bot"};
    //Todo change map name for christmasTask
    private String weihnachtsmap = "map1";

    public GameSettings() {
    }

    public GameSettings(int gameMode, String mapName, int amountTeams, int teamSize) {
        this.gameMode = gameMode;
        this.mapName = mapName;
        this.amountTeams = amountTeams;
        this.teamSize = teamSize;
        this.availableMaps = new MapRetriever().listMaps();

        this.availableGameMode = new String[]{"Weihnachtsaufgabe", "Normal"};
        //ToDo: Implement Bots
    }

    void evaluateButtonSettings(SelectBox<String> modeButton, SelectBox<String> mapButton, Slider teamButton, Slider playerButton, LinkedList<MenuScreen.BotSelectionBox> botSelection) {
        //Todo make mode selection work without getSelectedIndex (use Map)
        setGameMode(modeButton.getSelected());
        //spaghetti solution

        //gameMode 1 == Weihnachtsaufgabe
        if (gameMode == 1) {
            setMapName(weihnachtsmap);
            setTeamSize(4);
            setAmountTeams(1);
            //ToDo handle Bots
        } else {
            setMapName(mapButton.getSelected());
            //might need to be adjusted,could be that the buttons are switched
            setTeamSize((int) playerButton.getValue());
            setAmountTeams((int) teamButton.getValue());
        }
    }

    private void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    private void setGameMode(String gameModeName) {
        if (gameModeName.equals("Weihnachtsaufgabe")) {
            setGameMode(1);
        } else {
            setGameMode(0);
        }
    }

    private void setMapName(String mapName) {
        this.mapName = mapName;
    }

    private void setAmountTeams(int amountTeams) {
        this.amountTeams = amountTeams;
    }

    private void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    private String[] getAvailableMaps() {

        return this.availableMaps;
    }

    public int getGameMode() {
        return gameMode;
    }

    public String getMapName() {
        return mapName;
    }

    public int getAmountTeams() {
        return amountTeams;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Table getChristmasMenuButtons(Skin skin) {
        //Todo implement to return a table with the necessary buttons for the christmas task
        Table christmasMenu = new Table(skin);

        // MapSelection not really needed
        //GameModeselection should be in menutable
        SelectBox<String> botSelectionBox = new SelectBox<String>(skin);
        botSelectionBox.setItems(availableBots);
        christmasMenu.add(botSelectionBox);

        //Todo BotSelection


        return new Table();
    }

    public Table getNormalMenuButtons(Skin skin) {
        //Todo implement
        return new Table();
    }

    public RunConfiguration toRunConfiguration() {
        RunConfiguration config = new RunConfiguration();
        config.gameMode = getGameMode();
        config.teamCount = getAmountTeams();
        config.teamSize = getTeamSize();
        config.teamSize = getTeamSize();
        return config;
    }
}
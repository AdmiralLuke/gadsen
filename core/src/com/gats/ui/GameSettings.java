package com.gats.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Klasse, welche das Speichern der Einstellung für das Starten
 * des Spiels übernimmmt und Menüschalter erstellt.
 */
public class GameSettings {

    Actor[] buttons;
    ImageButton startGameButton;
    SelectBox<String> gameModeButton;
    SelectBox<String> mapSelectionBox;
    MenuScreen.SliderLabel teamAmountSliderLabel;
    Slider teamAmountSlider;
    MenuScreen.SliderLabel teamSizeSliderLabel;
    Slider teamSizeSlider;

    LinkedList<SelectBox<String>> botSelectors = new LinkedList<>();
    Table botTable;

    TextButton exitButton;
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

    private GADS gameInstance;
    public GameSettings() {
    }

    public GameSettings(int gameMode, String mapName, int amountTeams, int teamSize, GADS gameInstance) {

        this.gameInstance = gameInstance;

        this.gameMode = gameMode;
        this.mapName = mapName;
        this.amountTeams = amountTeams;
        this.teamSize = teamSize;
        this.availableMaps = new MapRetriever().listMaps();

        this.availableGameMode = new String[]{"Weihnachtsaufgabe", "Normal"};
        //ToDo: Implement Bots
    }

    /**
     * Wertet die vorgenommenen Einstellungen an den übergebenen Knöpfen aus und speichert diese.
     *
     * @param modeButton            Auswahl des Spielmodi
     * @param mapButton             Auswahl der Karte
     * @param teamButton            Anzahl der Teams
     * @param characterAmountButton Anzahl der Charaktere pro Team
     * @param botSelection          Ausgewählte Bots
     */
    void evaluateButtonSettings(SelectBox<String> modeButton, SelectBox<String> mapButton, Slider teamButton, Slider characterAmountButton, LinkedList<MenuScreen.BotSelectionBox> botSelection) {
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
            setTeamSize((int) characterAmountButton.getValue());
            setAmountTeams((int) teamButton.getValue());
        }
    }

    void evaluateSettings() {
        String gameModeName = ((SelectBox<String>) buttons[1]).getSelected();
        if (gameModeName.equals("Weihnachtsaufgabe")) {
            this.gameMode = 1;
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

    /**
     * Erstellt {@link Table} für den Weihnachtsaufgabe modi.
     *
     * @param skin Skin für den Table.
     * @return Table welcher alle wichtigen Buttons für den Christmas Spielmodi enthält.
     */
    public Table getChristmasMenuButtons(Skin skin) {
        //Todo implement to return a table with the necessary buttons for the christmas task
        Table christmasMenu = new Table(skin);

        // MapSelection not really needed
        //GameModeselection should be in menutable
        SelectBox<String> botSelectionBox = createBotButton(skin, availableBots);
        //	also work with bottable
        rebuildBotTable(1,botTable,botSelectors);
        botSelectionBox.setItems(availableBots);
        christmasMenu.add(botSelectionBox);

        //Todo BotSelection


        return new Table();
    }

    public Table getNormalMenuButtons(Skin skin) {
        //Todo implement
        return new Table();
    }

    public void createAllButtons(Skin skin, SpriteDrawable startButtonImage) {
        //slider value for min and max
        // used in the team slider
        // might be moved somewhere else
        int minValue = 1;
        int maxValue = 8;
        startGameButton = createStartButton(startButtonImage);
        gameModeButton = createGameModeButton(skin, availableGameMode);
        mapSelectionBox = createMapButton(skin, availableMaps);
        teamAmountSlider = createTeamAmountSlider(skin, minValue, maxValue);
        teamSizeSlider = createTeamSizeSlider(skin, minValue, maxValue);
        botSelectors.add(createBotButton(skin, availableBots));
        exitButton = createExitButton(skin);

    }

    public ImageButton createStartButton(SpriteDrawable image) {

        ImageButton startGameButton = new ImageButton(image);
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInstance.startGame();
            }
        });
        return startGameButton;
    }


    public SelectBox<String> createGameModeButton(Skin skin, String[] availableGameModes) {

        SelectBox<String> gameModeButton = new SelectBox<>(skin);
        gameModeButton.setItems(availableGameModes);
        return gameModeButton;
    }

    public SelectBox<String> createMapButton(Skin skin, String[] availableMaps) {

        SelectBox<String> mapButton = new SelectBox<>(skin);
        mapButton.setItems(availableMaps);
        return mapButton;
    }

    public Slider createTeamAmountSlider(Skin skin, int min, int max) {
        Slider teamAmountSlider = new Slider(min, max, 1, false, skin);
        teamAmountSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rebuildBotTable(teamAmountSlider.getValue(), botTable, botSelectors);
            }
        });
        return teamAmountSlider;

    }

    public Slider createTeamSizeSlider(Skin skin, int min, int max) {

        return new Slider(min, max, 1, false, skin);
    }

    public SelectBox<String> createBotButton(Skin skin, String[] availableBots) {

        SelectBox<String> botButton = new SelectBox<String>(skin);
        botButton.setItems(availableBots);
        return botButton;
    }

    public TextButton createExitButton(Skin skin) {
        TextButton exitButton = new TextButton("Beenden", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return exitButton;
    }

    void rebuildBotTable(float botAmount, Table botTable, LinkedList<SelectBox<String>> currentBots) {
        int columns = 3;
        LinkedList<SelectBox<String>> newBotSelectors = new LinkedList<>();
        botTable.clear();
        ListIterator<SelectBox<String>> iterator = currentBots.listIterator();
        for (int i = 0; i < botAmount; i++) {
            //add a new row to the bottable to create another column
            if (i % columns == 0 && i != 0) {
                botTable.row();
            }
            if (iterator.hasNext()) {
                SelectBox<String> current = iterator.next();
                botTable.add(current).pad(5);
                newBotSelectors.add(current);
            } else {
              SelectBox<String> newBotButton = createBotButton(botTable.getSkin(),availableBots);
                botTable.add(newBotButton).pad(5);
                newBotSelectors.add(newBotButton);
            }

        }
        botSelectors = newBotSelectors;


    }

    ;
}
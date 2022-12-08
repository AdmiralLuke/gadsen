package com.gats.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gats.manager.RunConfiguration;

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
    Slider teamAmountSlider;
    Slider teamSizeSlider;

    LinkedList<SelectBox<String>> botSelectors = new LinkedList<>();
    Table botTable;

    TextButton exitButton;
    private int teamAmount = 2;
    private int teamSize = 2;
    private String mapName = "default";
    private int gameMode = 0;

    private String[] selectedBotNames;
    private String[] availableMaps = {"no Maps loaded"};
    private String[] availableGameMode = {"no Modes loaded"};
    private final String christmasBotName = "Christmas Bot";    //Todo bots aus der einbindung entnehmen, wenn sie erfolgt ist
    private final String christmasGameModeName = "Weihnachtsaufgabe";

    //cheatyspaghetti cuz stuff is not working as intended :(
    //for some reason an actor can only be inside one table at a time
    private SelectBox<String> christmasBotSelectorBox;
    private String[] availableBots = {"Human", "TestBot", christmasBotName};
    //Todo change map name for christmasTask
    private String weihnachtsmap = "map1";

    private GADS game;
    private Table gameModeSpecificTable;
    Table normalMenuTable;
    Table christmasTable;

    public GameSettings(GADS gameInstance) {
        this.game = gameInstance;
        this.availableMaps = new MapRetriever().listMaps();
        this.availableGameMode = new String[]{"Normal", "Weihnachtsaufgabe"};
    }

    public GameSettings(int gameMode, String mapName, int teamAmount, int teamSize, GADS gameInstance) {

        this.game = gameInstance;

        this.gameMode = gameMode;
        this.mapName = mapName;
        this.teamAmount = teamAmount;
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
    void evaluateButtonSettings(SelectBox<String> modeButton, SelectBox<String> mapButton, Slider teamButton, Slider characterAmountButton, LinkedList<SelectBox<String>> botSelection) {
        //Todo make mode selection work without getSelectedIndex (use Map)
        setGameMode(modeButton.getSelected());
        //spaghetti solution

        //gameMode 1 == Weihnachtsaufgabe
        if (gameMode == 1) {
            setMapName(weihnachtsmap);
            setTeamSize(4);
            setTeamAmount(1);
            //ToDo handle Bots
        } else {
            setMapName(mapButton.getSelected());
            //might need to be adjusted,could be that the buttons are switched
            setTeamSize((int) characterAmountButton.getValue());
            setTeamAmount((int) teamButton.getValue());
        }
    }

    void evaluateSettings() {
        String gameModeName = gameModeButton.getSelected();
        if (gameModeName.equals(christmasGameModeName)) {
            this.gameMode = 1;
            setChristmasSpecialSettings(christmasTable.getSkin());
        }
        setMapName(mapSelectionBox.getSelected());
        setTeamAmount((int) teamAmountSlider.getValue());
        setTeamSize((int) teamSizeSlider.getValue());
        setSelectedBots(botSelectors);


    }

    private void setSelectedBots(LinkedList<SelectBox<String>> selectedBots) {
        int amountOfBots = selectedBots.size();
        this.selectedBotNames = new String[amountOfBots];

        int i = 0;
        for (SelectBox<String> selectedBot : selectedBots
        ) {
            selectedBotNames[i] = selectedBot.getSelected();
            i++;
        }
    }

    private void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    private void setGameMode(String gameModeName) {
        if (gameModeName.equals(christmasGameModeName)) {
            setGameMode(1);
        } else {
            setGameMode(0);
        }
    }

    private void setMapName(String mapName) {
        this.mapName = mapName;
    }

    private void setTeamAmount(int teamAmount) {
        this.teamAmount = teamAmount;
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

    public int getTeamAmount() {
        return teamAmount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    private Class[] getPlayers() {
        return null; //ToDo: return array of players
    }

    /**
     * Erstellt {@link Table} für den Weihnachtsaufgabe modi.
     *
     * @param skin Skin für den Table.
     * @return Table welcher alle wichtigen Buttons für den Christmas Spielmodi enthält.
     */
    private void makeChristmasMenuButtons(Skin skin) {
        //Todo implement to return a table with the necessary buttons for the christmas task
        Table christmasMenu = new Table(skin);
        christmasBotSelectorBox = createBotButton(skin, availableBots);

//      christmasMenu.add(createBotButton(skin,availableBots));
        christmasMenu.add(christmasBotSelectorBox);
        this.christmasTable = christmasMenu;
        //Todo BotSelection


    }

    void setChristmasSpecialSettings(Skin skin) {

        int christmasBotAmount = 4;
        int christmasTeamSize = 1;

        // MapSelection not really needed
        //GameModeselection should be in menutable
        //set value to that required by the game mode, buttons wont be visible
        mapSelectionBox.setSelected("Weihnachtsmap");
        rebuildBotTable(1, botTable, botSelectors);

        teamAmountSlider.setValue(christmasBotAmount);
        teamSizeSlider.setValue(christmasTeamSize);
        //erstellt BotAuswahlButtons mit dem Weihnachtsbot als auswahl
        botSelectors.clear();
        botSelectors.add(christmasBotSelectorBox);
        for (int i = 0; i < christmasBotAmount; i++) {
            botSelectors.add(createBotButton(skin, new String[]{christmasBotName}));
        }
        //add buttons to the table that are meant to be adjusted

    }

    private void makeNormalMenuButtons(Skin skin) {

        Table normalModeTable = new Table(skin);
//        normalModeTable.debug();
        normalModeTable.add(mapSelectionBox).pad(10).colspan(4);
        normalModeTable.row();
        normalModeTable.add(new SliderLabel("Anzahl der Teams: ", skin, teamAmountSlider));
        normalModeTable.add(teamAmountSlider).pad(10);

        teamAmountSlider.setValue(1);

        normalModeTable.row();
        normalModeTable.add(new SliderLabel("Gadsen pro Team: ", skin, teamSizeSlider));
        normalModeTable.add(teamSizeSlider).pad(10);

        normalModeTable.row();
        normalModeTable.add(botTable).size(100).colspan(4);
        rebuildBotTable(teamAmountSlider.getValue(), botTable, botSelectors);
        normalModeTable.row();

        this.normalMenuTable = normalModeTable;
    }

    void setGameModeSpecificTable(Table table) {

        this.gameModeSpecificTable.clear();
        this.gameModeSpecificTable.add(table).colspan(4);

    }

    private void makeButtons(Skin skin) {
        //slider value for min and max
        // used in the team slider
        // might be moved somewhere else
        int minValue = 1;
        int maxValue = 9;
        mapSelectionBox = createMapButton(skin, availableMaps);
        teamAmountSlider = createTeamAmountSlider(skin, minValue, maxValue);
        teamSizeSlider = createTeamSizeSlider(skin, minValue, maxValue);
        if (botTable == null) {
            botTable = new Table(skin);
        }
        exitButton = createExitButton(skin);

    }

    private ImageButton createStartButton(TextureRegion startButtonImage) {

        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(startButtonImage));
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                evaluateSettings();
                game.setScreenIngame();
            }
        });
        this.startGameButton = startGameButton;
        return startGameButton;
    }


    private SelectBox<String> createGameModeButton(Skin skin, String[] availableGameModes) {

        SelectBox<String> gameModeButton = new SelectBox<>(skin);
        gameModeButton.setItems(availableGameModes);
        gameModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameModeButton.getSelected().equals(christmasGameModeName)) {
                    setGameModeSpecificTable(christmasTable);
                } else {
                    setGameModeSpecificTable(normalMenuTable);
                }

            }
        });
        return gameModeButton;
    }

    private SelectBox<String> createMapButton(Skin skin, String[] availableMaps) {

        SelectBox<String> mapButton = new SelectBox<>(skin);
        mapButton.setItems(availableMaps);
        return mapButton;
    }

    private Slider createTeamAmountSlider(Skin skin, int min, int max) {
        Slider teamAmountSlider = new Slider(min, max, 1, false, skin);
        teamAmountSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rebuildBotTable(teamAmountSlider.getValue(), botTable, botSelectors);
            }
        });
        return teamAmountSlider;

    }

    private Slider createTeamSizeSlider(Skin skin, int min, int max) {

        return new Slider(min, max, 1, false, skin);
    }

    private SelectBox<String> createBotButton(Skin skin, String[] availableBots) {

        SelectBox<String> botButton = new SelectBox<String>(skin);
        botButton.setItems(availableBots);
        return botButton;
    }

    private TextButton createExitButton(Skin skin) {
        TextButton exitButton = new TextButton("Beenden", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        return exitButton;
    }
  /**
     * Recreates the botTable. Used for adjusting the amount of Botselections.
     * Not very efficient solution, yey we only have to handle a small number of buttons.
     */
    void rebuildBotTable(float botAmount, Table botTable, LinkedList<SelectBox<String>> currentBots) {
        int columns = 3;
        LinkedList<SelectBox<String>> newBotSelectors = new LinkedList<>();
        ListIterator<SelectBox<String>> iterator = currentBots.listIterator();
        botTable.clear();
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
                SelectBox<String> newBotButton = createBotButton(botTable.getSkin(), availableBots);
                botTable.add(newBotButton).pad(5);
                newBotSelectors.add(newBotButton);
            }

        }
        botSelectors = newBotSelectors;


    }

    Table buildMainLayoutTable(Skin skin, TextureRegion StartButtonImage, TextureRegion titleImage) {

        Table table = new Table(skin);
        table.setFillParent(true);
        //platziert den Table an der oberen Kante des Bildschirms
        table.top();
        //Spieltitel wird in der ersten Zeile hinzugefügt und hat eine breite von 4 Spalten
        table.add(new Image(titleImage)).colspan(4).pad(20);

        //table.row(); erzeugt eine neue Zeile in der Tabelle
        table.row();

        //Startknopf wird platziert
        table.add(createStartButton(StartButtonImage)).colspan(4);
        table.row();

        gameModeButton = createGameModeButton(skin, availableGameMode);
        table.add(gameModeButton).pad(10).width(200).colspan(4);
        table.row();

        //gamemodespecific table enthält während der Laufzeit das menü für den ausgewählten Spielmodus
        this.gameModeSpecificTable = new Table(skin);
        table.add(gameModeSpecificTable).colspan(4);

        makeButtons(skin);
        //Standardmäßig wird das Normale Menü erzeugt
        makeNormalMenuButtons(skin);
        makeChristmasMenuButtons(skin);

        setGameModeSpecificTable(normalMenuTable);
        //ganz unten im Menü ist der Exit button
        table.row();

        table.add(createExitButton(skin)).colspan(4).pad(10);

        return table;
    }

    public RunConfiguration toRunConfiguration() {
        RunConfiguration config = new RunConfiguration();
        config.gameMode = getGameMode();
        config.mapName = getMapName();
        config.teamCount = getTeamAmount();
        config.teamSize = getTeamSize();
        config.teamSize = getTeamSize();
        config.players = getPlayers();
        return config;
    }

    class SliderLabel extends Label {
        Slider sliderInstance;

        String name;

        public SliderLabel(String name, Skin skin, Slider slider) {
            super(name, skin);
            this.sliderInstance = slider;
            this.name = name;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            setText(name + (int) sliderInstance.getValue());

        }
    }


}
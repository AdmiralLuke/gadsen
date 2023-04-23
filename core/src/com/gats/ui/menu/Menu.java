package com.gats.ui.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gats.manager.Game;
import com.gats.manager.IdleBot;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.simulation.GameState;
import com.gats.ui.MenuScreen;
import com.gats.ui.menu.buttons.*;
import com.gats.ui.menu.gamemodeLayouts.CampaignLayout;
import com.gats.ui.menu.gamemodeLayouts.ChristmasLayout;
import com.gats.ui.menu.gamemodeLayouts.NormalLayout;
import com.gats.ui.menu.specificRunConfig.ChristmasModeConfig;
import com.gats.ui.menu.specificRunConfig.NormalModeConfig;

import java.util.Arrays;

public class Menu {

	/* Was ist für das Menu-Refactor wichtig?

     Buttons
     =======

     - "Aktionsknöpfe"
		- Führen etwas aus bei Berührung

	- Selectboxes
		- Maps
		- Bots

	- Slider
		- Teamanzahl und Größe

	- Titel


	- Hintergrund


	Dynamische Buttons
	==================

	- das Anpassen des Tables funktioniert nicht gut, da man von haus aus nichts removen kann
		- möglicherweise lässt sich das ändern durch eine neue Methode oder das verwenden einer anderen Klasse
			- ist das notwendig? bisher habe ich einen Weg gefunden, dass es funktioniert. dies ist jedoch nicht sehr effiizient

	- bisherige lösung reicht vermutlich aus, table anzupassen ist vermutlich zu aufwändig für ui kram


	- BotSelection als Klasse bzw Multi Selection, sollte sich anpassen lassen
		- Maximale länge angeben die der Table haben sollte?
		- Spaltenanzahl/größe

	- dann mithilfe der Slider einfach bestimmen wie viel Boxen gerendert werden
		- horizontal widget group anschauen?
		- bisher muss bei anpassung immer neuer Table gebaut werden
			- hinzufügen oder löschen eines elements wäre praktischer

		- evtl. manuelles positionieren oder eigenes Layout/Table schreiben.
			- bspw. Menü aufteilen in Zeilen, Elemente mit einem Set Speichern? oder als Matrix?

	*/

/*
	Plan
	manche Buttons in extra klassen auslagern
	getter und setter sowie button generation in eindeutigen bereiche verlagern

	Menu evtl noch in Gamemodespezifische "Screens"/Layouts aufgeteilen
	- ist aber an sich doch eher nicht notwendig



Frage: wie genau soll GameMode adjustment funktionieren
an sich ist die Hirarchie der Einstellungen bestimmt durch
- Gamemode -> Rahmenbedingungen, was eingestellt werden kann, bzw, welche buttons gezeigt werden
- Map -> bestimmt anzahl der Teams/Characters
- Teamamount bestimmt Botanzahl
- Bots geben botinfo weiter
- später evtl. noch skinauswahl?
	- oder skinauswahl soll durch die Bots bestimmt werden
		- name des skins im Bot vermerken falls dieser nicht erkannt wird, einfach standard skin auswahl nehmen

		klasse für bestimmte table erstellen, bei wechsel des tables alle actor clearen, und neuen table erzeugen
		Abstrakt class GameModeMenu


	----
	Verfügbare Buttons:
		Start
		GameMode
		Exit
		^^^ sollten in allen enthalten sein

		- Map
		- Teams + Bots
			- evtl andere Logik in verschiedenen Menüs!!
		- Teamsize





	-----
		NormalMenu
			- "alles"
		ChristmasMenu
		 	- 1 Bot auswahl

		CompetitionMenu

			- 1 Bot auswahl + Skin?

		evtl. noch Zulassung seperat vom Wettbewerb

 */


	private SelectBox<GameState.GameMode> gameModeSelector;
	private SelectBox<GameMap> mapSelector;
	private TeamSizeSlider teamSizeSlider;
	private TeamAmountSlider teamAmountSlider;

	private BotSelectorTable botSelector;


	private Image title;



//--------;


	Table menuTable;
	MenuScreen menuScreen;
	public Menu(Skin skin, TextureRegion titleSprite, Manager.NamedPlayerClass[] availableBots, RunConfiguration runConfig, MenuScreen menuScreen) {
		menuTable = new Table(skin);
		if(gameModeSelector==null) {
			createButtons(skin, availableBots, runConfig,titleSprite);
		}

		this.menuScreen=menuScreen;
	}

	public Table buildMenuLayout(Skin skin) {

		StartButton startButton = new StartButton("Spiel starten", skin, this);
		ExitButton exitButton = new ExitButton("Beenden", skin);
		//------------------------

		menuTable.clear();
		menuTable.setFillParent(true);


		//platziert den Table an der oberen Kante des Bildschirms
		menuTable.top();

		//titelbild/überschrift

		//Todo get title image sprite

		//Spieltitel wird in der ersten Zeile hinzugefügt und hat eine breite von 4 Spalten
		menuTable.add(title).colspan(4).pad(15).height(80).minWidth(title.getImageWidth());

		//menuTable.row(); erzeugt eine neue Zeile in der Tabelle
		menuTable.row();

		menuTable.add(startButton).colspan(4);
		menuTable.row();

		menuTable.add(gameModeSelector).pad(10).colspan(4);
		menuTable.row();

		menuTable.add(getGameModeLayout(gameModeSelector.getSelected())).colspan(4);
		menuTable.row();

		//ganz unten im  ist der Exit button
		menuTable.add(exitButton).colspan(4).pad(10);
		return menuTable;
	}

	/**
	 * Gets called when start button is pressed.
	 *
	 * Will pass button settings to the RunConfiguration
	 */
	public void startGame() {


		RunConfiguration config = this.toRunConfig();
		config = applyGamemodeSettings(config);
		System.out.println(config.toString());

		if(config.isValid()) {

		menuScreen.startGame(config);

		}
	}

	public RunConfiguration toRunConfig(){

		RunConfiguration configuration = new RunConfiguration();


		configuration.gui = true;

		//Todo make sure the gamemodes are in correct order
		configuration.gameMode = GameState.GameMode.values()[gameModeSelector.getSelectedIndex()];
		configuration.mapName = this.mapSelector.getSelected().getName();
		configuration.teamCount = (int)this.teamAmountSlider.getValue();
		configuration.teamSize = (int)this.teamSizeSlider.getValue();
		configuration.players = this.botSelector.evaluateSelected();

		return configuration;
	}

	public RunConfiguration applyGamemodeSettings(RunConfiguration configuration){

		RunConfiguration modeSettings;

		switch (configuration.gameMode){
			case Normal:
				modeSettings = new NormalModeConfig(configuration);
				break;
			case Christmas:
				//Todo deal with hardcoded values, might be neede for later gameModes
				modeSettings = new ChristmasModeConfig(configuration,"christmasMap",new Manager.NamedPlayerClass(IdleBot.class, "HumanPlayer").getClassRef());
				break;
			default:
				modeSettings = new NormalModeConfig(configuration);

		}


		return modeSettings;
	}
	public Table getGameModeLayout(GameState.GameMode gameMode) {
//Todo  fix hardcoded gamemode evaluation but passing String[] gameModes to buildTable
		Skin skin = menuTable.getSkin();

		if(gameMode == GameState.GameMode.Normal) {
			return new NormalLayout(skin, this);
		}
		if(gameMode == GameState.GameMode.Campaign) {
			return new CampaignLayout(skin, this);
		}

		//default case
		return new NormalLayout(skin, this);
	}

	public void rebuildTable(){
		this.menuTable=buildMenuLayout(this.menuTable.getSkin());
	}

	/*-----------------------------------------------------
	* Button creation Zone
	/*
	 */

	public void createButtons(Skin skin, Manager.NamedPlayerClass[] availableBots, RunConfiguration runConfig, TextureRegion titleImage){
		//Todo load settings from runConfig
		if(titleImage!=null){
		this.title = new Image(titleImage);
		}
		else{
			System.err.println("TitleSprite Texture Region was is null! ");
		}
		this.gameModeSelector = createGameModeSelector(skin, runConfig.gameMode);
		this.mapSelector = createMapSelector(skin,runConfig.mapName,runConfig.gameMode);
		this.botSelector = createBotSelector(skin,availableBots);
		this.teamAmountSlider = createTeamAmountSlider(skin);
		this.teamSizeSlider = createTeamSizeSlider(skin);

		//needs to be called to set teamSizeSlider to the correct starting range
		teamAmountSlider.addRelatedSlider(teamSizeSlider);
		teamAmountSlider.addBotSelector(botSelector);
		teamSizeSlider.addRelatedSlider(teamAmountSlider);

		teamAmountSlider.adjustTeamSizeToSpawnpoints(mapSelector.getSelected().getNumberOfSpawnpoints());


		GameState.GameMode[] gameModes = runConfig.getGameModes();
		//translate modes
		String[] modeNames = new String[gameModes.length];
		int i=0;

		gameModeSelector.setItems(runConfig.getGameModes());

	}



	private <T> SelectBox<T> createGameModeSelector(Skin skin,T selected){
		SelectBox<T> gameModeSelect = new SelectBox<T>(skin);
		gameModeSelect.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				rebuildTable();
			}
		});
		gameModeSelect.setSelected(selected);
		return gameModeSelect;
	}

	/**
	 * Initializes the map selecter with the normal maps
	 * @param skin
	 * @return
	 */
	private SelectBox<GameMap> createMapSelector(Skin skin, String selected, GameState.GameMode mode) {

		//Todo, adjust maps based on first gamemode, dynamically -> if gamemodeselector is initialized, use selected value
		//-> use function for evaluating the selected mode
		SelectBox<GameMap> mapSelector = new SelectBox<>(skin);


		setMaps(mapSelector,mode,selected);

		return mapSelector;
	}
	private TeamAmountSlider createTeamAmountSlider(Skin skin) {

		return new TeamAmountSlider(1,9,1,false,skin);
	}

	private TeamSizeSlider createTeamSizeSlider(Skin skin){
		return new TeamSizeSlider(1,9,1,false,skin);
	}

	private BotSelectorTable createBotSelector(Skin skin, Manager.NamedPlayerClass[] availableBots){
		BotSelectorTable botSelector = new BotSelectorTable(skin,3);
		botSelector.setAvailableBots(availableBots);
		return botSelector;
	}

	/**
	 * Adds the maps from the passed mode, to the {@link Menu#mapSelector}
	 * @param mapSelector to add maps to.
	 * @param mode gamemode to use for the maps
	 */
	public void setMaps(SelectBox<GameMap> mapSelector, GameState.GameMode mode, String selected){


		if(mode== GameState.GameMode.Campaign){

			mapSelector.setItems(new MapRetriever().getCampaignMaps());

		}


		else{
			//default case

			mapSelector.setItems(new MapRetriever().getMaps());
			mapSelector.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					teamAmountSlider.changeValues(mapSelector.getSelected().getNumberOfSpawnpoints());
				}
			});
		}

		if (mapSelector.getItems().size==0){
			mapSelector.setItems(new GameMap("no Maps found",0));
		}

		//need to change GameMap, for this to work
		//mapSelector.setSelected(selected);
	}

	public SelectBox<GameMap> getMapSelector() {
		return mapSelector;
	}

	public TeamSizeSlider getTeamSizeSlider() {
		return teamSizeSlider;
	}

	public TeamAmountSlider getTeamAmountSlider() {
		return teamAmountSlider;
	}

	public BotSelectorTable getBotSelector() {
		return botSelector;
	}
}
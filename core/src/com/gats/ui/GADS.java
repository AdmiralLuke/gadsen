package com.gats.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.simulation.Simulation;

/**
 * GADS ist die verantwortliche Klasse im LifeCycle der Anwendung.
 * Definiert das Verhalten der Anwendung bei LifeCycle-Events wie
 * {@link com.badlogic.gdx.ApplicationListener#create() Starten},
 * {@link com.badlogic.gdx.ApplicationListener#render() Rendern eines Frames} oder
 * {@link com.badlogic.gdx.ApplicationListener#resize(int,int)} () Änderung der Fenstergröße}.
 */
public class GADS extends Game {
	GADSAssetManager assetManager;

	InGameScreen ingame;
	Stage hudStage;
	MenuScreen menuScreen;

	Simulation simulation;

	//gameMode, can later be selected via Menu?
	//mapName, can later be selected via menu?
	//-> necessary for gameState


	class Settings{

		int amountTeams = 2;
		int amountPlayers = 2;
		String mapName = "default";
		int gameMode = 0;

		public Settings(){}

		public Settings(int gameMode,String mapName,int amountTeams,int amountPlayers){
			this.gameMode = gameMode;
			this.mapName = mapName;
			this.amountTeams = amountTeams;
			this.amountPlayers = amountPlayers;
		}

		void evaluateButtonSettings(SelectBox<String> modeButton, SelectBox<String> mapButton, Slider teamButton, Slider playerButton){
			setGameMode(modeButton.getSelectedIndex());
			setMapName(mapButton.getSelected());
			setAmountPlayers((int) playerButton.getValue());
			setAmountTeams((int) teamButton.getValue());

		}

		public void setGameMode(int gameMode) {
			this.gameMode = gameMode;
		}

		public void setMapName(String mapName) {
			this.mapName = mapName;
		}

		public void setAmountTeams(int amountTeams) {
			this.amountTeams = amountTeams;
		}

		public void setAmountPlayers(int amountPlayers) {
			this.amountPlayers = amountPlayers;
		}
	}
	Settings gameSettings;

	int gameMode = 0;
	@Override
	public void create() {
		gameSettings = new Settings();
		//subject to change
		//size of the viewport is subject to change
		assetManager = new GADSAssetManager();
		assetManager.loadTextures();
		menuScreen = new MenuScreen(this, assetManager);
		setScreen(new MenuScreen(this, assetManager));
	}

	public void render() {
		//	clear the screen
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//call assetmanager
		assetManager.update();
		super.render();
	}

	@Override
	public void dispose() {
		if (screen != null) this.screen.dispose();
	}

	public void setScreenIngame() {
		// ToDo: add Team Information from Sliders
		simulation = new Simulation(gameMode, gameSettings.mapName, 2, 1);
		setScreen(new InGameScreen(this, assetManager));
		menuScreen.dispose();
	}

	public void setScreenMenu() {
		menuScreen = new MenuScreen(this, assetManager);
		setScreen(menuScreen);
	}

	public Array<String> getMaps(){
		String[] maps = {"no Maps Loaded"};
		//ToDo: get list of maps from directory? or another place and return it here
		return new Array<String>(maps);


	}

	public Array<String> getBots(){
		String[] bots = {"no BotsLoaded"};
		//ToDo: implement the selection correctly, when Bots are implemente

		return new Array<String>(bots);
	}
}

package com.gats.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gats.simulation.Simulation;

import java.util.LinkedList;

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



	GameSettings gameSettings;

	int gameMode = 0;
	@Override
	public void create() {
		gameSettings = new GameSettings();
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

	public void startGame() {

		simulation = new Simulation(gameSettings.getGameMode(), gameSettings.getMapName(),gameSettings.getAmountTeams(),gameSettings.getTeamSize());
		setScreen(new InGameScreen(this, assetManager));
		menuScreen.dispose();
	}

	public void setScreenMenu() {
		menuScreen = new MenuScreen(this, assetManager);
		setScreen(menuScreen);
	}

	public String[] getMaps(){
		return new MapRetriever().listMaps();


	}

	public String[] getBots(){
		String[] bots = {"Human","testerino","MIO","IsThisTheCrustyCrab?","NOOOTHISISPATRICK"};
		//ToDo: implement the selection correctly, when Bots are implemente

		return bots;
	}
}

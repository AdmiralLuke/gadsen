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

	Simulation simulation;

	//gameMode, can later be selected via Menu?
	//mapName, can later be selected via menu?
	//-> necessary for gameState



	GameSettings gameSettings;

	@Override
	public void create() {
		gameSettings = new GameSettings(this);
		//subject to change
		//size of the viewport is subject to change
		assetManager = new GADSAssetManager();
		assetManager.loadTextures();
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
		assetManager.unloadAtlas();
	}

	public void setScreenIngame() {
		setScreen(new InGameScreen(this, assetManager, gameSettings));
	}

	public void setScreenMenu() {
		setScreen(new MenuScreen(this, assetManager));
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

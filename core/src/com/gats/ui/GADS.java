package com.gats.ui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gats.manager.RunConfiguration;

/**
 * GADS ist die verantwortliche Klasse im LifeCycle der Anwendung.
 * Definiert das Verhalten der Anwendung bei LifeCycle-Events wie
 * {@link com.badlogic.gdx.ApplicationListener#create() Starten},
 * {@link com.badlogic.gdx.ApplicationListener#render() Rendern eines Frames} oder
 * {@link com.badlogic.gdx.ApplicationListener#resize(int,int)} () Änderung der Fenstergröße}.
 */
public class GADS extends Game {
	GADSAssetManager assetManager;

	public void startGame(RunConfiguration config){
		setScreenIngame(config);
	}
	@Override
	public void create() {
		//size of the viewport is subject to change
		assetManager = new GADSAssetManager();
		assetManager.loadFiles();
		setScreen(new MenuScreen(this, assetManager));
//		setScreen(new TestScreen(new SpriteBatch(),assetManager));
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

	public void setScreenIngame(RunConfiguration runConfig) {
		setScreen(new InGameScreen(this, assetManager, runConfig));
	}

	public void setScreenMenu() {
		setScreen(new MenuScreen(this, assetManager));
	}


	public String[] getGameModes() {
		//Todo, maybe move to simulation?

		return new String[]{"Normal","Weihnachtsaufgabe"};
	}
}

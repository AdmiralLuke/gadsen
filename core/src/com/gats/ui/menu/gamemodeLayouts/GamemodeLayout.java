package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gats.ui.menu.Menu;

public abstract class GamemodeLayout extends Table {

	private int defaultColspan;
	private int defaultPadding;
	public GamemodeLayout(Skin skin, Menu menuInstance){
		super(skin);
		setDefaultColspan(4);
		setDefaultPadding(10);
	}

	protected abstract void positionButtons(Menu menu);


	public void setDefaultColspan(int colspan){

		this.defaultColspan = colspan;
	}
	public void setDefaultPadding(int padding){
		this.defaultPadding = padding;
	}

	public int getDefaultColspan() {
		return defaultColspan;
	}

	public int getDefaultPadding() {
		return defaultPadding;
	}
}

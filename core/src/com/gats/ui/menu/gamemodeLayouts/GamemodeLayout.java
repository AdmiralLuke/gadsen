package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gats.ui.menu.GameMap;
import com.gats.ui.menu.Menu;
import com.gats.ui.menu.buttons.BotSelectorTable;
import com.gats.ui.menu.buttons.TeamAmountSlider;
import com.gats.ui.menu.buttons.TeamSizeSlider;

public abstract class GamemodeLayout extends Table {

	private int defaultColspan;
	private int defaultPadding;
	Menu menu;
	public GamemodeLayout(Skin skin, Menu menuInstance){
		super(skin);
		setDefaultColspan(4);
		setDefaultPadding(10);
		menu = menuInstance;
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

	public BotSelectorTable getBotSelector(){
		return menu.getBotSelector();

	}
	public SelectBox<GameMap> getMapSelector(){
		return menu.getMapSelector();
	}

	public TeamAmountSlider getTeamAmountSlider(){
		return menu.getTeamAmountSlider();
	}

	public TeamSizeSlider getTeamSizeSlider(){
		return menu.getTeamSizeSlider();
	}

}

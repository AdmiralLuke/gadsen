package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.simulation.GameState;
import com.gats.ui.menu.Menu;

public class CampaignLayout extends GamemodeLayout{
	public CampaignLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menu);
	}

	@Override
	protected void positionButtons(Menu menu) {
		this.add(getMapSelector());
		menu.setMaps(getMapSelector(), GameState.GameMode.Campaign,"");
		this.add(getBotSelector());
	}
}

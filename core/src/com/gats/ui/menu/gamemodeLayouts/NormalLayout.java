package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.simulation.GameState;
import com.gats.ui.menu.Menu;
import com.gats.ui.menu.buttons.SliderLabel;

public class NormalLayout extends GamemodeLayout {


	public NormalLayout(Skin skin, Menu menu){
		super(skin,menu);
		positionButtons(menu);

	}
	@Override
	protected void positionButtons(Menu menu) {

		//allow teamslider to change botselector size etc.
		getTeamAmountSlider().freezeSlider(false);

		this.add(menu.getMapSelector()).colspan(getDefaultColspan()).pad(getDefaultPadding());
		menu.setMaps(getMapSelector(), GameState.GameMode.Normal);
		this.row();

		this.add(new SliderLabel("Teamanzahl: ",this.getSkin(),menu.getTeamAmountSlider()));
		this.add(menu.getTeamAmountSlider());
		this.row();

		this.add(new SliderLabel("Teamgröße: ",this.getSkin(),menu.getTeamSizeSlider()));
		this.add(menu.getTeamSizeSlider());

		this.row();
		this.add(new Label("",this.getSkin()));
		this.row();

		this.add(menu.getBotSelector()).colspan(getDefaultColspan());
		this.row();
	}
}

package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.simulation.GameState;
import com.gats.ui.menu.Menu;

public class ExamAdmissionLayout extends GamemodeLayout{
	public ExamAdmissionLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menuInstance);
	}

	@Override
	protected void positionButtons(Menu menu) {
		//teamslider wont react
		getTeamAmountSlider().freezeSlider(true);
		getTeamAmountSlider().setRanges(1,1);
		getTeamSizeSlider().setRange(1,1);

		row();

		//set playercount to 1
		this.add(getBotSelector()).colspan(getDefaultColspan());


		menu.setMaps(getMapSelector(), GameState.GameMode.Exam_Admission);


		getBotSelector().resizeTable(1);
	}
}

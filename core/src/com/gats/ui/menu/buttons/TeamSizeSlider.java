package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TeamSizeSlider extends RelationSlider {
	public TeamSizeSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
	}

	/* TeamSize*TeamAmount= Spawnpoints
	* bei mapchange wird der TeamAmountSlider gecalled und verändert, mithilfe der bisher ausgewählten teamanzahl
	* Teamsize = Spawnpoints/teamAmount
	 *
	 */
	@Override
	public void adjustRelatedSliders(float min, float max) {

	}
}

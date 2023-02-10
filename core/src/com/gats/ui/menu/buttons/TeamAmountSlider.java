package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class TeamAmountSlider extends RelationSlider{
	public TeamAmountSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
	}

	public void adjustTeamSizeToSpawnpoints(int numberOfspawnpoints){
		if(numberOfspawnpoints==0){
			this.setRange(0,0);
			adjustRelatedSliders(0,0);
		}
		else{

		int maxTeamsize = numberOfspawnpoints/(int)this.getValue();
		if(maxTeamsize > 0){
			adjustRelatedSliders(1,maxTeamsize);
		}
		else{
			adjustRelatedSliders(1,1);
			this.setRange(1,numberOfspawnpoints);
		}
		}
	}

}

package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class RelationSlider extends Slider{

	Slider relatedSlider;
	boolean freeze;
	public RelationSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
		relatedSlider = null;

		freeze=false;
	}


	public void addRelatedSlider(Slider slider) {
		this.relatedSlider = slider;
	}

	/**
	 *  called after
	 * @param min
	 * @param max
	 */
	public void adjustRelatedSliders(float min, float max){
		if(!freeze) {
			if(relatedSlider !=null)
				relatedSlider.setRange(min, max);
		}
	}

	public void freezeSlider(boolean freeze){
		this.freeze=freeze;
	}


}

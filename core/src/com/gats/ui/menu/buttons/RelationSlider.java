package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import java.util.ArrayList;

public class RelationSlider extends Slider{

	ArrayList<Slider> relatedSliders;
	public RelationSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
	}

	public void setRelatedSliders(ArrayList<Slider> relatedSliders) {
		this.relatedSliders = relatedSliders;
	}

	public void addRelatedSlider(Slider slider) {
		this.relatedSliders.add(slider);
	}

	/**
	 *  called after
	 * @param min
	 * @param max
	 */
	public void adjustRelatedSliders(float min, float max){
		for (Slider slider:relatedSliders) {
			slider.setRange(min,max);
		}
	}


}

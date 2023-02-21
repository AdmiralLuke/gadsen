package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Slider for selecting Amount of Teams to play with
 */
public class TeamAmountSlider extends RelationSlider {

	BotSelectorTable botSelectorTable;
	int currentSpawnpoints;

	public TeamAmountSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
		currentSpawnpoints = 0;
		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeValues(currentSpawnpoints);
			}
		});

	}

	/**
	 * calls {@link BotSelectorTable#resizeTable(int value)} to adjust the amount of selectable bots
	 * @param value
	 */
	private void adjustBotSelector(int value) {
		if (this.botSelectorTable != null) {
			this.botSelectorTable.resizeTable(value);
		}
	}

	/**
	 * Changes the range of this and the TeamSizeSlider in relation to the number of Spawnpoints provided.
	 * @param numberOfspawnpoints
	 */
	public void adjustTeamSizeToSpawnpoints(int numberOfspawnpoints) {
		this.currentSpawnpoints = numberOfspawnpoints;
		if (numberOfspawnpoints == 0) {
			this.setRange(0, 0);
			adjustRelatedSliders(0, 0);
		} else {
			int selectedValue = (int) this.getValue();
			if(selectedValue==0){
				selectedValue=1;
			}
			int maxTeamsize = numberOfspawnpoints / selectedValue;
			if (maxTeamsize > 0) {
				adjustRelatedSliders(1, maxTeamsize);
			} else {
				adjustRelatedSliders(1, 1);
			}

			this.setRange(1, numberOfspawnpoints);
		}
	}

	/**
	 * This function gets called by other buttons (e.g. MapSelector) after a change happened to adjust the values to the correct one.
	 * @param spawnpoints of the current selection
	 */
	public void changeValues(int spawnpoints){

		if(!freeze) {
			adjustTeamSizeToSpawnpoints(spawnpoints);
			adjustBotSelector((int) getValue());
		}
	}

		/**
		 * Adds a reference to the BotSelector to this table, so it can be updated when {@link TeamAmountSlider}
		 * is changed. -> will call {@link BotSelectorTable#resizeTable(int)}
		 * @param botSelector a
		 */
		public void addBotSelector(BotSelectorTable botSelector){
			this.botSelectorTable=botSelector;
		}

}
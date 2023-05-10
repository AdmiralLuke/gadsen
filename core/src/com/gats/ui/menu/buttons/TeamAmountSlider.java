package com.gats.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Slider um die Anzahl der Teams zu bestimmen
 */
public class TeamAmountSlider extends RelationSlider {

	BotSelectorTable botSelectorTable;
	int currentSpawnpoints;
	int currentTeams;

	public TeamAmountSlider(float min, float max, float stepSize, boolean vertical, Skin skin) {
		super(min, max, stepSize, vertical, skin);
		currentSpawnpoints = 0;
		currentTeams = 0;

		this.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				adjustBotSelector((int)getValue());
			}
		});

	}

	/**
	 * ruft {@link BotSelectorTable#resizeTable(int value)} auf um die Anzahl der Auszuwählenden bots anzupassen
	 * @param value
	 */
	private void adjustBotSelector(int value) {
		if(!freeze) {
			if (this.botSelectorTable != null) {
				this.botSelectorTable.resizeTable(value);
			}
		}
	}

	///** Ändert die Intervalgrenzen von this und dem {@link TeamSizeSlider} in relation zu der Anzahl an Spawnpunkten auf der Map.
	// * @param numberOfspawnpoints
	// */
	//public void adjustTeamSizeToSpawnpoints(int numberOfspawnpoints) {
	//	//Todo: make this function interchangeable: eq. by moving it to an interface/function and then changing it for different modes
	//	this.currentSpawnpoints = numberOfspawnpoints;
	//	//Falls keine Spawnpunkte vorhanden sind, die Auswahl begrenzen auf 0
	//	if (numberOfspawnpoints == 0) {
	//		this.setRange(0, 0);
	//		adjustRelatedSliders(0, 0);
	//	} else {
	//		//Ausgewählte Teamanzahl nutzen, um die mögliche Teamgröße zu berechnen
	//		int selectedValue = (int) this.getValue();
	//		if(selectedValue==0){
	//			//Falls aus vorheriger auswahl kein Team ausgewählt wurde, wird der ausgewählte Wert auf 2 Teams gesetzt
	//			selectedValue=2;
	//		}

	//		//Teamgröße berechnen
	//		int maxTeamsize = numberOfspawnpoints / selectedValue;

	//		//Teamgröße muss mindestens 1 sein
	//		adjustRelatedSliders(1, Math.max(maxTeamsize, 1));

	//		this.setRange(1, numberOfspawnpoints);
	//	}
	//}

	/**
	 * Wird aufgerufen, wenn andere Buttons (bspw. MapSelector) verändert werden, um die Anzahl der Spawnpunkte/Teamgröße anzupassen
	 * @param spawnpoints of the current selection
	 */
	public void changeValues(int spawnpoints,int teams){

		if(!freeze) {

			setRanges(teams,spawnpoints);

			adjustBotSelector((int) getValue());
		}

	}

	public void setRanges(int teams, int teamsize ){

		this.setRange(1,teams);
		relatedSlider.setRange(1,teamsize);


	}

		/**
		 * Fügt eine Referenz auf einen BotSelectorAdds hinzu, sodass die Anzahl der BotAuswahlboxen angepasst werden kann durch{@link BotSelectorTable#resizeTable(int)}
		 * @param botSelector a
		 */
		public void addBotSelector(BotSelectorTable botSelector){
			this.botSelectorTable=botSelector;
		}

}
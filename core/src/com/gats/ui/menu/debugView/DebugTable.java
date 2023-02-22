package com.gats.ui.menu.debugView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gats.simulation.ActionLog;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;

public class DebugTable extends Table {

	ColoredLabelWithBackground playerLabel;

	Label characterLabel;

	final String playerText = "Spielerindex: ";
	final String characterText = "Characterindex: ";

	public DebugTable(Skin skin) {
		super(skin);

		playerLabel = new ColoredLabelWithBackground(playerText, skin,1,1,1,1);
		characterLabel = new Label(characterText, skin);
		rebuildTable();
	}

	public void addString(String string){
		ColoredLabelWithBackground label = new ColoredLabelWithBackground(string,this.getSkin(), Color.WHITE);
		label.setFontScale(0.7f);
		add(label).colspan(6);
		row();
	}

	public void addActionLog(ActionLog log){
		addString(log.toString());
	}



	public void updatePlayerLabel(int index) {
		playerLabel.setText(playerText + index);
	}

	public void updateCharacterLabel(int index) {
		playerLabel.setText(characterText + index);
	}

	/**
	 * Wird aufgerufen wenn der Table erneuert/cleared werden soll.
 	 */
	void rebuildTable(){
		add(playerLabel);
		add(characterLabel);
		row();
	}

	@Override
	public void clear() {
		super.clear();
		rebuildTable();
	}
}

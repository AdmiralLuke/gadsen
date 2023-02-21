package com.gats.ui.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class DebugTable extends Table {

	Label playerLabel;
	Label characterLabel;

	final String playerText = "Spielerindex: ";
	final String characterText = "Characterindex: ";

	public DebugTable(Skin skin) {
		super(skin);

		playerLabel = new Label(playerText, skin);
		characterLabel = new Label(characterText, skin);
	}

	public void addString(String string){
		add(new Label(string,this.getSkin()));
		row();
	}

	public void addActionLog(){

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

package com.gats.ui.menu.debugView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.simulation.ActionLog;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;

import javax.swing.text.View;

public class DebugTable extends Table {


	public DebugTable(Skin skin, Viewport viewport) {
		super(skin);

		rebuildTable();
	}

	public void addString(String string){
		ColoredLabelWithBackground label = new ColoredLabelWithBackground(string,this.getSkin(), Color.WHITE);
		label.setFontScale(0.7f);
		//label.setWrap(true);

		//if(this.getRows()*label.getHeight()> getParent().getHeight()){
		//	clear();
		//}
		add(label).colspan(6).align(Align.left);//.width(this.getWidth()/2);
		row();
	}

	public void addActionLog(ActionLog log){
		addString(log.toString());
	}



	/**
	 * Wird aufgerufen, wenn der Table erneuert/cleared werden soll.
 	 */
	void rebuildTable(){

	}

	@Override
	public void clear() {
		super.clear();
		rebuildTable();
	}
}

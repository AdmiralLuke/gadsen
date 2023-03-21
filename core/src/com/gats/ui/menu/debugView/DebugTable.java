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

	Viewport viewport;

	public DebugTable(Skin skin, Viewport viewport) {
		super(skin);
		this.viewport = viewport;
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


		if(this.getMinHeight()>viewport.getWorldHeight()*0.75){
			this.clear();
		};


	}

	public void addActionLog(ActionLog log){

		//Todo make it so every action is inserted separately
			// does not work with the current implementation i think
		addString(log.toString());
	}



	/**
	 * Wird aufgerufen, wenn der Table erneuert/cleared werden soll.
 	 */

	@Override
	public void clear() {
		super.clear();
	}
}

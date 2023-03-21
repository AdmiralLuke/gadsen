package com.gats.ui.menu.debugView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.simulation.ActionLog;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;


/**
 * Class for Storing the most recent ActionLogs, laying them out and drawing.
 */
public class DebugTextContainer extends VerticalGroup {


		Viewport viewport;

		Skin skin;

		Color textcolor = Color.WHITE;
		float fontscale = 0.7f;

		//size before elements get removed
		//percent of the viewport
		//currently 3/4 of the screen can be covered
		float maxSizePercent = 0.75f;

		public DebugTextContainer(Skin skin, Viewport viewport) {
			super();
			this.skin = skin;
			this.viewport = viewport;

			//text should align on the top left
			this.columnLeft();
			this.top();
			this.left();
			this.setFillParent(true);

		}

	/**
	 * Adds a String to the Bottom of the TextView
	 * @param string
	 */
	public void addString(String string){
		//Create a ColoredLabel to set a specific Color and Background for the label
		ColoredLabelWithBackground label = new ColoredLabelWithBackground(string,skin, textcolor);
		label.setFontScale(fontscale);
		addActor(label);

		adjustSize();
	}

	/** Removes the top Elements once the configured size is reached
	 *
 	 */
	private void adjustSize(){
		while(getPrefHeight()>viewport.getWorldHeight()*maxSizePercent){
			removeActorAt(0,false);
		}
	}

		public void addActionLog(ActionLog log){
			addString(log.toString());
		}



	}
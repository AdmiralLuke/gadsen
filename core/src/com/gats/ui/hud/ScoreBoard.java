package com.gats.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.gats.manager.Player;
import com.gats.simulation.GameState;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.menu.buttons.ScoreBoardLabel;

import java.util.ArrayList;

public class ScoreBoard extends VerticalGroup {


	public ScoreBoard(Color[] tcolors, String[] playerNames, GameState state){
		super();
		if (state!=null){
			//placement of the scorebeard
			top();
			right();
			pad(10);

			//setValues
			float[] scores = state.getScores();
			int numTeams = state.getTeamCount();
			if(playerNames==null){
				playerNames = new String[numTeams];
			}
			if(tcolors==null){
				tcolors = new Color[numTeams];
			}
			for(int i = 0;i<numTeams;i++){
				//brauche sowas wie Sliderlabel, muss den score verändern können, jedoch nicht den restlichen text
				//wo team names?
				if(tcolors[i]==null){
					tcolors[i]=Color.WHITE;
				}
				if(playerNames[i]==null){
					playerNames[i]="NoName";
				}

				ScoreBoardLabel l = new ScoreBoardLabel(playerNames[i], AssetContainer.MainMenuAssets.skin,tcolors[i]);
				l.adjustScore(scores[i]);
				addActor(l);
			}
		}

		setFillParent(true);

	}

	public void adjustScores(float[] score){
		for(int i = 0;i<score.length;i++){
			((ScoreBoardLabel) getChild(i)).adjustScore(score[i]);
		}

	}

}

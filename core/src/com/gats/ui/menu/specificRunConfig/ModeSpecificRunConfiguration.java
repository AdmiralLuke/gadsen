package com.gats.ui.menu.specificRunConfig;

import com.gats.manager.RunConfiguration;

/**
 * Creates a RunConfig from another, but only replaces null values;
 */
public abstract class ModeSpecificRunConfiguration extends RunConfiguration {

	public ModeSpecificRunConfiguration(RunConfiguration settings){
		super();
	}

	/**
	 * Replaces null variables with passed RunConfiguration.
	 * @param settings
	 */
	public void passSettings(RunConfiguration settings){

		if(gameMode==0){
			gameMode = settings.gameMode;
		}

		if(!gui){
			gui = settings.gui;
		}

		if(animationLogProcessor!=null){
			animationLogProcessor=settings.animationLogProcessor;
		}

		if(hud==null){
			hud = settings.hud;
		}

		if(mapName==null){
			mapName = settings.mapName;
		}

		if(teamCount == 0){
			teamCount = settings.teamCount;
		}
		if(teamSize==0){
			teamSize = settings.teamSize;
		}
		if(players==null){
			players = settings.players;
		}

	}

}

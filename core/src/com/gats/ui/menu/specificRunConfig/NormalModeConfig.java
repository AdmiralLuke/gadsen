package com.gats.ui.menu.specificRunConfig;

import com.gats.manager.RunConfiguration;

/**
 * RunConfig for the NormalGameMode
 */
public class NormalModeConfig extends  ModeSpecificRunConfiguration{


	public NormalModeConfig(RunConfiguration settings) {
		super(settings);
		passSettings(settings);
	}

}

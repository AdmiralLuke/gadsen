package com.gats.ui.menu.specificRunConfig;

import com.gats.manager.RunConfiguration;
import com.gats.simulation.GameState;

public class ExamAdmissionConfig extends ModeSpecificRunConfiguration{
	public ExamAdmissionConfig(RunConfiguration settings) {
		super(settings);

		settings.gameMode = GameState.GameMode.Exam_Admission;
		mapName = "";


		passSettings(settings);
	}
}

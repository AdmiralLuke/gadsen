package com.gats.ui.hud;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.gats.ui.assets.AssetContainer;

public class AimInformation extends VerticalGroup {

	private HorizontalGroup aimAngleGroup;
	private Label angleValueLabel;
	private Label angleTextLabel;

	private HorizontalGroup aimStrengthGroup;

	private Label strengthValueLabel;
	private Label strengthTextLabel;

	public AimInformation(String angleText,String strengthText){

	 	aimAngleGroup = new HorizontalGroup();
		this.angleTextLabel = new Label(angleText,AssetContainer.MainMenuAssets.skin);
		this.angleValueLabel = new Label("",AssetContainer.MainMenuAssets.skin);

		aimAngleGroup.addActor(this.angleTextLabel);
		aimAngleGroup.addActor(this.angleValueLabel);

		aimStrengthGroup = new HorizontalGroup();

		this.strengthTextLabel = new Label(strengthText,AssetContainer.MainMenuAssets.skin);
		this.strengthValueLabel = new Label("",AssetContainer.MainMenuAssets.skin);


		aimStrengthGroup.addActor(strengthTextLabel);
		aimStrengthGroup.addActor(strengthValueLabel);


		addActor(aimAngleGroup);
		addActor(aimStrengthGroup);

		angleTextLabel.setFontScale(0.5f);
		strengthTextLabel.setFontScale(0.5f);

		space(-10);
	}



	public void setValues(float angle, float strength){
		setAimAngle(angle);
		setAimStrength(strength);
	}


	public void setAimAngle(float angle){
		angleValueLabel.setText((char)angle);
	}
	public void setAimStrength(float strength){
		strengthValueLabel.setText((char)strength);
	}
}

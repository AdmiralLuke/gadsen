package com.gats.ui.hud;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.gats.ui.assets.AssetContainer;


/**
 * Class for displaying the aim Values in text
 */
public class AimInformation extends VerticalGroup {

	private Table aimAngleGroup;
	private Label angleValueLabel;
	private Label angleTextLabel;

	private Table aimStrengthGroup;

	private Label strengthValueLabel;
	private Label strengthTextLabel;



	public AimInformation(String angleText,String strengthText){


		//Todo change color of aimInformation
	 	aimAngleGroup = new Table();

		this.angleTextLabel = new Label(angleText,AssetContainer.MainMenuAssets.skin);
		this.angleValueLabel = new Label("",AssetContainer.MainMenuAssets.skin);

		aimAngleGroup.debug();


		aimStrengthGroup = new Table();

		this.strengthTextLabel = new Label(strengthText,AssetContainer.MainMenuAssets.skin);
		this.strengthValueLabel = new Label("",AssetContainer.MainMenuAssets.skin);

		//setup text alignment
		angleValueLabel.setAlignment(Align.right);
		aimAngleGroup.add(this.angleValueLabel).width(12).right().expandX().align(Align.right);
		aimAngleGroup.add(this.angleTextLabel).right().expandX();

		aimStrengthGroup.add(strengthValueLabel).width(12).right().expandX().align(Align.right);
		aimStrengthGroup.add(strengthTextLabel).right().expandX();


		addActor(aimAngleGroup);
		addActor(aimStrengthGroup);

		space(0);

		setFontScale(0.5f);
	}


	/**
	 * Change the displayed aim and strength values
	 * @param angle
	 * @param strength
	 */
	public void setValues(float angle, float strength){
		setAimAngle(angle);
		setAimStrength(strength);
	}

	/**
	 * Adjusts the font scaling of all Text Labels
	 * @param scale
	 */
	public void setFontScale(float scale){
		angleTextLabel.setFontScale(scale);
		angleValueLabel.setFontScale(scale);
		strengthTextLabel.setFontScale(scale);
		strengthValueLabel.setFontScale(scale);
	}

	@Override
	public VerticalGroup align(int align) {
		aimAngleGroup.align(align);
		aimStrengthGroup.align(align);
		return super.align(align);

	}

	/**
	 * Changes the displayed Angle Value. Extracts the first number after the comma and then rounds the angle value.
	 * @param angle
	 */
	public void setAimAngle(float angle){

		int anglePostComma = (int)(angle*10)%10;

		angle = Math.round(angle);
		angleValueLabel.setText((int)angle + "." +anglePostComma);
	}

	/**
	 * Changes the displayed strength Value.
	 * @param strength
	 */
	public void setAimStrength(float strength){
		strengthValueLabel.setText((char)(100*strength));
	}
}

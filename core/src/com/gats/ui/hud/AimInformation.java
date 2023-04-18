package com.gats.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;


/**
 * Class for displaying the aim Values in text
 */
public class AimInformation extends VerticalGroup {


	private Color textColor;
	private Table aimAngleGroup;
	private ColoredLabelWithBackground angleValueLabel;
	private ColoredLabelWithBackground angleTextLabel;

	private Table aimStrengthGroup;

	private ColoredLabelWithBackground strengthValueLabel;
	private  ColoredLabelWithBackground strengthTextLabel;



	public AimInformation(String angleText,String strengthText){

		TextureRegionDrawable background = new TextureRegionDrawable(AssetContainer.IngameAssets.pixel);

		//Todo change color of aimInformation
		textColor = new Color(Color.SCARLET);
	 	aimAngleGroup = new Table();
		this.angleTextLabel = new ColoredLabelWithBackground(angleText,AssetContainer.MainMenuAssets.skin,textColor,background);
		this.angleValueLabel = new ColoredLabelWithBackground("",AssetContainer.MainMenuAssets.skin,textColor,background);

		aimStrengthGroup = new Table();

		this.strengthTextLabel = new ColoredLabelWithBackground(strengthText,AssetContainer.MainMenuAssets.skin,textColor,background);
		this.strengthValueLabel = new ColoredLabelWithBackground("",AssetContainer.MainMenuAssets.skin,textColor,background);

		//setup text alignment
		//size currently hardcoded, easier to adjust without creating new methods, ui does not change much
		//still adjusts size based on screen size change
		angleValueLabel.setAlignment(Align.right);
		aimAngleGroup.add(this.angleValueLabel).width(68).right().expandX().align(Align.right);
		aimAngleGroup.add(this.angleTextLabel).right().expandX();

		aimStrengthGroup.add(strengthValueLabel).width(44).right().expandX().align(Align.right);
		aimStrengthGroup.add(strengthTextLabel).right().expandX();


		addActor(aimAngleGroup);
		addActor(aimStrengthGroup);

		space(0);

		setFontScale(2);
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

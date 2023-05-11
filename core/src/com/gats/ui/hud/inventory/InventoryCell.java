package com.gats.ui.hud.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.gats.simulation.weapons.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.menu.buttons.ColoredLabelWithBackground;


/**
 * Class representing a single inventory Slot.
 */
public class InventoryCell extends Image {

	private InventoryWeapon weapon;
	private float sizeBy=1;
	private boolean selected;

	private Color selectedColor;
	private ColoredLabelWithBackground ammoLabel;
	public InventoryCell(TextureRegion background) {
		super(background);
		//Todo make size adjustable, either by implementing it, so it can be resized easily, or just creating a size parameter for the constructor.
		//hardcoded size, because i could not figure out a nice way with to set it via the table
		//i just wasted so much time trying to scale this >:O
		setSize(64,64);
		Color baseColor = new Color(0.7f,0.7f,0.7f,1);
		setColor(baseColor);

		selectedColor = new Color(1, 1, 1,1);
		ammoLabel = new ColoredLabelWithBackground("", AssetContainer.MainMenuAssets.skin);
		ammoLabel.setFontScale(2);


		positionAmmoLabel();
	}

	void setWeapon(Weapon weapon){
		TextureRegion weaponIcon = AssetContainer.IngameAssets.weaponIcons.get(weapon.getType());
		if(weaponIcon==null){
			weaponIcon = AssetContainer.IngameAssets.coolCat.getKeyFrame(0);
		}
		this.weapon = new InventoryWeapon(weapon, weaponIcon);

		if(ammoLabel!=null) {
			if (weapon.getAmmo() > 99) {
				ammoLabel.setText("€");
			} else {
				ammoLabel.setText(weapon.getAmmo());
			}
		}
	}


	void setSelected(boolean selected){
		this.selected = selected;
	}
	WeaponType getWeaponType(){
		return weapon.getWeaponType();
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
	}

	@Override
	public float getPrefWidth() {
		return super.getWidth();
	}

	@Override
	public float getPrefHeight() {
		return super.getHeight();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		Image icon = null;
		if(weapon!=null) {
			icon = new Image(weapon.getIcon());
			icon.setColor(getColor());
			icon.setScaleX(getScaleX());
		}


		Drawable drawable = getDrawable();
		float imageX = getImageX();
		float imageY = getImageY();
		float imageWidth = getImageWidth();
		float imageHeight = getImageHeight();


		//copy the basic image draw function and add the item icon  be drawn aswell
		validate();

		Color color = getColor();
		if(selected){
			//highlight the cellSprite, if it is selected
			color = selectedColor;
		}
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();

		if (drawable != null) drawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		//color the batch white for drawing the icon
		batch.setColor(Color.WHITE);

		if(icon != null){
			if(((TextureRegionDrawable)(icon.getDrawable())).getRegion()!=null) {
				icon.getDrawable().draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
			}
		}
		ammoLabel.draw(batch,parentAlpha);


	}
	@Override
	protected void positionChanged() {
		super.positionChanged();
		positionAmmoLabel();
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		positionAmmoLabel();

	}

	private void positionAmmoLabel(){
		if(ammoLabel!=null) {
			ammoLabel.setPosition(getX() + getImageX(), getY() + (54));
		}
	}
}

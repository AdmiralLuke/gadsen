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


/**
 * Class representing a single inventory Slot.
 */
public class InventoryCell extends Image {

	private InventoryWeapon weapon;
	private float scale;
	private boolean selected;

	private Color selectedColor;
	public InventoryCell(TextureRegion background) {
		super(background);
		this.scale = 1;
		Color baseColor = new Color(0.7f,0.7f,0.7f,1);
		setColor(baseColor);

		selectedColor = new Color(1, 1, 1,1);

	}

	void setWeapon(Weapon weapon){
		TextureRegion weaponIcon = AssetContainer.IngameAssets.weaponIcons.get(weapon.getType());
		if(weaponIcon==null){
			weaponIcon = AssetContainer.IngameAssets.coolCat.getKeyFrame(0);
		}
		this.weapon = new InventoryWeapon(weapon, weaponIcon);
	}


	void setSelected(boolean selected){
		this.selected = selected;
	}
	WeaponType getWeaponType(){
		return weapon.getWeaponType();
	}
	@Override
	public float getPrefHeight() {
		return super.getPrefHeight()*scale;
	}

	@Override
	public float getPrefWidth() {
		return super.getPrefWidth()*scale;
	}

	public void scaleSizeBy(float scale){
		this.scale = scale;
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

		if (drawable instanceof TransformDrawable) {
			float rotation = getRotation();
			if (scaleX != 1 || scaleY != 1 || rotation != 0) {
				((TransformDrawable)drawable).draw(batch, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
						imageWidth, imageHeight, scaleX, scaleY, rotation);
				if(icon!=null&&icon.getDrawable() instanceof TransformDrawable){

					((TransformDrawable)icon.getDrawable()).draw(batch, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
							imageWidth, imageHeight, scaleX, scaleY, rotation);
				}
				return;
			}
		}
		if (drawable != null) drawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		//color the batch white for drawing the icon
	batch.setColor(Color.WHITE);

		if(icon != null){
			if(((TextureRegionDrawable)(icon.getDrawable())).getRegion()!=null) {
				icon.getDrawable().draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
			}
		}

	}


}

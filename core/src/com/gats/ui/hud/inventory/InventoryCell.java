package com.gats.ui.hud.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.gats.simulation.Weapon;
import com.gats.ui.assets.AssetContainer;


/**
 * Class representing a single inventory Slot.
 */
public class InventoryCell extends Image {

	private InventoryWeapon weapon;
	private float sizeBy=1;
	private boolean selected;

	private Color selectedColor;
	public InventoryCell(TextureRegion background) {
		super(background);
		//Todo make size adjustable, either by implementing it, so it can be resized easily, or just creating a size parameter for the constructor.
		//hardcoded size, because i could not figure out a nice way with to set it via the table
		//i just wasted so much time trying to scale this >:O
		setSize(64,64);
		Color baseColor = new Color(0.7f,0.7f,0.7f,1);
		setColor(baseColor);

		selectedColor = new Color(1, 1, 1,1);
	}

	void setWeapon(Weapon weapon){
		this.weapon = new InventoryWeapon(weapon, AssetContainer.IngameAssets.weaponIcons.get(weapon.getType()));
	}


	void setSelected(boolean selected){
		this.selected = selected;
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
		if(icon != null&&icon.getDrawable()!=null) icon.getDrawable().draw(batch,x+imageX,y+imageY,imageWidth*scaleX,imageHeight*scaleY);

	}
}

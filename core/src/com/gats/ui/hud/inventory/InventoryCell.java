package com.gats.ui.hud.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;


/**
 * Class representing a single inventory Slot.
 */
public class InventoryCell extends Image {
	private	Image item;

	private int ammo;

	private float scale;
	public InventoryCell(TextureRegion background) {
		super(background);
		this.scale = 1;

	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void setItem(TextureRegion itemIcon) {
		if(itemIcon!=null) {
			this.item = new Image(itemIcon);
		}
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


		if(item!=null) {
			item.setColor(getColor());
			item.setScaleX(getScaleX());
		}


		Drawable drawable = getDrawable();
		float imageX = getImageX();
		float imageY = getImageY();
		float imageWidth = getImageWidth();
		float imageHeight = getImageHeight();


		//copy the basic image draw function and add the item icon  be drawn aswell
		validate();

		Color color = getColor();
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
				if(item!=null&&item.getDrawable() instanceof TransformDrawable){

					((TransformDrawable)item.getDrawable()).draw(batch, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
							imageWidth, imageHeight, scaleX, scaleY, rotation);
				}
				return;
			}
		}
		if (drawable != null) drawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		if(item != null&&item.getDrawable()!=null) item.getDrawable().draw(batch,x+imageX,y+imageY,imageWidth*scaleX,imageHeight*scaleY);

	}
}

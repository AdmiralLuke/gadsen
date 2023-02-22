package com.gats.ui.menu.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.awt.*;

public class ColoredLabelWithBackground extends Label {

	Drawable background;
	public ColoredLabelWithBackground(CharSequence text, Skin skin, float[] colors) {
		this(text,skin,colors[0],colors[1],colors[2],colors[3]);

	}
	public ColoredLabelWithBackground(CharSequence text,Skin skin, float r, float g, float b,float a){
		super(text,skin);
		this.setColor(r,g,b,a);
		this.background= skin.getDrawable("base");
		//Todo add background color
	}

	 void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		if (background == null) return;
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		background.draw(batch, x, y, getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawBackground(batch,parentAlpha,this.getX(),this.getY());
		super.draw(batch, parentAlpha);
	}
}

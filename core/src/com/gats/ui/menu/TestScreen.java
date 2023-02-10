package com.gats.ui.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.ui.GADSAssetManager;
import com.gats.ui.menu.layout.ActorLayout;
import com.gats.ui.menu.layout.BasicLefthandLayer;
import com.gats.ui.menu.layout.Layer;

import static java.lang.Math.abs;

public class TestScreen implements Screen {



	private SpriteBatch batch;
	private TextureRegion texture;
	private Viewport viewport;
	private Stage test;

	private int wordlWidth;
	private int worldHeight;
	private ActorLayout layout;
	public TestScreen(SpriteBatch batch, GADSAssetManager assetManager){
	this.batch = batch;
	this.texture = assetManager.getAtlas().findRegion("tile/16x_box01");
	this.viewport = new ExtendViewport(256,256);
		viewport.setCamera(new OrthographicCamera());
		this.test = new Stage(this.viewport,this.batch);
		this.layout = new ActorLayout(new Vector2(viewport.getWorldWidth(),viewport.getWorldHeight()));
		for (int i = 0; i<8;i++){
			Layer layer =  new BasicLefthandLayer(new Vector2(0,worldHeight/3f));
			for(int j = 0; j<i; j++){
					layer.addElement(new Image(texture));
			}
			layout.addLayer(layer);
			layer.setElementDistance(16);
		}

		test.addActor(layout);
	}





	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

//
		batch.setProjectionMatrix(viewport.getCamera().combined);
//	drawLayout();

		test.act();
		test.draw();
	}


	public void drawLayout(){


		//kann in resize, bzw sollte sich worldheight nie ändern?!

		worldHeight =(int)viewport.getWorldHeight();
		wordlWidth =(int)viewport.getWorldWidth();

		int centerX = wordlWidth/2;
		int centerY = worldHeight/2;
	int sectionAmount = 8;
	int sectionOffset = worldHeight /sectionAmount;


	for (int i =1;sectionAmount+1>i;i++){

	//		batch.draw(texture,centerX-texture.getRegionWidth()/2,i*sectionOffset-texture.getRegionHeight()/2);
		//for(int layers = -i/2;layers<(i/2);layers++) {
		//
		//	batch.draw(texture, calculateLayerPositionRelToCenter(texture, centerX, layers), i * sectionOffset - texture.getRegionHeight() / 2);
		//}

		int layerElementCount = i;
		boolean isEven = layerElementCount%2 ==0;
		while(layerElementCount>0){
		batch.draw(texture,calculateLayerPositionRelToCenter(texture,centerX,layerElementCount),i*sectionOffset-texture.getRegionHeight()/2);
			layerElementCount++;
		}
	}


	}

	public float calculateLayerPositionRelToCenter(TextureRegion textureRegion,int center, int layerElementIndex){


//		besser ganze Ebene verarbeiten, bzw. ebenengröße übergeben

		//gets sign of the integer, abuses bit representation to get the 32nd bit, wich is the sign
if (abs(layerElementIndex)-1==0){return center;}
		int signOfElement = layerElementIndex >> 31;
	int position=		(textureRegion.getRegionWidth()*(abs(layerElementIndex)-1));
		return signOfElement < 0 ? center+ texture.getRegionWidth() - position: center-texture.getRegionWidth() + position;
	}
	@Override
	public void resize(int width, int height) {
viewport.update(width,height);
viewport.apply(true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}

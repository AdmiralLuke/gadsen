package com.gats.ui.menu.debugView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.*;

public class DebugView {


	private int padding;
	private Batch batch;
	private Viewport viewport;

	private BitmapFont font;

	private String currentState = "";

	private Stage stage;
	private DebugTable layoutTable;

	public DebugView(Skin skin){
		viewport = new ExtendViewport(500,500,2000,2000);
		viewport.setCamera(new OrthographicCamera(30,30*(Gdx.graphics.getHeight()*1f/Gdx.graphics.getWidth())));
		viewport.apply();

		this.batch = new SpriteBatch();

		stage = new Stage(viewport,batch);

		layoutTable = createTable(skin);

		stage.addActor(layoutTable);

	}


	public void addToInformation(String info){
		currentState += info+"\n" ;
	}
	public void refreshState(){
		this.currentState = "Turn";
	}

	public void setCurrentPlayerAndChar(int player,int character){

		layoutTable.updatePlayerLabel(player);
		layoutTable.updateCharacterLabel(character);

	}
	String baseInformation(){

		return "";
	}

	DebugTable createTable(Skin skin){
		DebugTable table = new DebugTable(skin);
		table.setFillParent(true);
		table.top();
		table.left();
		return table;
	}

	public void draw() {
		viewport.apply(true);
		batch.setProjectionMatrix(viewport.getCamera().combined);

		if(currentState!=null) {
			stage.act();
			stage.draw();

			//	batch.begin();
			//	font.draw(batch, currentState, 0,viewport.getWorldHeight());
			//	batch.end();


		}
	}

	public Viewport getViewport() {
		return viewport;
	}

	//Stage besitzen?
	/*
	Viewport
	 */

	//todo resize()
}

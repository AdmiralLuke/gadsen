package com.gats.ui.menu.debugView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.simulation.ActionLog;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DebugView {


	private int padding;
	private Batch batch;
	private Viewport viewport;

	private BitmapFont font;

	private String currentState = "";

	private Stage stage;
	private final DebugTable layoutTable;

	private final DebugTextContainer debugText;


	private boolean isEnabled;

	private LinkedBlockingQueue<ActionLog> blockingLogQueue = new LinkedBlockingQueue<>();

	public DebugView(Skin skin){
		viewport = new ExtendViewport(600,600);
		viewport.setCamera(new OrthographicCamera(30,30*(Gdx.graphics.getHeight()*1f/Gdx.graphics.getWidth())));
		viewport.apply();


		this.batch = new SpriteBatch();

		stage = new Stage(viewport,batch);

		layoutTable = createTable(skin );
		debugText = new DebugTextContainer(skin,viewport);

		//stage.addActor(layoutTable);
		stage.addActor(debugText);
		isEnabled = false;

	}


	/**
	 * Fügt Action log in die View ein;
	 * @param log
	 */
	public void add(ActionLog log){

		blockingLogQueue.add(log);
		//try to make multithreading not kaboom
		//does not work


	}

	/**
	 * Fügt einen String in die View/Table ein.
	 * @param string
	 */
	public void add(String string){


	}

	DebugTable createTable(Skin skin){
		DebugTable table = new DebugTable(skin,viewport);
		table.setFillParent(true);
		table.setDebug(true);
		table.top();
		table.left();
		return table;
	}

	public void draw() {


			ActionLog log = blockingLogQueue.poll();
			if(log!=null) {
				layoutTable.addActionLog(log);
				debugText.addActionLog(log);
			}

		viewport.apply(true);
		batch.setProjectionMatrix(viewport.getCamera().combined);

		if(stage!=null&&isEnabled) {
			stage.draw();
			//	batch.begin();
			//	font.draw(batch, currentState, 0,viewport.getWorldHeight());
			//	batch.end();

		}
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void toggleDebugView() {
		isEnabled = !isEnabled;
	}

	//todo resize()?
}

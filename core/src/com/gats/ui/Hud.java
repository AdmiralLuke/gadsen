package com.gats.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.hud.GadsenInputProcessor;
import com.gats.ui.hud.InputHandler;
import com.gats.ui.hud.TurnSplashScreen;
import com.gats.ui.hud.inventory.InventoryDrawer;

import javax.swing.text.View;

/**
 * Class for taking care of the User Interface.
 * Input Handling during the game.
 * Displaying health, inventory
 */
public class Hud {

    private Stage stage;
	private InputHandler inputHandler;
	private InventoryDrawer inventoryDrawer;
	private TurnSplashScreen turnSplashScreen;
	private Table layoutTable;
	private Viewport viewport;

	public Hud(InGameScreen ingameScreen) {
		int viewportSizeX = 512;
		int viewportSizeY = 512;
		Camera cam = new OrthographicCamera(viewportSizeX,viewportSizeY);

		this.viewport = new ExtendViewport(viewportSizeX,viewportSizeY,cam);
        stage = new Stage(viewport);

        setupLayoutTable();
		setupInventory();
		setupInputHandler(ingameScreen);
	}

	private void setupInventory(){
		float inventoryScale = 1;
		inventoryDrawer = new InventoryDrawer();
		inventoryDrawer.setScale(inventoryScale);
		layoutTable.add(inventoryDrawer).pad(20);
	}

	private void setupInputHandler(InGameScreen ingameScreen){
		inputHandler = new InputHandler(ingameScreen);
	}

    private void setupLayoutTable(){
        layoutTable = new Table(AssetContainer.MainMenuAssets.skin);

        layoutTable.setFillParent(true);
        //debug
        layoutTable.setDebug(true);
		//align the table to the left of the stage
		layoutTable.left();
        stage.addActor(layoutTable);
    }

	public GadsenInputProcessor getInputHandler() {
		return inputHandler;
	}

	public void draw() {
		//apply the viewport, so the glViewport is using the correct settings for drawing
		stage.getViewport().apply(true);
       	stage.draw();
	}

	protected void tick(float delta) {
		inputHandler.tick(delta);
        stage.act(delta);
	}

	public InventoryDrawer getInventoryDrawer() {
		return inventoryDrawer;
	}

	public TurnSplashScreen getTurnSplashScreen() {
		return turnSplashScreen;
	}

	public void resizeViewport(int width, int height){
		stage.getViewport().update(width,height,true);
	}
}

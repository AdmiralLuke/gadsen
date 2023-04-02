package com.gats.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.manager.HumanPlayer;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.hud.*;
import com.gats.ui.hud.inventory.InventoryDrawer;

import java.util.List;

/**
 * Class for taking care of the User Interface.
 * Input Handling during the game.
 * Displaying health, inventory
 */
public class Hud {

    private Stage stage;
	private InputHandler inputHandler;
	private InputMultiplexer inputMultiplexer;
	private InventoryDrawer inventoryDrawer;
	private TurnSplashScreen turnSplashScreen;
	private Table layoutTable;
	private Viewport viewport;


	private UiMessenger uiMessenger;
	public Hud(InGameScreen ingameScreen) {

		int viewportSizeX = 256;
		int viewportSizeY = 256;
		float animationSpeedupValue = 8;

		Camera cam = new OrthographicCamera(viewportSizeX,viewportSizeY);

		this.viewport = new ExtendViewport(viewportSizeX,viewportSizeY,cam);
        stage = new Stage(viewport);

        layoutTable = setupLayoutTable();
		inventoryDrawer = setupInventory();
		inputHandler = setupInputHandler(ingameScreen);


		this.uiMessenger = new UiMessenger(ingameScreen,inventoryDrawer,turnSplashScreen);

		FastForwardButton fastButton =	setupFastForwardButton(uiMessenger, animationSpeedupValue);

		layoutHudElements(layoutTable,inventoryDrawer,fastButton);


		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputHandler);
		inputMultiplexer.addProcessor(stage);

		stage.addActor(layoutTable);
	}

	private void layoutHudElements(Table table,InventoryDrawer inventoryDrawer,FastForwardButton fastForwardButton) {
		table.add(inventoryDrawer).pad(20);
		table.row();
		table.add(fastForwardButton).pad(20);

	}

	private InventoryDrawer setupInventory(){
		float inventoryScale = 1;
		InventoryDrawer invDraw = new InventoryDrawer();
		invDraw.setScale(inventoryScale);

		return invDraw;
	}

	private InputHandler setupInputHandler(InGameScreen ingameScreen){
		return new InputHandler(ingameScreen);
	}

    private Table setupLayoutTable(){
       Table table = new Table(AssetContainer.MainMenuAssets.skin);

        table.setFillParent(true);
        //debug
        table.setDebug(true);
		//align the table to the left of the stage
		table.left();
		return table;
    }

	private FastForwardButton setupFastForwardButton(UiMessenger uiMessenger,float speedUp){

		FastForwardButton button = new FastForwardButton(new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButton),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonPressed),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonChecked),
				uiMessenger,speedUp);
		return button;
	}

	public GadsenInputProcessor getGadsenInputProcessor() {
		return inputHandler;
	}

	public InputProcessor getInputProcessor(){
		return inputMultiplexer;
	}

	public void setHumanPlayers(List<HumanPlayer> humanPlayers){
		inputHandler.setHumanPlayers(humanPlayers);
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

	public UiMessenger getUiMessenger() {
		return uiMessenger;
	}
}

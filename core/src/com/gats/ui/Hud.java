package com.gats.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.manager.HumanPlayer;
import com.gats.manager.RunConfiguration;
import com.gats.ui.assets.AssetContainer;
import com.gats.ui.hud.*;
import com.gats.ui.hud.inventory.InventoryDrawer;
import com.gats.ui.hud.ImagePopup;

import java.util.List;

/**
 * Class for taking care of the User Interface.
 * Input Handling during the game.
 * Displaying health, inventory
 */
public class Hud implements Disposable {

    private Stage stage;
	private InputHandler inputHandler;
	private InputMultiplexer inputMultiplexer;
	private InventoryDrawer inventory;
	private Table layoutTable;
	private Viewport viewport;

	private VerticalGroup turnPopupContainer;
	private InGameScreen inGameScreen;

	private TextureRegion turnChangeSprite;



	private UiMessenger uiMessenger;
	public Hud(InGameScreen ingameScreen, RunConfiguration runConfig) {

		this.inGameScreen = ingameScreen;
		int viewportSizeX = 256;
		int viewportSizeY = 256;
		float animationSpeedupValue = 8;
		turnChangeSprite = AssetContainer.IngameAssets.turnChange;
		Camera cam = new OrthographicCamera(viewportSizeX,viewportSizeY);
		//Viewport entweder extend oder Fit -> noch nicht sicher welchen ich nehmen soll
		this.viewport = new ExtendViewport(viewportSizeX,viewportSizeY,cam);
        stage = new Stage(viewport);


        layoutTable = setupLayoutTable();

		inventory = setupInventoryDrawer(runConfig);
		inputHandler = setupInputHandler(ingameScreen);


		this.uiMessenger = new UiMessenger(this);

		FastForwardButton fastButton =	setupFastForwardButton(uiMessenger, animationSpeedupValue);

		turnPopupContainer = new VerticalGroup();
		layoutHudElements(layoutTable, inventory,fastButton,turnPopupContainer);


		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputHandler);
		inputMultiplexer.addProcessor(stage);

		stage.addActor(layoutTable);
	}

	private void layoutHudElements(Table table, InventoryDrawer inventory, FastForwardButton fastForwardButton,VerticalGroup turnPopupContainer) {
		float padding = 10;
		table.add(this.inventory).pad(padding).expandX().left();
		//set a fixed size for the turnPopupContainer, so it will not change the layout, once the turn Sprite is added
		table.add(turnPopupContainer).expandX().size(turnChangeSprite.getRegionWidth(),turnChangeSprite.getRegionHeight());

		table.row();
		table.add(fastForwardButton).pad(padding).expandX().left();

	}

	private InventoryDrawer setupInventoryDrawer(RunConfiguration runConfiguration){
		float inventoryScale = 1;
		//InventoryDrawerDrawerDrawer invDraw = new InventoryDrawerDrawer(runConfiguration);
		//invDraw.setScale(inventoryScale);

		InventoryDrawer invDraw = new InventoryDrawer(runConfiguration);
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
		table.center();
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
		return inventory;
	}

	public void createTurnChangePopup() {
		if(turnPopupContainer.hasChildren()) {
			turnPopupContainer.removeActorAt(0, false);
		}
		turnPopupContainer.addActor(new ImagePopup(turnChangeSprite,1));
	}

	public void resizeViewport(int width, int height){
		stage.getViewport().update(width,height,true);
	}

	public UiMessenger getUiMessenger() {
		return uiMessenger;
	}

	public void setRenderingSpeed(float speed){
		inGameScreen.setRenderingSpeed(speed);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}

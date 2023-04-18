package com.gats.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
	private TurnTimer turnTimer;
	private Table layoutTable;
	private Container<ImagePopup> turnPopupContainer;
	private InGameScreen inGameScreen;

	private TextureRegion turnChangeSprite;

	private float turnChangeDuration;

	private UiMessenger uiMessenger;

	private FastForwardButton fastForwardButton;

	private AimInformation aimInfo;

	private StaminaBar staminaBar;

	private boolean debugVisible;
	public Hud(InGameScreen ingameScreen, RunConfiguration runConfig) {

		this.inGameScreen = ingameScreen;


		int viewportSizeX = 1028;
		int viewportSizeY = 1028;
		float animationSpeedupValue = 8;
		turnChangeDuration = 1;
		turnChangeSprite = AssetContainer.IngameAssets.turnChange;


		staminaBar = new StaminaBar(0,0,1,false,AssetContainer.MainMenuAssets.skin);
		Camera cam = new OrthographicCamera(viewportSizeX,viewportSizeY);
		//Viewport entweder extend oder Fit -> noch nicht sicher welchen ich nehmen soll
		Viewport viewport= new ExtendViewport(viewportSizeX,viewportSizeY,cam);


		stage = new Stage(viewport);
        layoutTable = setupLayoutTable();

		aimInfo = new AimInformation(" Grad"," %");
		inventory = setupInventoryDrawer(runConfig);
		inputHandler = setupInputHandler(ingameScreen);
		turnTimer = new TurnTimer(AssetContainer.IngameAssets.turnTimer,AssetContainer.MainMenuAssets.skin);
		turnTimer.setCurrentTime(3);
		this.uiMessenger = new UiMessenger(this);

		fastForwardButton =	setupFastForwardButton(uiMessenger, animationSpeedupValue);

		turnPopupContainer = new Container<ImagePopup>();
		layoutHudElements();

		//Combine input from both processors
		inputMultiplexer = new InputMultiplexer();
		//needed for input for the simulation
		inputMultiplexer.addProcessor(inputHandler);
		//input for the ui buttons
		inputMultiplexer.addProcessor(stage);

		stage.addActor(layoutTable);
	}


	/**
	 * Creates an {@link InventoryDrawer} with options from the {@link RunConfiguration}.
	 * @param runConfiguration
	 * @return
	 */
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


	/**
	 * Creates a Table for the Button/Element Layout and applies some Settings.
	 * @return
	 */
    private Table setupLayoutTable(){
       Table table = new Table(AssetContainer.MainMenuAssets.skin);

        table.setFillParent(true);
		//align the table to the left of the stage
		table.center();
		return table;
    }


	/**
	 * Places Hud Elements inside the Table to define their positions on the screen.
	 */
	private void layoutHudElements() {
		float padding = 10;

		//currently setting the element size of elements in their class file: hardcoded
		//changing the size via the table/actor methods does not really work. could be a fault of not implementing the ui elementparents correctly
		//-> yet it is a bit too much work for now
		//Todo Refactor resizing of every Ui element



		staminaBar.setSize(1,48);

		layoutTable.add(this.inventory).pad(padding).expandX().expandY().left().width(aimInfo.getPrefWidth());
		//set a fixed size for the turnPopupContainer, so it will not change the layout, once the turn Sprite is added
		layoutTable.add(turnPopupContainer).pad(padding).expandX().expandY().size(200,200);
		layoutTable.add(aimInfo).expandX().expandY().pad(padding).right().align(Align.right);
		layoutTable.row();
		layoutTable.add(fastForwardButton).pad(padding).left().bottom().size(64,64);

		layoutTable.add(staminaBar).pad(padding).center().bottom().width(384);
		layoutTable.add(turnTimer).pad(padding).right().bottom();
	}


	/**
	 * Creates a {@link FastForwardButton} with the correct sprites.
	 * @param uiMessenger
	 * @param speedUp
	 * @return
	 */
	private FastForwardButton setupFastForwardButton(UiMessenger uiMessenger,float speedUp){

		FastForwardButton button = new FastForwardButton(new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButton),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonPressed),
				new TextureRegionDrawable(AssetContainer.IngameAssets.fastForwardButtonChecked),
				uiMessenger,speedUp);
		return button;
	}

	/**
	 * Input Processor handling all of the Inputs meant to be sent to {@link com.gats.simulation.Simulation} via {@link HumanPlayer}
	 * @return
	 */
	public GadsenInputProcessor getGadsenInputProcessor() {
		return inputHandler;
	}

	/**
	 * Returns all Input Processors inside a Multiplexer.
	 * @return
	 */
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


	/**
	 * Creates a Turn Change Popup for {@link Hud#turnChangeDuration} second
	 */
	public void createTurnChangePopup() {
		drawImagePopup(new ImagePopup(turnChangeSprite,turnChangeDuration));
	}

	public void drawImagePopup(ImagePopup image){
		if(turnPopupContainer.hasChildren()) {
			turnPopupContainer.removeActorAt(0,false);
		}
		turnPopupContainer.setActor(image);
		turnPopupContainer.bottom().left();
		turnPopupContainer.fill();
	}

	public void resizeViewport(int width, int height){
		stage.getViewport().update(width,height,true);
	}

	public UiMessenger getUiMessenger() {
		return uiMessenger;
	}

	/**
	 * Changes the animation playback speed.
	 * @param speed Will multiply with the normal playback.
	 */
	public void setRenderingSpeed(float speed){
		inGameScreen.setRenderingSpeed(speed);
	}

	/**
	 * Sets the value of the remaining turn time to display.
	 * @param time
	 */
	public void setTurntimeRemaining(int time){
		turnTimer.setCurrentTime(time);
	}

	public void startTurnTimer(){
		turnTimer.startTimer();
	}

	public void stopTurnTimer(){
		turnTimer.stopTimer();
	}

	public void setAimIndicatorValues(float angle, float strength){
		aimInfo.setValues(angle,strength);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void setMaxStamina(int stamina){
		staminaBar.setMaxStamina(stamina);
	}

	public void updateCurrentStamina(int currentStamina){
		staminaBar.updateCurrentStamina(currentStamina);
	}

	public void toggleDebugOutlines() {
		this.debugVisible = !debugVisible;

		this.layoutTable.setDebug(debugVisible);
	}
}

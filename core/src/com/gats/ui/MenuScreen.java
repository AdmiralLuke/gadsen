package com.gats.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.ui.menu.Menu;

/**
 * Das Hauptmenü des Spiels
 */
public class MenuScreen implements Screen {
    private Viewport menuViewport;
    private Viewport backgroundViewport;
    private GADS gameInstance;
    private GADSAssetManager gadsAssetManager;
    private Stage mainMenu;
    private Menu menuBuilder;
    private Camera camera;
    private TextureAtlas atlas;
private TextureRegion background;
private SpriteBatch menuBatch;

    public MenuScreen(GADS gameInstance, GADSAssetManager gadsAssetManager) {

        atlas = gadsAssetManager.getAtlas();

        this.gameInstance = gameInstance;
        this.gadsAssetManager = gadsAssetManager;
        TextureRegion titleSprite = new TextureRegion(atlas.findRegion("ui/titleTileset"));

        this.background = atlas.findRegion("background/WeihnachtsBG");
        this.camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() * 1f / Gdx.graphics.getWidth()));
        //set the viewport, world with and height are currently the one of the title sprite, so the table is always on screen
        //the world sizes are roughly estimating the table size in title image width, no way of getting the size of the button table/it did not really work out
        menuViewport = new ExtendViewport(titleSprite.getRegionWidth()/3, titleSprite.getRegionWidth()+100, camera);
        backgroundViewport = new FillViewport(background.getRegionWidth(),background.getRegionHeight());
        mainMenu = new Stage(menuViewport);

        menuBatch = new SpriteBatch();
        //create a table, holds ui widgets like buttons and textfields

        setupMenuScreen(mainMenu,titleSprite);
    }

    public void startGame(RunConfiguration config){
       gameInstance.startGame(config);
    }

    @Override
    public void show() {
        //sets mainMenu Stage to handle the inputs
        Gdx.input.setInputProcessor(mainMenu);
    }

    /**
     * Creates a {@link Table}for the button Layout,
     * initiates the buttons and puts them into the table.
     *
     * @param menuStage is the Stage that is supposed to receive the buttons.
     */
    public void setupMenuScreen(Stage menuStage, TextureRegion title) {


        Skin skin = gadsAssetManager.getSkin();
        this.menuBuilder = new Menu(skin,title, Manager.getPossiblePlayers(),gameInstance.getGameModes(),this);

        menuStage.addActor(menuBuilder.buildMenuLayout(skin));
    }



    @Override
    public void render(float delta) {
        camera.update();

        backgroundViewport.apply(true);
        menuBatch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        menuBatch.begin();
        this.menuBatch.draw(background,0,0);

        menuBatch.end();
       menuViewport.apply(true);
        menuBatch.setProjectionMatrix(menuViewport.getCamera().combined);
        mainMenu.act(delta);
        mainMenu.draw();
    }

    @Override
    public void resize(int width, int height) {
           menuViewport.update(width, height, true);

           menuViewport.apply();
           backgroundViewport.update(width, height, true);

           //center the camera
           //camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
           backgroundViewport.apply();
           camera.update();

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
        mainMenu.dispose();
    }

}






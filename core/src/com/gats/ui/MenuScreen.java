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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Das Hauptmenü des Spiels
 */
public class MenuScreen implements Screen {
    private Viewport menuViewport;
    private GameSettings gameSettings;
    private GADS gameInstance;
    private GADSAssetManager gadsAssetManager;
    private Stage mainMenu;
    private Camera camera;
    private TextureAtlas atlas;
private TextureRegion background;
private SpriteBatch menuBatch;

    public MenuScreen(GADS gameInstance, GADSAssetManager gadsAssetManager) {

        atlas = gadsAssetManager.getAtlas();

        this.gameInstance = gameInstance;
        this.gadsAssetManager = gadsAssetManager;


        this.camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        menuViewport = new ExtendViewport(40 * 12, 40 * 12, camera);


        mainMenu = new Stage(menuViewport);
        gameSettings = gameInstance.gameSettings;

        menuBatch = new SpriteBatch();
        //create a table, holds ui widgets like buttons and textfields
        setupMenuScreen(mainMenu);
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
     * @param menu Stage that is supposed to receive the buttons.
     */
    public void setupMenuScreen(Stage menu) {

        Skin skin = new Skin(Gdx.files.internal("core/resources/ui/skin.json"));


        Table menuTable = gameSettings.buildMainLayoutTable(skin, new TextureRegion(atlas.findRegion("ui/cat_lowRes")), new TextureRegion(atlas.findRegion("ui/titleTileset")));
        menu.addActor(menuTable);
        menuTable.setDebug(false); // This is optional, but enables debug lines for tables.
        menuTable.setFillParent(true);
       this.background = atlas.findRegion("tile/GADSBG");
    }


    @Override
    public void render(float delta) {
        camera.update();
        menuBatch.begin();
        this.menuBatch.draw(background,0,0,background.getRegionWidth()*4,background.getRegionHeight()*4);

        menuBatch.end();
        mainMenu.act(delta);
        mainMenu.draw();
    }

    @Override
    public void resize(int width, int height) {
        menuViewport.update(width, height, true);

        //center the camera
        //camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
        menuViewport.apply();
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






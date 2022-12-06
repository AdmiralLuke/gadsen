package com.gats.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Das Hauptmenü des Spiels
 */
public class MenuScreen implements Screen {
    private Viewport menuViewport;
    private GADS gameInstance;
    private GADSAssetManager gadsAssetManager;
    private Stage mainMenu;
    private Camera camera;
    private Table table;
    private SpriteBatch batch;
    private Image title;
    private TextureAtlas atlas;
    private LinkedList<BotSelectionBox> currentBotSelectors = new LinkedList<>();


    public MenuScreen(GADS gameInstance, GADSAssetManager gadsAssetManager) {

        atlas = gadsAssetManager.getAtlas();

        this.gameInstance = gameInstance;
        this.gadsAssetManager = gadsAssetManager;


        this.camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        menuViewport = new ExtendViewport(40 * 12, 40 * 12, camera);


        mainMenu = new Stage(menuViewport);

        //create a table, holds ui widgets like buttons and textfields
        setupTable(mainMenu);
    }

    @Override
    public void show() {
        //sets mainMenu Stage to handle the inputs
        Gdx.input.setInputProcessor(mainMenu);
    }

    /**
     *  Creates a {@link Table}for the button Layout,
     *  initiates the buttons and puts them into the table.
     *
     *
     * @param menu Stage that is supposed to receive the buttons.
     */
    public void setupTable(Stage menu) {
    //ToDo Add slider for Character per team selection

        Skin skin = new Skin(Gdx.files.internal("core/resources/ui/skin.json"));
        table = new Table(skin);
        Table botTable = new Table(skin);
        // Label title = new Label("GADS",new Label.LabelStyle().font = new BitmapFont(Gdx.files.internal("build/res/texture/font/default.fnt")));

        //{**This block creates all the desired Buttons**

        //Creates the title Image to be usable in the table
        title = new Image(atlas.findRegion("ui/gadsTitleTemporary"));
        //Gamemodemenu to choose the desired Mode
        SelectBox<String> modesButton = new GameModeButton(skin);
        SelectBox<String> mapButton = new SelectBox<String>(skin);
        mapButton.setItems(gameInstance.getMaps());


        //maybe change to other type later
        SelectBox<String> botSelection = new SelectBox<String>(skin);
        botSelection.setItems(gameInstance.getBots());

        //create sliders for team and bot amount
        //botslider will become teamamount
        //teamslider will be player
        Slider teamSelectionSlider = new Slider(1, 8, 1, false, skin);

        //add a change Listener to the teamSelectionSlider so the amount of bot selections is dynamically updated
        teamSelectionSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                rebuildBotTable(teamSelectionSlider.getValue(),botTable,currentBotSelectors);
            }
        });
        Slider amountOfCharactersPerTeamSlider = new Slider(1, 8, 1, false, skin);
        //to dynamically adjust the amountOfCharactersPerTeamSlider Range, a change Listener is used to update the teamSelectionSlider everytime the TeamButton is changed
        /*amountOfCharactersPerTeamSlider.addListener(new ChangeListener() {*/
        /*    @Override*/
        /*    public void changed(ChangeEvent event, Actor actor) {*/
        /*        teamSelectionSlider.setRange(0,amountOfCharactersPerTeamSlider.getValue());*/
        /*        rebuildBotTable(teamSelectionSlider.getValue(),botTable,currentBotSelectors);*/
        /*    }*/
        /*});*/

        //create Labels for SliderButtons
        SliderLabel playerText = new SliderLabel("Anzahl der Teams: ", skin,teamSelectionSlider);
        SliderLabel teamText = new SliderLabel("Gadsen pro Team: ", skin, amountOfCharactersPerTeamSlider);

        Actor startButton = new ImageButton(new SpriteDrawable(gadsAssetManager.getAtlas().createSprite("ui/cat_lowRes")));
        TextButton textButtonExit = new TextButton("Exit", skin);

        //The change event for the button gets called, when it is pressed
        //Calls startGame() to initiate changing the screen;
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame(modesButton,mapButton, amountOfCharactersPerTeamSlider, teamSelectionSlider);
            }
        });

        textButtonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        //temporary christmas spaghetti
modesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //ToDo fix spaghetti and move the gamemode specific button changes to gameSettings class
                //needs to be changed to the correct map
                if(modesButton.getSelected().equals("Weihnachtsaufgabe")){
                   teamSelectionSlider.setRange(1,1);
                   amountOfCharactersPerTeamSlider.setRange(1,1);
                    mapButton.setItems("Weihnachtsmap");
                }
                else {
                        teamSelectionSlider.setRange(1,8);
                    amountOfCharactersPerTeamSlider.setRange(1,8);
                    mapButton.setItems(gameInstance.getMaps());
            }
            }
        });


        menu.addActor(table);
        table.setDebug(false); // This is optional, but enables debug lines for tables.
        //sizes the table to the stage, should only be used by root table
        //add the table to the menu stage
        table.setFillParent(true);
        //locates the table at the top of the screen
       buildTopOfTable(table,startButton,modesButton,mapButton);
       table.add(playerText);
       table.add(teamSelectionSlider).pad(10);
       table.row();
       table.add(teamText);
       table.add(amountOfCharactersPerTeamSlider).pad(10);
       table.row();
       //table.add(botSelectioin);
        table.row();
        table.add(botTable).colspan(4);
        table.row();
        table.add(textButtonExit).width(80).pad(10).colspan(4);

        rebuildBotTable(teamSelectionSlider.getValue(),botTable,currentBotSelectors);

    }

    @Override
    public void render(float delta) {
        camera.update();
        mainMenu.act(delta);
        table.act(delta);
        mainMenu.draw();

    }

    /**
     * Gets called when the Start button is pressed.
     * Uses {@link GameSettings} Settings to retrieve and set
     * the chosen settings.
     *
     * To change the screen it calls {@link GADS#setScreenIngame()}
     */
    public void startGame(SelectBox<String> mode, SelectBox<String> map, Slider team, Slider player) {
        gameInstance.gameSettings.evaluateButtonSettings(mode,map,team,player,currentBotSelectors);
        gameInstance.setScreenIngame();
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
        table.clear();
    }

    void setupButtons() {
    }

    /**
     * Recreates the botTable. Used for adjusting the amount of Botselections.
     *
     * Not very efficient solution, yey we only have to handle a small number of buttons.
     */
    void rebuildBotTable(float botAmount,Table botTable, LinkedList<BotSelectionBox> currentBots){
        int columns = 3;
       LinkedList<BotSelectionBox> newBotSelectors = new LinkedList<>();
       botTable.clear();
       ListIterator<BotSelectionBox> iterator = currentBots.listIterator();
        for(int i=0;i<botAmount;i++){
            //add a new row to the bottable to create another column
            if(i%columns==0&&i!=0){
                botTable.row();
            }
           if(iterator.hasNext()) {
                BotSelectionBox current = iterator.next();
                botTable.add(current).pad(5);
                newBotSelectors.add(current);
            }
            else {
                BotSelectionBox newBotSelection = new BotSelectionBox(table.getSkin());
                botTable.add(newBotSelection).pad(5);
                newBotSelectors.add(newBotSelection);
            }

        }
        currentBotSelectors = newBotSelectors;


    }
    void buildTopOfTable(Table table,Actor startButton,Actor modesButton, Actor mapButton){
        table.top().pad(10);
        table.add(title).colspan(4);
        table.row();
        table.add(startButton).colspan(4);
        table.row();
        table.add(modesButton).pad(10).width(200);
        table.add(mapButton).pad(10).width(200);
        table.row();
    }

    //Section for the buttons
    class GameModeButton extends SelectBox<String> {

        String[] modes = {
                "Normal",
                "UltraSuperGadsenFight",
                "Weihnachtsaufgabe"
        };

        public GameModeButton(Skin skin) {
            super(skin);
            setupButton();
        }

        /**
         * Add the modes
         */

        void setupButton() {
            this.setItems(modes);
        }

    }

   class SliderLabel extends Label {
        Slider sliderInstance;
        String name;

        public SliderLabel(String name, Skin skin, Slider slider) {
            super(name, skin);
            this.sliderInstance = slider;
            this.name = name;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            setText(name + (int) sliderInstance.getValue());

        }
    }
    class BotSelectionBox extends SelectBox<String>{

        public BotSelectionBox(Skin skin) {
            super(skin);
            this.setItems(gameInstance.getBots());
        }
    }
}


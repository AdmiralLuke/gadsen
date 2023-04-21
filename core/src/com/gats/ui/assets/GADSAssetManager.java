package com.gats.ui.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;
import com.gats.ui.assets.AssetContainer.MainMenuAssets;
import com.gats.ui.hud.InputHandler;
import com.gats.ui.hud.inventory.InventoryCell;

public class GADSAssetManager {
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public final String resourceDirectory = "";
    public final String atlas = resourceDirectory + "texture_atlas/TextureAtlas.atlas";

    public final String skin = resourceDirectory + "uiUtility/skin.json";
    public final String font = resourceDirectory + "uiUtility/lsans-15.fnt";
    public final String outlineShader = resourceDirectory + "shader/outline.frag";
    public final String lookupShader = resourceDirectory + "shader/lookup.frag";
    public final String lookupOutlineShader = resourceDirectory + "shader/lookupOutline.frag";

    private boolean finishedLoading = false;

    //path to background
    //public final String background = resourceDirectory + "background/GADSBG.png";
    private final AssetManager manager;

    public GADSAssetManager() {
        manager = new AssetManager();
        loadFiles();
    }


    //ToDo: Make private after migrating to animation branch
    public void loadFiles() {
        loadFont();
        loadTextures();
        loadSkin();
        loadShader();

        //ToDo: Implement Loading screen and remove the 2 following statements
        manager.finishLoading();
        moveToContainer();
    }

    /**
     * Lädt den Texturen Atlas mit Assets relevant für das Ingame
     * mithilfe des Assetmanagers.
     * <p>
     * Methode wird erst beendet, sobald der Atlas geladen ist.
     * Solange der Manager diesen noch nicht fertig geladen hat, blockiert die Methode
     * </p>
     */
    public void loadTextures() {
        manager.load(atlas, TextureAtlas.class);
    }

    public void loadSkin() {
        manager.load(skin, Skin.class);
    }

    public void loadFont() {
        manager.load(font, BitmapFont.class);

    }

    private void loadShader(){
        manager.load(outlineShader, ShaderProgram.class);
        manager.load(lookupShader, ShaderProgram.class);
        manager.load(lookupOutlineShader, ShaderProgram.class);
    }


    public void unloadAtlas() {
        manager.unload(atlas);
    }

    private void moveToContainer() {
        TextureAtlas atlas = manager.get(this.atlas, TextureAtlas.class);

        //Main Menu
        MainMenuAssets.background = atlas.findRegion("background/WeihnachtsBG");
        MainMenuAssets.titleSprite = atlas.findRegion("ui/titleTileset");
        MainMenuAssets.skin = manager.get(skin, Skin.class);

        //Ingame
        IngameAssets.destroyTileAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("tile/16x_tileDestroyAnimation"));

        IngameAssets.victoryDisplay = atlas.findRegion("ui/victoryTileset");
        IngameAssets.lossDisplay = atlas.findRegion("ui/loseTilesetTitle");

        IngameAssets.background = atlas.findRegion("background/WeihnachtsBG");

        IngameAssets.tileTextures = new TextureRegion[]{atlas.findRegion("tile/16x_box01"), atlas.findRegion("tile/16x_anchor01")};

        IngameAssets.aimingIndicatorSprite = atlas.findRegion("ui/aimIndicator");

        IngameAssets.gameCharacterAnimations = new AtlasAnimation[GameCharacterAnimationType.values().length];

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()] =
                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catIdle"), Animation.PlayMode.LOOP);

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_WALKING.ordinal()] =
                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catWalking"), Animation.PlayMode.LOOP);

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_FALLING.ordinal()] =
                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catFalling"), Animation.PlayMode.LOOP);

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_HIT.ordinal()] =
                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catHit"), Animation.PlayMode.LOOP);

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_DEATH.ordinal()] =
                new AtlasAnimation(1 / 10f, atlas.findRegions("cat/catDeath"), Animation.PlayMode.LOOP);

        IngameAssets.tombstoneAnimation = new AtlasAnimation(1 / 10f, atlas.findRegions("cat/tombstone"), Animation.PlayMode.NORMAL);


        IngameAssets.outlineShader = manager.get(outlineShader, ShaderProgram.class);

        IngameAssets.lookupShader = manager.get(lookupShader, ShaderProgram.class);

        IngameAssets.lookupOutlineShader = manager.get(lookupOutlineShader, ShaderProgram.class);

        IngameAssets.coolCatSkin = new AtlasAnimation(1 / 10f, atlas.findRegions("skin/coolCatSkin"), Animation.PlayMode.LOOP);

        IngameAssets.orangeCatSkin = new AtlasAnimation(1 / 10f, atlas.findRegions("skin/orangeCatSkin"), Animation.PlayMode.LOOP);

        IngameAssets.yinYangSkin = new AtlasAnimation(1 / 10f, atlas.findRegions("skin/yinYangSkin"), Animation.PlayMode.LOOP);

        IngameAssets.mioSkin = new AtlasAnimation(1 / 10f, atlas.findRegions("skin/mioSkin"), Animation.PlayMode.LOOP);

        IngameAssets.coolCat = new AtlasAnimation(1f, atlas.findRegions("cat/coolCat"), Animation.PlayMode.LOOP);

        IngameAssets.Cookie = new AtlasAnimation(1/8f, atlas.findRegions("projectile/cookieTumblingCroppedR"), Animation.PlayMode.LOOP);

        IngameAssets.SugarCane = new AtlasAnimation(1/8f, atlas.findRegions("projectile/sugarcaneProjectileFront"), Animation.PlayMode.LOOP);

        IngameAssets.WaterPistol = new AtlasAnimation(1/8f, atlas.findRegions("weapon/watergun"), Animation.PlayMode.LOOP);

        IngameAssets.cookieIcon = atlas.findRegion("ui/CookieSprite");
        IngameAssets.sugarCaneIcon = atlas.findRegion("ui/SugarCaneSprite");
        IngameAssets.inventoryCell = atlas.findRegion("ui/inventoryCell");

        //IngameAssets.weaponIcons.put(WeaponType.COOKIE, IngameAssets.cookieIcon);
      //  IngameAssets.weaponIcons.put(WeaponType.SUGAR_CANE, IngameAssets.sugarCaneIcon);

        IngameAssets.weaponIcons.put(WeaponType.NOT_SELECTED,null);
        IngameAssets.fastForwardButton = atlas.findRegion("ui/fastForwardButton");
        IngameAssets.fastForwardButtonPressed = atlas.findRegion("ui/fastForwardButtonPressed");
        IngameAssets.fastForwardButtonChecked = atlas.findRegion("ui/fastForwardButtonChecked");
        IngameAssets.turnChange = atlas.findRegion("ui/turnChange");
        IngameAssets.turnTimer = atlas.findRegion("ui/clockSprite");


        finishedLoading = true;
    }

    public float update() {
        if (manager.update() && !finishedLoading) {
            moveToContainer();
        }
        return manager.getProgress();
    }


}

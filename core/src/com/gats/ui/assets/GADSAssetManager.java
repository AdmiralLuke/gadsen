package com.gats.ui.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;
import com.gats.ui.assets.AssetContainer.MainMenuAssets;

public class GADSAssetManager {
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public final String resourceDirectory = "";
    public final String atlas = resourceDirectory + "texture_atlas/TextureAtlas.atlas";

    public final String skin = resourceDirectory + "ui/skin.json";
    public final String font = resourceDirectory + "ui/lsans-15.fnt";

    public final String outlineShader = resourceDirectory + "shader/outline.frag";

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
        IngameAssets.destroyTileAnimation = new Animation<TextureRegion>(0.5f, atlas.findRegions("tile/16x_tileDestroyAnimation"));

        IngameAssets.victoryDisplay = atlas.findRegion("ui/victoryTileset");
        IngameAssets.lossDisplay = atlas.findRegion("ui/loseTilesetTitle");

        IngameAssets.background = atlas.findRegion("background/WeihnachtsBG");

        IngameAssets.tileTextures = new TextureRegion[]{atlas.findRegion("tile/16x_box01"), atlas.findRegion("tile/16x_anchor01")};

        IngameAssets.aimingIndicatorSprite = atlas.findRegion("hud/aimIndicator");

        IngameAssets.gameCharacterAnimations = new Animation[GameCharacterAnimationType.values().length];

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_IDLE.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catIdleL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_WALKING.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catWalkingL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_FALLING.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catFallingL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_COOKIE.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catCookieIdleL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_SUGAR_CANE.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catSugarCaneIdleL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_HIT.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catHitL"));

        IngameAssets.gameCharacterAnimations[GameCharacterAnimationType.ANIMATION_TYPE_DEATH.ordinal()] =
                new Animation<>(1 / 10f, atlas.findRegions("cat/catDeathL"));
        for (Animation<TextureRegion> anim : IngameAssets.gameCharacterAnimations
        ) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }

        IngameAssets.outlineShader = manager.get(outlineShader, ShaderProgram.class);

        IngameAssets.Cookie = atlas.findRegions("projectile/cookieTumblingR");

        IngameAssets.SugarCane = atlas.findRegions("projectile/sugarcaneProjectileFront");

        finishedLoading = true;
    }

    public float update() {
        if (manager.update() && !finishedLoading) {
            moveToContainer();
        }
        return manager.getProgress();
    }


}

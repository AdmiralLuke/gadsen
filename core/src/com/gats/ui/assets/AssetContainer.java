package com.gats.ui.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Contains Loaded Instances of Assets
 */
public class AssetContainer {

    /**
     * Loading Screen Assets
     *
     * Will be Loaded immediately before other assets are finished
     */
    public static class LoadingScreenAssets{

    }

    /**
     * Main Menu Assets
     */
    public static class MainMenuAssets{

        public static TextureRegion titleSprite;
        public static TextureRegion background;
        public static Skin skin;
    }



    /**
     * Ingame Assets
     */
    public static class IngameAssets{

        public static Animation<TextureRegion> destroyTileAnimation;

        public static TextureRegion victoryDisplay;
        public static TextureRegion lossDisplay;


        public static TextureRegion background;

        public static TextureRegion[] tileTextures;

        public static TextureRegion aimingIndicatorSprite;

        public static Animation<TextureRegion>[] gameCharacterAnimations;


        public static ShaderProgram outlineShader;

        public static ShaderProgram lookupShader;

        //Projectiles
        public static Array<TextureAtlas.AtlasRegion> Cookie;
        public static Array<TextureAtlas.AtlasRegion> SugarCane;
        public static TextureAtlas.AtlasRegion coolCatSkin;

        public enum GameCharacterAnimationType {
            ANIMATION_TYPE_IDLE,
            ANIMATION_TYPE_WALKING,
            ANIMATION_TYPE_FALLING,
            ANIMATION_TYPE_COOKIE,
            ANIMATION_TYPE_SUGAR_CANE,
            ANIMATION_TYPE_HIT,
            ANIMATION_TYPE_DEATH
        }
    }

}

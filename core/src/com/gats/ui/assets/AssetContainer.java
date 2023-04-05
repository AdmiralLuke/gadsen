package com.gats.ui.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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

        public static TextureRegion pixel;

        static {
            Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            map.setColor(Color.WHITE);
            map.drawPixel(0, 0);
            pixel = new TextureRegion(new Texture(map));
        }

        public static TextureRegion cross_marker;

        static {
            Pixmap map = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
            map.setColor(Color.WHITE);
            map.drawLine(1, 0, 1, 2);
            map.drawLine(0, 1, 2, 1);
            cross_marker = new TextureRegion(new Texture(map));
        }

        public static ShaderProgram outlineShader;

        public static ShaderProgram lookupShader;

        public static ShaderProgram lookupOutlineShader;

        //Projectiles
        public static Animation<TextureRegion> Cookie;
        public static Animation<TextureRegion> SugarCane;

        public static Animation<TextureRegion> coolCatSkin;
        public static Animation<TextureRegion> orangeCatSkin;
        public static Animation<TextureRegion> yinYangSkin;
        public static Animation<TextureRegion> mioSkin;


        public static Animation<TextureRegion> coolCat;

        public static Animation<TextureRegion> tombstoneAnimation;

        public enum GameCharacterAnimationType {
            ANIMATION_TYPE_IDLE,
            ANIMATION_TYPE_WALKING,
            ANIMATION_TYPE_FALLING,
            ANIMATION_TYPE_HIT,
            ANIMATION_TYPE_DEATH
        }
    }

}

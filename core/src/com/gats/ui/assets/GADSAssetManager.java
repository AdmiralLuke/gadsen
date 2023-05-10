package com.gats.ui.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gats.animation.entity.SpriteEntity;
import com.gats.simulation.WeaponType;
import com.gats.simulation.action.ProjectileAction;
import com.gats.ui.assets.AssetContainer.IngameAssets;
import com.gats.ui.assets.AssetContainer.IngameAssets.GameCharacterAnimationType;
import com.gats.ui.assets.AssetContainer.MainMenuAssets;
import com.gats.ui.hud.InputHandler;
import com.gats.ui.hud.inventory.InventoryCell;

public class GADSAssetManager {
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public static final String resourceDirectory = "";
    public static final String atlas = resourceDirectory + "texture_atlas/TextureAtlas.atlas";

    public final String skin = resourceDirectory + "uiUtility/skin.json";
    public final String font = resourceDirectory + "uiUtility/lsans-15.fnt";

    public static final String particleGroup = "particle/";
    public static final String slimeParticle = "particle/slimeParticle.p";
    public static final String walkParticle = "particle/slimeParticle.p";

    public static final String damageParticle = "particle/damageParticle.p";

    public static final String explosionParticle = "particle/explosionParticle.p";


    //ToDo load different effect (not splash)
    public static final String waterParticle = "particle/waterSplashParticle.p";

    public static final String outlineShader = resourceDirectory + "shader/outline.frag";
    public static final String lookupShader = resourceDirectory + "shader/lookup.frag";
    public static final String lookupOutlineShader = resourceDirectory + "shader/lookupOutline.frag";

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
        loadParticles();

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

    private void loadShader() {
        manager.load(outlineShader, ShaderProgram.class);
        manager.load(lookupShader, ShaderProgram.class);
        manager.load(lookupOutlineShader, ShaderProgram.class);
    }

    private void loadParticles() {
        ParticleEffectLoader.ParticleEffectParameter particleEffectParameter = new ParticleEffectLoader.ParticleEffectParameter();
        particleEffectParameter.atlasFile = atlas;
        particleEffectParameter.atlasPrefix = particleGroup;

        manager.load(slimeParticle, ParticleEffect.class, particleEffectParameter);
        manager.load(walkParticle, ParticleEffect.class, particleEffectParameter);
        manager.load(damageParticle, ParticleEffect.class, particleEffectParameter);
        manager.load(explosionParticle, ParticleEffect.class, particleEffectParameter);
        manager.load(waterParticle, ParticleEffect.class, particleEffectParameter);
    }


    public void unloadAtlas() {
        manager.unload(atlas);
    }

    private void moveToContainer() {
        TextureAtlas atlas = manager.get(this.atlas, TextureAtlas.class);

        //Main Menu
        MainMenuAssets.background = atlas.findRegion("background/mainTitleBackground");
        MainMenuAssets.titleSprite = atlas.findRegion("ui/titleTileset");
        MainMenuAssets.skin = manager.get(skin, Skin.class);

        //Ingame
        IngameAssets.destroyTileAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("tile/16x_tileDestroyAnimation"));

        IngameAssets.victoryDisplay = atlas.findRegion("ui/victoryTileset");
        IngameAssets.lossDisplay = atlas.findRegion("ui/loseTilesetTitle");
        IngameAssets.drawDisplay = atlas.findRegion("ui/drawTileset");

        IngameAssets.background = atlas.findRegion("background/WeihnachtsBG");

        IngameAssets.tileTextures = new TextureRegion[]{atlas.findRegion("tile/16x_box01"), atlas.findRegion("tile/16x_anchor01")};

        IngameAssets.aimingIndicatorSprite = atlas.findRegion("ui/aimIndicatorNinepatchColorless");

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

        IngameAssets.Cookie = new AtlasAnimation(1 / 8f, atlas.findRegions("projectile/cookieTumblingCroppedR"), Animation.PlayMode.LOOP);

        IngameAssets.SugarCane = new AtlasAnimation(1 / 8f, atlas.findRegions("projectile/sugarcaneProjectileFront"), Animation.PlayMode.LOOP);

        IngameAssets.slimeParticle = new ParticleEffectPool(manager.get(slimeParticle, ParticleEffect.class), 1, 10);

        IngameAssets.walkParticle = new ParticleEffectPool(manager.get(walkParticle, ParticleEffect.class), 1, 10);//ToDo replace

        IngameAssets.damageParticle = new ParticleEffectPool(manager.get(damageParticle, ParticleEffect.class), 1, 10);

        IngameAssets.explosionParticle = new ParticleEffectPool(manager.get(explosionParticle, ParticleEffect.class), 1, 10);

        IngameAssets.waterParticle = new ParticleEffectPool(manager.get(waterParticle, ParticleEffect.class), 1, 10);

        IngameAssets.splashParticle = new ParticleEffectPool(manager.get(waterParticle, ParticleEffect.class), 1, 10);

        IngameAssets.WaterPistol = new AtlasAnimation(1/8f, atlas.findRegions("weapon/watergun"), Animation.PlayMode.LOOP);

        IngameAssets.Wool = new AtlasAnimation(1/8f, atlas.findRegions("weapon/ballOfWoolWeapon"), Animation.PlayMode.LOOP);

        IngameAssets.Grenade = new AtlasAnimation(1/8f, atlas.findRegions("weapon/grenadeReddish"), Animation.PlayMode.LOOP);
        IngameAssets.Miojlnir = new AtlasAnimation(1/8f, atlas.findRegions("weapon/miosHammer"), Animation.PlayMode.LOOP);

        IngameAssets.BaseballBat = new AtlasAnimation(1/8f, atlas.findRegions("weapon/baseballBat"), Animation.PlayMode.LOOP);

        IngameAssets.BaseballBatAttack = new AtlasAnimation(1/8f, atlas.findRegions("weapon/baseballBatAttack"), Animation.PlayMode.NORMAL);

        IngameAssets.WaterBomb = new AtlasAnimation(1/8f, atlas.findRegions("weapon/waterbombProjectile"), Animation.PlayMode.NORMAL);

        IngameAssets.cookieIcon = atlas.findRegion("ui/CookieSprite");
        IngameAssets.sugarCaneIcon = atlas.findRegion("ui/SugarCaneSprite");
        IngameAssets.inventoryCell = atlas.findRegion("ui/inventoryCell");

        //IngameAssets.weaponIcons.put(WeaponType.COOKIE, IngameAssets.cookieIcon);
      //  IngameAssets.weaponIcons.put(WeaponType.SUGAR_CANE, IngameAssets.sugarCaneIcon);

        IngameAssets.weaponIcons.put(WeaponType.NOT_SELECTED,null);
        IngameAssets.weaponIcons.put(WeaponType.WATER_PISTOL, atlas.findRegion("ui/watergunIcon"));
        IngameAssets.weaponIcons.put(WeaponType.WOOL, atlas.findRegion("ui/ballOfWoolIcon"));
        IngameAssets.weaponIcons.put(WeaponType.GRENADE, atlas.findRegion("ui/grenadeReddishIcon"));
        IngameAssets.weaponIcons.put(WeaponType.MIOJLNIR, atlas.findRegion("ui/miosHammerIcon"));
        IngameAssets.weaponIcons.put(WeaponType.CLOSE_COMBAT, atlas.findRegion("ui/baseballBatIcon"));
        IngameAssets.weaponIcons.put(WeaponType.WATERBOMB, atlas.findRegion("ui/waterbombSprite"));

        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.WATER, new AtlasAnimation(1/8f, atlas.findRegions("projectile/watergunProjectile"), Animation.PlayMode.LOOP));
        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.WOOL, new AtlasAnimation(1/8f, atlas.findRegions("weapon/ballOfWoolWeapon"), Animation.PlayMode.LOOP));
        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.GRENADE, new AtlasAnimation(1/8f, atlas.findRegions("projectile/grenadeProjectile"), Animation.PlayMode.LOOP));
        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.MIOJLNIR, new AtlasAnimation(1/8f, atlas.findRegions("weapon/miosHammer"), Animation.PlayMode.LOOP));
        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.CLOSE_COMB, new AtlasAnimation(1/8f, atlas.findRegions("projectile/baseballBatHit"), Animation.PlayMode.NORMAL));
        IngameAssets.projectiles.put(ProjectileAction.ProjectileType.WATERBOMB, new AtlasAnimation(1/8f, atlas.findRegions("weapon/waterbombProjectile"), Animation.PlayMode.LOOP));

        IngameAssets.fastForwardButton = atlas.findRegion("ui/fastForwardButton");
        IngameAssets.fastForwardButtonPressed = atlas.findRegion("ui/fastForwardButtonPressed");
        IngameAssets.fastForwardButtonChecked = atlas.findRegion("ui/fastForwardButtonChecked");
        IngameAssets.turnChange = atlas.findRegion("ui/turnChange");
        IngameAssets.turnTimer = atlas.findRegion("ui/clockSprite");


        createCircleTexture();
        createHealthHealthbarAssets(atlas);

        //create aim indicator circle

        finishedLoading = true;
    }

private void createHealthHealthbarAssets(TextureAtlas atlas) {

        //hb background

        TextureRegion background = atlas.findRegion("ui/healthbarBackground");
        IngameAssets.healthBarBackground = new NinePatchDrawable(new NinePatch(background,1,1,1,1));


        ProgressBar.ProgressBarStyle progStyle = new ProgressBar.ProgressBarStyle();
        //background of the healtbar

        progStyle.background = new NinePatchDrawable(IngameAssets.healthBarBackground);
        //create pixmap for the color representation
        Pixmap pixmap = new Pixmap(1,2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        TextureRegionDrawable healthBarColor = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        //area representing health
        progStyle.knobBefore = new TextureRegionDrawable(healthBarColor);

        //knob of 0 width,  "points" to current health
        pixmap = new Pixmap(0,2,Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        progStyle.knob = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();



        IngameAssets.healthbarStyle = progStyle;


    }


    public void createCircleTexture(){
        Vector2 maxSize = new Vector2(64,7);
        float circleOpacity = 0.5f;

        //todo find betterway to load circle with size parameters
        //could not be done bcause of threading context inside of aim indicator -> graphic operations need to be done on graphic thread
        // -> could theoretically be done, but this is easier
        //only problem being hardcoded size

        final TextureRegion[] circleTexture = new TextureRegion[1];
        //get max size of the aim indicator
        int circleSize = (int) maxSize.x;
        //create a Pixmap for drawing the circle texture
        Pixmap circle = new Pixmap(2 * circleSize + 1, 2 * circleSize + 1, Pixmap.Format.RGBA8888);
        //circle Color
        Color color = new Color(Color.WHITE);
        //circle alpha
        color.a = circleOpacity;
        circle.setColor(color);
        //draw circle at
        circle.drawCircle(circleSize, circleSize, circleSize);

        IngameAssets.aimCircle =
        new TextureRegion(new Texture(circle));


    }

    public float update() {
        if (manager.update() && !finishedLoading) {
            moveToContainer();
        }
        return manager.getProgress();
    }


}

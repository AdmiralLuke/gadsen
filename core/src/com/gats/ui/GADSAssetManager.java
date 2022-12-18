package com.gats.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sun.media.sound.RIFFInvalidDataException;

import java.util.HashMap;
import java.util.Map;

public class GADSAssetManager{
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public final String resourceDirectory = "";
    public final String inGameAtlas = resourceDirectory+ "texture_atlas/TextureAtlas.atlas";

    public final String skin = resourceDirectory + "ui/skin.json";
public final String font = resourceDirectory + "ui/lsans-15.fnt";
    //path to menu assets
    public final String menuAtlas = "";


    Map<String, Boolean> isResLoadedMap = new HashMap<String, Boolean>(){
    };
   public void fillMap(){
       isResLoadedMap.put(inGameAtlas,false);
       isResLoadedMap.put(skin,false);
       isResLoadedMap.put(font,false);
   }
    //path to background
    //public final String background = resourceDirectory + "background/GADSBG.png";
    public AssetManager manager;

    public GADSAssetManager(){
        manager = new AssetManager();
        fillMap();
    }



    /** Lädt den Texturen Atlas mit Assets relevant für das Ingame
     * mithilfe des Assetmanagers.
     * <p>
     *     Methode wird erst beendet, sobald der Atlas geladen ist.
     *     Solange der Manager diesen noch nicht fertig geladen hat, blockiert die Methode
     * </p>
     */
    public void loadTextures() {
        manager.load(inGameAtlas, TextureAtlas.class);
        manager.finishLoading();
        isResLoadedMap.put(inGameAtlas,true);
    }

    public void loadSkin(){
        manager.load(skin, Skin.class);
        manager.finishLoading();
        isResLoadedMap.put(skin,true);
    }

    public void loadFont(){
        manager.load(font,BitmapFont.class);
        manager.finishLoading();
        isResLoadedMap.put(font,true);

    }
    public void loadFiles(){
        loadFont();
        loadTextures();
        loadSkin();
    }


    public void unloadAtlas(){
        manager.unload(inGameAtlas);
        isResLoadedMap.put(inGameAtlas,false);
    }
    public TextureAtlas getAtlas(){

        if (isResLoadedMap.get(inGameAtlas)) {
            return manager.get(inGameAtlas);
        }
        else {
            throw new RuntimeException("InGame Atlas needs to be loaded, before calling manager.get()");
        }
    }

    public BitmapFont getFont(){
        if (isResLoadedMap.get(font)) {
            return manager.get(font);
        }
        else{
            throw new RuntimeException("BitmapFont needs to be loaded, before calling manager.get()");
        }
    }

    public Skin getSkin(){
        if (isResLoadedMap.get(skin)) {
            return manager.get(skin);
        }
        else{
            throw new RuntimeException("Skin needs to be loaded, before calling manager.get()");
        }
    }

    public boolean update(){
        return manager.update();
    }


}

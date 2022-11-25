package com.gats.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.w3c.dom.Text;

public class GADSAssetManager{
    //dedicatod to loading and mangaing assets used in the application

    //Pfad an dem sich der Textureatlas mit Assets relevant für das Spiel
    public final String resourceDirectory = "core/resources/";
    public final String inGameAtlas = resourceDirectory+ "texture_atlas/TextureAtlas.atlas";
    //path to menu assets
    public final String menuAtlas = "";
    //path to background
    //public final String background = resourceDirectory + "background/GADSBG.png";
    public AssetManager manager;
    private boolean inGameLoaded;

    public GADSAssetManager(){
        manager = new AssetManager();
        inGameLoaded = false;
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
        inGameLoaded = true;
    }

    public void unloadAtlas(){
        manager.unload(inGameAtlas);
        inGameLoaded = false;
    }
    public TextureAtlas getAtlas(){

        if (inGameLoaded) {
            return manager.get(inGameAtlas);
        }
        else {
            throw new RuntimeException("InGame Atlas needs to be loaded, before calling manager.get()");
        }
    }

    public boolean update(){
        return manager.update();
    }

}

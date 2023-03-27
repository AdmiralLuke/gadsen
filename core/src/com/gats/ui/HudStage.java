package com.gats.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gats.animation.AnimatorCamera;
import com.gats.manager.HumanPlayer;
import com.gats.simulation.Simulation;
import com.gats.ui.assets.GADSAssetManager;
import com.gats.ui.hud.GadsenInputProcessor;
import com.gats.ui.hud.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class HudStage extends Stage {

   private
    SpriteBatch batch;

    InputHandler inputHandler;

    public HudStage(Viewport hudViewport, InGameScreen ingameScreen) {
        super(hudViewport);
        batch = new SpriteBatch();
        inputHandler = new InputHandler(ingameScreen);
    }
    public GadsenInputProcessor getInputHandler() {
      return inputHandler;
    }

    @Override
    public void draw() {
        super.draw();
    }


    protected void tick(float delta) {
        inputHandler.tick(delta);
    }


}

package com.gats.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gats.animation.entity.AnimatedEntity;
import com.gats.animation.entity.GameCharacterHudElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Repräsentiert eine Spielfigur auf der Karte
 */
public class GameCharacter extends AnimatedEntity {
    private AimIndicator aimingIndicator;
    private Color teamColor;
    private static ShaderProgram outlineShader;
    {
        StringBuilder builder = new StringBuilder();
        try(InputStream stream = GameCharacter.class.getClassLoader().getResourceAsStream("shader/vertex.glsl")){
            int c = 0;
            if (stream == null) throw new RuntimeException("Could not read outline vertex shader");
            while ((c = stream.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String vertexShader = builder.toString();

        builder = new StringBuilder();
        try(InputStream stream = GameCharacter.class.getClassLoader().getResourceAsStream("shader/fragment.glsl")){
            int c = 0;
            if (stream == null) throw new RuntimeException("Could not read outline fragment shader");
            while ((c = stream.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fragmentShader = builder.toString();
        outlineShader = new ShaderProgram(vertexShader, fragmentShader);
    }

    public GameCharacter(Animation<TextureRegion> animation, Color teamColor) {
        super(animation);
        this.teamColor = teamColor;
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public GameCharacter(Animation<TextureRegion> animation, AimIndicator aimIndicator){
        super(animation);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.aimingIndicator = aimIndicator;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        batch.flush();
        batch.setShader(outlineShader);
        outlineShader.setUniformf("outline_color", teamColor);
        super.draw(batch, deltaTime, parentAlpha);
        batch.flush();
        batch.setShader(null);
        if(aimingIndicator!=null){
            aimingIndicator.draw(batch,deltaTime,parentAlpha);
        }
    }

    public AimIndicator getAimingIndicator(){
        return this.aimingIndicator;
    }

    public void setAimingIndicator(AimIndicator aimIndicator) {
        this.aimingIndicator = aimIndicator;
    }
}

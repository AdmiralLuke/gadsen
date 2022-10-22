package com.gats.animation;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Animations-relevantes Objekt.
 * Dient der Strukturierung der animierten Welt und der einfachen Positionierung geschachtelter Objekte
 */
public class Entity {
    private Vector2 pos;
    private Vector2 relPos;

    /**
     * Fordert das Entity auf alle nötigen Operationen für das Rendering des aktuellen Frames auszuführen.
     * Das Standardverhalten besteht darin nichts zu tun und dient als Interface zur Implementierung in Sub-Klassen.
     * @param batch
     */

    void draw(Batch batch){}

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getRelPos() {
        return relPos;
    }
}

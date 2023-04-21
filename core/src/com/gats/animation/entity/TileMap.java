package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.gats.simulation.GameState;
import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

public class TileMap extends Entity {

    public static final int TILE_TYPE_NONE = -1;

    private static BodyDef bodyDef = new BodyDef();

    private static final PolygonShape tileShape = new PolygonShape();

    static {
        bodyDef.position.set(new Vector2(0, 10));
    }

    private final Body body;

    private final TextureRegion[] tileTextures;
    private final int[][] tiles;

    private final Fixture[][] colliders;
    private final int sizeX;
    private final int sizeY;
    private final World tilesWorld;

    private int tileSize = 16;

    public TileMap(TextureRegion[] tileTextures, GameState state, World tilesWorld) {
        this.tileTextures = tileTextures;
        sizeX = state.getBoardSizeX();
        sizeY = state.getBoardSizeY();
        this.tilesWorld = tilesWorld;
        body = tilesWorld.createBody(bodyDef);
        tileShape.setAsBox(sizeX, sizeY);
        if (tileTextures.length > 0) tileSize = tileTextures[0].getRegionWidth();
        this.tiles = new int[sizeX][sizeY];
        this.colliders = new Fixture[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                Tile tile = state.getTile(i, j);
                if (tile == null) tiles[i][j] = TILE_TYPE_NONE;
                else {
                    tiles[i][j] = tile.getType();
                    colliders[i][j] = body.createFixture(tileShape, 0);
                }
            }
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        Vector2 pos = new Vector2(0, 0);
        //Vector2 pos = new Vector2(sizeX/2f * tileSize, sizeY/2f * tileSize).add(getPos());
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                int type = tiles[i][j];
                if (type != TILE_TYPE_NONE) {
                    batch.draw(tileTextures[type], pos.x + i * tileSize, pos.y + j * tileSize);
                }
            }
    }


    public int getTile(IntVector2 pos) {
        return getTile(pos.x, pos.y);
    }

    public int getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTile(IntVector2 pos, int value) {
        setTile(pos.x, pos.y, value);
    }

    public void setTile(int x, int y, int value) {
        if (value == TILE_TYPE_NONE){
            if (tiles[x][y] != TILE_TYPE_NONE){
                body.destroyFixture(colliders[x][y]);
            }
        }else {
            if (tiles[x][y] == TILE_TYPE_NONE){
                colliders[x][y] = body.createFixture(tileShape, 0);
            }
        }
        tiles[x][y] = value;
    }

    public int getTileSize() {
        return tileSize;
    }
}

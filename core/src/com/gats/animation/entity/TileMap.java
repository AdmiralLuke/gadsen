package com.gats.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameState;
import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;
import com.gats.simulation.action.TileSummonAction;
import com.gats.ui.assets.AssetContainer;

import java.lang.reflect.InvocationTargetException;

public class TileMap extends Entity{

    public static final int TYLE_TYPE_NONE = -1;
    private int[][] tiles;
    private int sizeX;
    private int sizeY;

    private int tileSize = 12;

    public TileMap(GameState state) {
        sizeX = state.getBoardSizeX();
        sizeY = state.getBoardSizeY();
        if(AssetContainer.IngameAssets.tileTextures.length > 0) tileSize = AssetContainer.IngameAssets.tileTextures[0].getRegionWidth();
        this.tiles = new int[sizeX][sizeY];
        for (int i = 0; i<sizeX; i++)
            for (int j = 0; j<sizeY; j++){
                Tile tile = state.getTile(i,j);
                tiles[i][j] = getTileID(tile);
            }
    }

    private int getTileID(Tile tile){
        if (tile == null) return TYLE_TYPE_NONE;
        Tile.TileType type = tile.getTileType();
        if (type == Tile.TileType.HEALTH_BOX) return 5;
        if (type == Tile.TileType.STANDARD) return tile.isAnchor()?0:1;
        if (type == Tile.TileType.WEAPON_BOX) return tile.isAnchor()?3:4;
        return TYLE_TYPE_NONE;
    }

    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
       Vector2 pos = new Vector2(0,0);
        //Vector2 pos = new Vector2(sizeX/2f * tileSize, sizeY/2f * tileSize).add(getPos());
        for (int i = 0; i<sizeX; i++)
            for (int j = 0; j<sizeY; j++){
                int type = tiles[i][j];
                if (type != TYLE_TYPE_NONE){
                    batch.draw(AssetContainer.IngameAssets.tileTextures[type],pos.x + i * tileSize, pos.y + j * tileSize);
                }
            }
    }


    public int getTile(IntVector2 pos){
        return getTile(pos.x, pos.y);
    }
    public int getTile(int x, int y){
        return tiles[x][y];
    }

    public void setTile(IntVector2 pos, int value){
        setTile(pos.x, pos.y, value);
    }
    public void setTile(int x, int y, int value){
        tiles[x][y] = value;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}

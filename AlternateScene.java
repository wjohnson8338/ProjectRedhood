/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author wjohn
 */
public class AlternateScene {
    
    private int soulBoxColor;
    public static final int spacing = 25;
    Array<Sprite> tileArray;
    
    public AlternateScene(float viewportWidth, float viewportHeight) {
        // Instantiate Tile Array
        this.tileArray = new Array<Sprite>();
        // Use these two variables to keep track of where to place tiles. 
        float currentTilemapWidth = viewportWidth;
        float currentTilemapHeight = viewportHeight-spacing; 
        // From the tiles I've been using, I know the dimensions are 32f.
        float tileDimensions = 32; 
        // Save our Tile Textures 
        Texture caveTileTexture = new Texture(Gdx.files.internal("caveTileNormal.png"));
        Texture caveTileCrackedTexture = new Texture(Gdx.files.internal("caveTileCrackedNormal.png"));
        Texture caveTileRubbleTexture = new Texture(Gdx.files.internal("caveTileRubbleNormal.png"));
        Texture caveWallTexture = new Texture(Gdx.files.internal("caveTileWall.png"));
        // Begin adding Sprites to the Array 
        
        // First, add top walls. 
        currentTilemapHeight -= 48;
        while (currentTilemapWidth > -32) { // We set it to -32, so we can make sure we cover the entire screen 
            Sprite tmpWallSprite = new Sprite(caveWallTexture);
            tmpWallSprite.setPosition(currentTilemapWidth, currentTilemapHeight);
            tileArray.add(tmpWallSprite);
            currentTilemapWidth -= tileDimensions;
        }
        // We finished adding walls, decrease height, and reset width, begin adding normal floor tiles 
        int randomChoice = MathUtils.random(1, 10);
        currentTilemapWidth = 0;
        currentTilemapHeight -= 32;
        // Until all rows are finished, keep generating
        while (currentTilemapHeight > spacing) {
            // With Random Choice, choose which tile to place down.
            if (randomChoice < 2) {
                Sprite tmpSprite = new Sprite(caveTileCrackedTexture);
                tmpSprite.setPosition(currentTilemapWidth, currentTilemapHeight);
                tileArray.add(tmpSprite); 
            }
            else if (randomChoice < 3) {
                Sprite tmpSprite = new Sprite(caveTileRubbleTexture);
                tmpSprite.setPosition(currentTilemapWidth, currentTilemapHeight);
                tileArray.add(tmpSprite); 
            }
            else {
                Sprite tmpSprite = new Sprite(caveTileTexture);
                tmpSprite.setPosition(currentTilemapWidth, currentTilemapHeight);
                tileArray.add(tmpSprite); 
            }
            // Finsihed Placing Tile, Add width to total, and reset generate new random number 
            currentTilemapWidth += tileDimensions; 
            randomChoice = MathUtils.random(1, 10);
            // If we finished this row, move to the next row 
            if (currentTilemapWidth > viewportWidth) {
                currentTilemapWidth = 0;
                currentTilemapHeight -= 32;
            }
            // no Soulbox color applied.
            this.soulBoxColor = -1;
        }
    }
    
    public void update(Vector2 altPlayerLocation) {
        for (int i = 0; i < tileArray.size; i++) {
            Sprite tmpSprite = tileArray.get(i);
            float tileDistFromPlayer = AlternatePlayer.getDistance(altPlayerLocation, new Vector2(tmpSprite.getX(), tmpSprite.getY()));
            if (tileDistFromPlayer > AlternatePlayer.distanceToLightTilesFade) {
                tmpSprite.setColor(0.2f, 0.2f, 0.2f, 1);
            }
            else if (tileDistFromPlayer < AlternatePlayer.distanceToLightTilesFade && tileDistFromPlayer > AlternatePlayer.distanceToLightTiles) {
                tmpSprite.setColor(0.7f, 0.7f, 0.7f, 1);
            }
            else {
                tmpSprite.setColor(1, 1, 1, 1);
            }
            // If the scene is in soulbox mode, ignore everything only change coor 
            switch(this.soulBoxColor) {
                case 0:
                    tmpSprite.setColor(0.2f, 0.2f, 10f, 1);
                    break;
                case 1:
                    tmpSprite.setColor(1.0f, 0.75f, 0.79f, 1);
                    break;
                case 2:
                    tmpSprite.setColor(10.0f, 0.2f, 0.2f, 1);
                    break;
                case 3:
                    tmpSprite.setColor(0.2f, 10f, 0.2f, 1);
                    break;
                case -1:
                    break;
            }
        }
    }
    
    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < this.tileArray.size; i++) {
            this.tileArray.get(i).draw(spriteBatch);
        }
    }
    
    public void changeSoulboxColor(int colorCode) {
        /**
         * This will render all tiles with the soulbox color.
         */
        this.soulBoxColor = colorCode;
    }
    
    public void removeSoulboxColor() {
        /**
         * Sets soulbox color to -1, which means no color
         */
        this.soulBoxColor = -1;
    }
}

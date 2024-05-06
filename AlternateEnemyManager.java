/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
/**
 *
 * @author wjohn
 */
public class AlternateEnemyManager {
    public static final float SOUND_COOLDOWN = .33f;
    
    private Main mainReference;
    private AudioManager audioManagerReference;
    private AlternateScene alternateScene;
    private AlternateEnemyAnimationManager universalEnemyAnimManager;
    private Array<AlternateEnemy> enemyArray;
    private float viewportWidth;
    private float viewportHeight;
    
    private boolean isSoundReady;
    private float soundCooldown;
    
    private int enemyCount;
    
    public AlternateEnemyManager(AlternateEnemyAnimationManager animManager, float viewportWidth, float viewportHeight, Main mainRef, AudioManager audioManagerReference, AlternateScene alternateScene) {
        this.enemyArray = new Array<AlternateEnemy>();
        this.universalEnemyAnimManager = animManager;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.enemyCount = 0;
        this.mainReference=  mainRef;
        this.audioManagerReference = audioManagerReference;
        this.alternateScene = alternateScene;
    }
    
    public void update(Vector2 playerPosition, float dt) {
        for (int i = 0; i < this.enemyArray.size; i++) {
            this.enemyArray.get(i).update(playerPosition, dt);
            // Also dispose of no health enemies 
            if (this.enemyArray.get(i).getHealth() <= 0) {
                this.enemyArray.removeIndex(i);
                this.enemyCount -= 1;
            }
        }
        if (this.enemyCount <= 0) {
            this.mainReference.disable2D();
            AudioManager.transition_zoom.play();
            audioManagerReference.enable3D();
            this.alternateScene.removeSoulboxColor();
        }
    }
    
    public void render(SpriteBatch spriteBatch, Vector2 playerPosition) {
        for (int i = 0; i < this.enemyArray.size; i++) {
            this.enemyArray.get(i).render(spriteBatch, playerPosition);
        }
    }
            
    public void checkApplyEnemiesHit(Vector2 playerPosition) {
        boolean isHit = false;
        boolean isFinished = false;
        for (int i = 0; i <this.enemyArray.size; i++) {
            if (!isHit) { isHit = this.enemyArray.get(i).checkApplyHit(playerPosition); }
            if (!isFinished) { isFinished = this.enemyArray.get(i).getHealth() <=  0; }
        }
        
        // Play a sound whether a hit or finish was found 
        if (isFinished) {
            AudioManager.sword_finish.play();
        }
        else if (isHit) {
            AudioManager.sword_hit.play();
        }
    }
    public void spawnEnemies(int amountOfEnemies, boolean spawnLeftSide) {
        // First, decide which x location to spawn 
        float spawn_x_location;
        if (spawnLeftSide) {
            spawn_x_location = -100f;
        }
        else {
            spawn_x_location = this.viewportWidth + 100f;
        }
        // Now spawn the enemies, also have them at random y locations
        for (int i = 0; i < amountOfEnemies; i++) {
            this.enemyArray.add(new AlternateEnemy(new Vector2(spawn_x_location, MathUtils.random(0, this.viewportHeight)), this.universalEnemyAnimManager, false));
            this.enemyCount += 1;
        }
    }
    
    public void spawnSoulBox(int colorCode) {
        /**
         * Spawns Soul Box (Colored Wolf), they take 10 hits to defeat, and are passive. 
         * 
         * @param colorCode Grab a color code from AlternateEnemy's static fields.
         */
        AlternateEnemy tmpEnemy = new AlternateEnemy(new Vector2(-50, viewportHeight/2), this.universalEnemyAnimManager, true);
        tmpEnemy.setSoulboxColor(colorCode);
        this.alternateScene.changeSoulboxColor(colorCode);
        this.enemyArray.add(tmpEnemy);
        this.enemyCount += 1;
    }
    
    public int getEnemyCount() {
        return this.enemyCount;
    }
}

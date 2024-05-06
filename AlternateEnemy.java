/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author wjohn
 */
public class AlternateEnemy {
    public static int BLUE_COLOR  = 0;
    public static int PINK_COLOR = 1;
    public static int RED_COLOR = 2;
    public static int GREEN_COLOR = 3;
    
  
    private static float MIN_SPEED = 25f;
    private static float MAX_SPEED = 75f;
    private static int MIN_HEALTH = 3;
    private static int MAX_HEALTH = 8;
    private static float MIN_HIT_DISTANCE = 35f;
    private static float HIT_COOLDOWN = 0.33f;
    private int activeColor;
    private boolean isSoulBox;
    private int healthHits; // Instead of normal health, we just track the amount of times the enemy can take a hit from the player.
    private float movementX; // Just tells us if wolf is moving left or right 
    private Vector2 position;
    private float runtime;
    private float hitCooldown;
    private boolean isHitReady;
    private float speed;
    private AlternateEnemyAnimationManager universalEnemyAnimManager;
    
    
    public AlternateEnemy(Vector2 initialPosition, AlternateEnemyAnimationManager universalEnemyManager, boolean isSoulBox) {
        /**
         * Creates an alternate enemy, the enemy can be a Soulbox which cannot move and has a unique color, or a normal enemy.
         * 
         * PRE REQUISITES:// 
         * 1. If it's a soulbox, make sure you call setSoulboxColor(AlternateEnemy.Color))
         * @param initialPosition Enemy will spawn at this location
         * @param universalEnemyManager Managies all enemies at once
         * @param isSoulBox Soul boxes behave differently 
         * 
         */
        this.position = initialPosition;
        this.universalEnemyAnimManager = universalEnemyManager;
        if (isSoulBox) {
            this.healthHits = 10;
            this.speed = AlternateEnemy.MAX_SPEED;
        }
        else {
            this.healthHits = MathUtils.random(AlternateEnemy.MIN_HEALTH, AlternateEnemy.MAX_HEALTH);
            this.speed = MathUtils.random(AlternateEnemy.MIN_SPEED, AlternateEnemy.MAX_SPEED); // For now, speed will be 50 DEBUG 
        }
        this.movementX = 0f;
        this.hitCooldown = 0f;
        this.isHitReady = true;
        this.isSoulBox = isSoulBox;
    }
    
    public void setSoulboxColor(int color) {
        this.activeColor = color;
    }
    
    public void update(Vector2 playerPosition, float dt) {
        this.runtime += dt; // For Animation Handling
        this.moveTowardsPlayer(playerPosition, dt); 
        this.updateHitCooldown(dt);
    }
    
    public void render(SpriteBatch spriteBatch, Vector2 playerLocation) {
        float distFromPlayer = AlternatePlayer.getDistance(playerLocation, new Vector2(this.position.x, this.position.y)) - 50; // We do -50 to make up for coordinate offset
        if (distFromPlayer > AlternatePlayer.distanceToLightTilesFade) {
            spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1);
        }
        else if (distFromPlayer < AlternatePlayer.distanceToLightTilesFade && distFromPlayer > AlternatePlayer.distanceToLightTiles) {
            spriteBatch.setColor(0.7f, 0.7f, 0.7f, 1);
        }
        else if (!this.isHitReady) {
            spriteBatch.setColor(10f, 0.2f, 0.2f, 1f);
        }
        else {
            spriteBatch.setColor(1, 1, 1, 1);
        }
        if (this.isSoulBox) {
            switch(this.activeColor) {
                case 0:
                    spriteBatch.setColor(0.2f, 0.2f, 10f, 1);
                    break;
                case 1:
                    spriteBatch.setColor(1.0f, 0.75f, 0.79f, 1);
                    break;
                case 2:
                    spriteBatch.setColor(10.0f, 0.2f, 0.2f, 1);
                    break;
                case 3:
                    spriteBatch.setColor(0.2f, 10f, 0.2f, 1);
                    break;
            }
        }
        spriteBatch.draw(this.universalEnemyAnimManager.getCurrentFrame(this.runtime, this.movementX), this.position.x, this.position.y);
        spriteBatch.setColor(1, 1, 1, 1);
    }
    
    public boolean checkApplyHit(Vector2 playerPosition) {
        float distFromPlayer = AlternatePlayer.getDistance(playerPosition, position) - 50; // We do -50 to make up for coordinate offset
        if (distFromPlayer <= AlternateEnemy.MIN_HIT_DISTANCE && this.isHitReady) {
            this.healthHits -= 1;
            this.isHitReady = false;
            this.hitCooldown = AlternateEnemy.HIT_COOLDOWN;
            return true;
        }
        return false;
    }
    
    public int getHealth() {
        return this.healthHits;
    }
    
    public void updateHitCooldown(float dt) {
        if (!this.isHitReady) {
            this.hitCooldown -= dt; 
        }
        
        if (this.hitCooldown < 0f) {
            this.isHitReady = true;
        }
    }
    
    public void moveTowardsPlayer(Vector2 playerPosition, float dt) {
        // Handling X Coordinate Change 
        if (this.position.x > playerPosition.x) {
            this.position.x -= this.speed * dt;
            this.movementX = -1;
        }
        else {
            this.position.x += this.speed * dt;
            this.movementX = 1;
        }
        // Handling Y Coordinate Change
        if (this.position.y > playerPosition.y) {
            this.position.y -= this.speed * dt;
        }
        else {
            this.position.y += this.speed * dt;
        }
    }
    

}

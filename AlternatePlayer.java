/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author wjohn
 */
public class AlternatePlayer {
    public static final float ALTERNATE_STAMINA_LOSS_FACTOR = 30f;
    public static final float ALTERNATE_STAMINA_GAIN_FACTOR = 90f;
    public static final float ALTERNATE_STAMINA_REGAIN_DURATION = 1.5f;
    
    public static final int distanceToLightTiles = 50;
    public static final int distanceToLightTilesFade = 100;
    
    private AudioManager audioManager;
    private AlternateEnemyManager enemyManager;
    private boolean isFaceLeft;
    private int currentAction; // Used to pass in actions to the Alt Animation Manager 
    Vector2 position;
    Vector2 movementVector; // Updates position with movement vector, i.e if player presses "W", movement vector X changes to positive 40, position updated, movement vector reset back to zero
    private float movement_x; // Taken from movementVector, helps us determine which way to make RRR face, set to 0 at the end of each render
    private float speed;
    
    private float staminaDuration;
    private boolean isStaminaDepleted;
    private ValueBar staminaValueBar;
    private boolean isAttacking; 
    
    AlternatePlayerAnimationManager altAnimManager;
    public AlternatePlayer(float viewportWidth, float viewportHeight, ValueBar staminaValueBar, AudioManager audioManager, AlternateEnemyManager enemyManager) {
        this.altAnimManager = new AlternatePlayerAnimationManager();
        this.movementVector = new Vector2(0, 0);
        this.position = new Vector2();
        this.position.x = viewportWidth / 2;
        this.position.y = viewportHeight / 2;
        this.speed = 150f; 
        System.out.println(this.position.toString());
        this.currentAction = AlternatePlayerAnimationManager.IDLE_ANIMATION;
        this.isFaceLeft = false;
        this.movement_x = 0f;
        this.staminaValueBar = staminaValueBar;
        this.isAttacking = false;
        this.audioManager = audioManager;
        this.enemyManager = enemyManager;
    }
    
    public void update(float dt) {
        this.altAnimManager.update(dt);
        this.updateStaminaValue(dt);
        if (!this.movementVector.isZero()) {
            this.position.add(this.movementVector.x * dt, this.movementVector.y * dt); 
            this.movement_x = this.movementVector.x;
            this.movementVector.setZero();
            this.currentAction = AlternatePlayerAnimationManager.RUN_ANIMATION;
        }
        else {
            this.currentAction = AlternatePlayerAnimationManager.IDLE_ANIMATION;
        }
        if (this.isAttacking) { // We handle attacking here, play the swoosh sound, and check enemies hit.
            this.currentAction = AlternatePlayerAnimationManager.LIGHTATK_ANIMATION;
            this.audioManager.stepSwordWoosh(dt);
            this.enemyManager.checkApplyEnemiesHit(position);
            this.isAttacking = false;
        }
    }
    
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(this.altAnimManager.getCurrentFrame(this.currentAction, movement_x, this.isFaceLeft), this.position.x, this.position.y);
        this.movement_x = 0f;
    }
    
    public void enableAttacking() {
        this.isAttacking = true;
    }
    
    public void updateStaminaValue(float deltaTime) {
        this.isStaminaDepleted = staminaValueBar.isEmpty();
        if (this.isAttacking) {
            staminaValueBar.decrementValue(deltaTime * AlternatePlayer.ALTERNATE_STAMINA_LOSS_FACTOR);
            staminaDuration = AlternatePlayer.ALTERNATE_STAMINA_REGAIN_DURATION;
        }
        else {
            if (this.staminaDuration > 0) {
                this.staminaDuration -= deltaTime;
            }
            else {
                staminaValueBar.incrementValue(deltaTime * AlternatePlayer.ALTERNATE_STAMINA_GAIN_FACTOR);
            }
        }
    }
    
    public void movePlayer(int direction) {
        /**
         * Moves player forward
         * 
         * @note Design Decision - Instead of having four functions (move left, forward right, back), use static variables to and "case" to determine where to move
         */
                
        switch (direction) {
            case 0: // forward
                this.movementVector.y = this.speed;
                break;
            case 1: // back 
                this.movementVector.y = -this.speed;
                break;
            case 2: // left 
                this.movementVector.x = -this.speed;
                this.isFaceLeft = true;
                break;
            case 3: // right 
                this.movementVector.x = this.speed;
                this.isFaceLeft = false;
                break;
            case 4: // forward left 
                this.movementVector.x = -this.speed;
                this.movementVector.y = this.speed;
                this.isFaceLeft = true;
                break;
            case 5: // forward right 
                this.movementVector.x = this.speed;
                this.movementVector.y = this.speed;
                this.isFaceLeft = false;
                break;
            case 6: // back left -  A + S 
                this.movementVector.x = -this.speed;
                this.movementVector.y = -this.speed;
                this.isFaceLeft = true;
                break;
            case 7: // back right 
                this.movementVector.x = this.speed;
                this.movementVector.y = -this.speed;
                this.isFaceLeft = false;
                break;
        }
    }
    
    public Vector2 getPositionVector() {
        return this.position;
    }
    
    public static float getDistance(Vector2 pos_one, Vector2 pos_two) {
        return (float) Math.sqrt(Math.pow((pos_two.x - pos_one.x), 2) + Math.pow((pos_two.y - pos_one.y), 2));
    }
    
    public boolean isStaminaDepleted() {
        return this.isStaminaDepleted;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

/**
 *
 * @author wjohn
 */
public class AlternatePlayerAnimationManager {
    public static final int LIGHTATK_ANIMATION = 2;
    public static final int RUN_ANIMATION = 1;
    public static final int IDLE_ANIMATION = 0;
    
    private TextureRegion frame; // For reusability, we often return this object to be drawn on the screen via getCurrentFrame()
    
    private float runtime;
    private Texture idle_sheet;
    private Animation<TextureRegion> idleAnimation;
    
    private Texture run_sheet;
    private Animation<TextureRegion> runAnimation;
    
    private Texture lightatk_sheet;
    private Animation<TextureRegion> lightatkAnimation;
    
    
    public AlternatePlayerAnimationManager() {
        this.idle_sheet = new Texture(Gdx.files.internal("idleAlternateSheet.png"));
        // This creates a 2D Array of TextureRegions                  // 18 Columns in Idle Sprite Sheet      1 Rows in Idle Spritesheet
        TextureRegion[][] tmp = TextureRegion.split(idle_sheet, idle_sheet.getWidth()/18, idle_sheet.getHeight()/1);
        TextureRegion[] idleFrames = tmp[0]; 
        // Now we can create our idleAnimation 
        this.idleAnimation = new Animation<TextureRegion>(0.050f, idleFrames);
        
        // We will now go do the same process as the rest, this is the run spritesheet
        this.run_sheet = new Texture(Gdx.files.internal("runAlternateSheet.png"));
        tmp = TextureRegion.split(run_sheet, run_sheet.getWidth()/24, idle_sheet.getHeight()/1);
        TextureRegion[] runFrames = tmp[0];
        this.runAnimation = new Animation<TextureRegion>(0.035f, runFrames);
        
        // Light Attacks Spritesheet 
        this.lightatk_sheet = new Texture(Gdx.files.internal("lightatkAlternateSheet.png"));
        tmp = TextureRegion.split(this.lightatk_sheet, lightatk_sheet.getWidth()/26, lightatk_sheet.getHeight()/1);
        TextureRegion[] lightatkFrames = tmp[0];
        this.lightatkAnimation = new Animation<TextureRegion>(0.040f, lightatkFrames);
        
        this.runtime = 0f; // Keep track of time to decide which animation to show 
    }
    
    public void update(float dt) {
        this.runtime += dt;
    }
    
    public TextureRegion getCurrentFrame(int action, float movement_x, boolean isFaceLeft) {
        /**
         * Returns a Texture based off the action being performed   
         * 
         * @param action Pass in the static field of this class, i.e AlternatePlayerAnimationManager.RUN_ANIMATION
         */
        switch (action) {
            case 0:
                frame = this.idleAnimation.getKeyFrame(runtime, true);
                break;
            case 1:
                frame = this.runAnimation.getKeyFrame(runtime, true);
                break;
            case 2:
                frame = this.lightatkAnimation.getKeyFrame(runtime, true);
        }
        if ((movement_x > 0 || !isFaceLeft) && frame.isFlipX()) {
            frame.flip(true, false);
        }
        else if ((movement_x < 0 || isFaceLeft) && !frame.isFlipX()) {
            frame.flip(true, false);
        }
        return frame;
    }
}

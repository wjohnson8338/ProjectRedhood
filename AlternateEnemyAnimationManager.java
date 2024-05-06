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
public class AlternateEnemyAnimationManager {
    
    
    private TextureRegion frame;
    
    private Texture run_sheet;
    private Animation<TextureRegion> runAnimation;
    
    public AlternateEnemyAnimationManager() {
        
        this.run_sheet = new Texture(Gdx.files.internal("wolfRunSheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(this.run_sheet, this.run_sheet.getWidth()/8, this.run_sheet.getHeight()/1);
        TextureRegion[] run_frames = tmp[0];
        this.runAnimation = new Animation<TextureRegion>(0.060f, run_frames);
        
    }
    
    public TextureRegion getCurrentFrame(float instanceRunTime, float movement_x) {
        frame = this.runAnimation.getKeyFrame(instanceRunTime, true);
        
        if ((movement_x > 0) && frame.isFlipX()) {
            frame.flip(true, false);
        }
        else if ((movement_x < 0) && !frame.isFlipX()) {
            frame.flip(true, false);
        }
        
        return frame;
    }
    
}

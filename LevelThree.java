/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game.levels;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.runtime.Mundus;
import com.mygdx.game.AudioManager;
import com.mygdx.game.GameCamera;
import com.mygdx.game.PhysicsSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AlternateEnemy;
import com.mygdx.game.AlternateEnemyManager;
import com.mygdx.game.Main;
import com.mygdx.game.Player;

/**
 *
 * @author wjohn
 */
public class LevelThree implements Level {
    // Player Reference so we can monitor location 
    Player playerReference; 
    // Alt Enemy Manager for us to spawn enemies 
    AlternateEnemyManager altEnemyManagerReference;
    // Main Reference to Enable 2D Mode 
    Main mainReference;
    // Audio Manager to Enable 8 Bit Music
    AudioManager audioManagerReference;
    
    AnimationController wolfAnimController;
    GameObject wolfGameObject;
    // Soulbox GameObjects
    GameObject blueSoulBoxObject; 
    GameObject pinkSoulBoxObject;
    GameObject redSoulBoxObject;
    GameObject greenSoulBoxObject;
    
    WolfBoss wolfBoss;
    // For the intro 
    boolean isIntroAttackCompleted;
    
    public LevelThree(Player playerReference, AlternateEnemyManager altEnemyManager, Main main, AudioManager audioManagerReference) {
        this.playerReference = playerReference;
        this.altEnemyManagerReference = altEnemyManager;
        this.mainReference = main;
        this.audioManagerReference = audioManagerReference;
        this.isIntroAttackCompleted = false;
        
    }
    @Override
    public Object[] load(Mundus mundus, Player player, PhysicsSystem physicsSystem, GameCamera camera, AudioManager audioManager) {
        Object[] returnables = LevelHelper.autoload("levelThree.mundus", "bossTerrain", mundus, player, physicsSystem, camera);
        Scene scene = (Scene) returnables[0];
        
        wolfGameObject = scene.sceneGraph.findByName("wolf.gltf");
        ModelComponent wolfModelComponent = (ModelComponent) wolfGameObject.findComponentByType(Component.Type.MODEL);
        wolfAnimController = new AnimationController(wolfModelComponent.getModelInstance());
        
        Vector3 wolfPos = new Vector3();
        wolfGameObject.getPosition(wolfPos);
        wolfBoss = new WolfBoss(wolfPos);
        
        this.blueSoulBoxObject = scene.sceneGraph.findByName("blueSoulBox");
        this.pinkSoulBoxObject = scene.sceneGraph.findByName("pinkSoulbox");
        this.redSoulBoxObject = scene.sceneGraph.findByName("redSoulBox");
        this.greenSoulBoxObject = scene.sceneGraph.findByName("greenSoulBox");
        
        this.blueSoulBoxObject.active = false;
        this.pinkSoulBoxObject.active = false;
        this.redSoulBoxObject.active = false;
        this.greenSoulBoxObject.active = false;
        
        audioManager.activateBossMusic();
        
        
        return returnables;
    }

    @Override
    public void checks(Scene scene) {
        System.out.println(altEnemyManagerReference.getEnemyCount());
        if ((!this.greenSoulBoxObject.active) && (!this.pinkSoulBoxObject.active) && (!this.redSoulBoxObject.active) && (!this.blueSoulBoxObject.active) && (this.isIntroAttackCompleted) && (this.altEnemyManagerReference.getEnemyCount() <= 0)) {
            this.mainReference.endGame();
            this.wolfGameObject.active = false;
        }

        if (!this.isIntroAttackCompleted) {
            if (this.playerReference.getPlayerBodyZ() > 85f) {
                this.isIntroAttackCompleted = true;
                wolfBoss.startAction(wolfBoss.ACTION_CHARGE_REDHOOD);
                this.blueSoulBoxObject.active = true;
                this.pinkSoulBoxObject.active = true;
                this.redSoulBoxObject.active = true;
                this.greenSoulBoxObject.active = true;
            }
        }
        
        if (this.blueSoulBoxObject.active) {
            if((this.playerReference.getPlayerModelX() <= 35 && this.playerReference.getPlayerModelX() > 27) && (this.playerReference.getPlayerModelZ() <= 42 && this.playerReference.getPlayerModelZ() > 35)) {
                this.blueSoulBoxObject.active = false;
                this.altEnemyManagerReference.spawnSoulBox(AlternateEnemy.BLUE_COLOR);
                this.enable2D();
            }
        }
        if (this.redSoulBoxObject.active) {
            if((this.playerReference.getPlayerModelX() <= 68 && this.playerReference.getPlayerModelX() > 62) && (this.playerReference.getPlayerModelZ() <= 226 && this.playerReference.getPlayerModelZ() > 220)) {
                this.redSoulBoxObject.active = false;
                this.altEnemyManagerReference.spawnSoulBox(AlternateEnemy.RED_COLOR);
                this.enable2D();
            }
        }
        if (this.pinkSoulBoxObject.active) {
            if((this.playerReference.getPlayerModelX() <= 149 && this.playerReference.getPlayerModelX() > 145) && (this.playerReference.getPlayerModelZ() <= 147 && this.playerReference.getPlayerModelZ() > 143)) {
                this.pinkSoulBoxObject.active = false;
                this.altEnemyManagerReference.spawnSoulBox(AlternateEnemy.PINK_COLOR);
                this.enable2D();
            }
        }
        if (this.greenSoulBoxObject.active) {
            if((this.playerReference.getPlayerModelX() <= 227 && this.playerReference.getPlayerModelX() > 221) && (this.playerReference.getPlayerModelZ() <= 104 && this.playerReference.getPlayerModelZ() > 99)) {
                this.greenSoulBoxObject.active = false;
                this.altEnemyManagerReference.spawnSoulBox(AlternateEnemy.GREEN_COLOR);
                this.enable2D();
             
        }
        
    }
}
        
    
    public void enable2D() {
        this.mainReference.enable2D();
        this.audioManagerReference.enable2D();
        AudioManager.transition_zoom.play();
    }
        

    @Override
    public void update() {
        wolfBoss.update();
        wolfAnimController.update(Gdx.graphics.getDeltaTime());
    }
    
    
    private class WolfBoss {
        // ANIM Integers to decide which animation to be set to on switch
        private final int ANIM_IDLE = 0;
        private final int ANIM_RUNNING = 1;
        // ACTION Integers to decide which action to perform, i.e charge redhood and attack 
        private final int ACTION_WAIT = 0;
        private final int ACTION_CHARGE_REDHOOD = 1;
        // Basic Fields 
        private Vector3 position;
        private int activeAnimation;
        private int activeAction;
        private float speed;
        // Enemies to spawn 
        private int enemiesToSpawn;
        private final float attackInterval = 20f;
        private float timeUntilAttack;
        
        public WolfBoss(Vector3 wolfPosition) {
            this.position = wolfPosition;
            this.activeAnimation = ANIM_IDLE;
            this.activeAction = ACTION_WAIT;
            this.speed = 35f;
            this.enemiesToSpawn = 2;
            this.timeUntilAttack = this.attackInterval;
        }
        
        public void updateAnimation() {
            /**
             * Updates our animation with the current activated animation 
             * 
             * @param Animation Integer from the final fields
             */
            switch (activeAnimation) {
                case 0:
                    wolfAnimController.setAnimation("Armature|idle|Baked frames.001", -1);
                    break;
                case 1:
                    wolfAnimController.setAnimation("Armature|run|Baked frames.001", -1);
                    break;
            }
        }
        
        public void update() {
            this.performActiveAction();
            this.updateAnimation();
            if (isIntroAttackCompleted && !mainReference.is2DMode && !mainReference.isGameFinished) {
                this.timeUntilAttack -= Gdx.graphics.getDeltaTime();
                if (timeUntilAttack < 0 && this.activeAction != ACTION_CHARGE_REDHOOD) {
                    this.timeUntilAttack = this.attackInterval;
                    this.startAction(ACTION_CHARGE_REDHOOD);
                }
            }
        }
        
        public void performActiveAction() {
            if (this.activeAction == ACTION_WAIT) {
                this.activeAnimation = ANIM_IDLE;
            }
            else if (this.activeAction == ACTION_CHARGE_REDHOOD) {
                this.activeAnimation = ANIM_RUNNING;
                if (Player.getDistance(playerReference.getPlayerModelPosVector(), position) < 10f) {
                    this.activeAction = ACTION_WAIT;
                    altEnemyManagerReference.spawnEnemies(this.enemiesToSpawn/2, true);
                    altEnemyManagerReference.spawnEnemies(this.enemiesToSpawn /2, false);
                    this.enemiesToSpawn += 2;
                    audioManagerReference.enable2D();
                    AudioManager.transition_zoom.play();
                    mainReference.enable2D();
                }
                else {
                    // Not close enough to player, keep moving to player.
                    float dt = Gdx.graphics.getDeltaTime();
                    if (this.position.x > playerReference.getPlayerModelX()) {
                        this.position.x -= dt * this.speed;
                    }
                    else {
                        this.position.x += dt * this.speed;
                    }
                    
                    // Handle Z Coordinate too.
                    if (this.position.z > playerReference.getPlayerModelZ()) {
                        this.position.z -= dt * this.speed;
                    }
                    else {
                        this.position.z += dt * this.speed;
                    }
                    wolfGameObject.setLocalPosition(this.position.x, this.position.y, this.position.z);
                }
            }
        }
        
        public void startAction(int actionNumber) {
            switch(actionNumber) {
                case 0:
                    this.activeAction = ACTION_WAIT;
                case 1:
                    this.activeAction = ACTION_CHARGE_REDHOOD;
            }
        }
    }
}

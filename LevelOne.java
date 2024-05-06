/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game.levels;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.runtime.Mundus;
import com.mbrlabs.mundus.commons.scene3d.components.*;
import com.mygdx.game.AudioManager;
import com.mygdx.game.GameCamera;
import com.mygdx.game.Main;
import com.mygdx.game.PhysicsSystem;
import com.mygdx.game.Player;

/**
 *
 * @author wjohn
 */
public class LevelOne implements Level {
    
    // Keep track of our physics system so we can check if colliding with cave entrance 
    Player playerReference;
    Vector3 playerPosition;
    Main mainReference;
    
    public LevelOne(Main main, Player player) {
        this.playerReference = player;
        this.mainReference = main;
    }
    
    @Override
    public Object[] load(Mundus mundus, Player player, PhysicsSystem physicsSystem, GameCamera camera, AudioManager audioManager) {
//      LevelHelper.autoload(sceneName, terrainName, mundus, player, physicsSystem, camera);
        Object[] returnables = LevelHelper.autoload("levelOne.mundus", "terrainAreaOne", mundus, player, physicsSystem, camera);
        // Handle Audio
        audioManager.setAmbienceAudio(AudioManager.ambience_grass);
        audioManager.playAmbienceAudio();
        
        audioManager.setNormalRunAudio(AudioManager.footsteps_grass_normal);
        audioManager.setSprintRunAudio(AudioManager.footsteps_grass_sprint);
        
        return returnables;
    }

    @Override
    public void checks(Scene scene) {
        // Cave Entrance Coordinate Manipulation 
        this.playerPosition = playerReference.getPlayerModelPosVector();
        if ((playerPosition.z > 113f && playerPosition.z < 117f) && (playerPosition.x > 25f && playerPosition.x < 41f)) {
            this.mainReference.switchLevel(2);
        }
    }
        
    @Override
    public void update() {
    }
    
}

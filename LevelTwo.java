/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game.levels;

import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.LightComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.runtime.Mundus;
import com.mygdx.game.AudioManager;
import com.mygdx.game.GameCamera;
import com.mygdx.game.Main;
import com.mygdx.game.PhysicsSystem;
import com.mygdx.game.Player;
import net.mgsx.gltf.scene3d.lights.SpotLightEx;

/**
 *
 * @author wjohn
 */
public class LevelTwo implements Level {
    private GameObject playerLight;
    private LightComponent lightComponent;
    private SpotLightEx spotlight;
    
    private Main mainReference;
    
    private GameObject playerModel;
    private GameCamera cameraReference;
    private Player player;
    
    
    Vector3 playerPosition;
    
    public  LevelTwo(GameCamera camera, Player player, Main main) {
        this.cameraReference = camera;
        this.player = player;
        this.mainReference = main;
    }
    @Override
    public Object[] load(Mundus mundus, Player player, PhysicsSystem physicsSystem, GameCamera camera, AudioManager audioManager) {
        Object[] returnables = LevelHelper.autoload("levelTwo.mundus", "levelTwoTerrain", mundus, player, physicsSystem, camera);
        
        Scene scene = (Scene) returnables[0];
        
        this.playerLight = scene.sceneGraph.findByName("rrrLight");
        this.playerModel = scene.sceneGraph.findByName("rrrAnimed.gltf");
        
        LightComponent lp = (LightComponent) this.playerLight.findComponentByType(Component.Type.LIGHT);
        spotlight = (SpotLightEx) lp.getLight();
        
        this.playerPosition = new Vector3(); 
        this.playerModel.getLocalPosition(this.playerPosition);
        
        // Handling Audio Now 
        audioManager.setAmbienceAudio(AudioManager.ambience_cave);
        audioManager.playAmbienceAudio();
        
        audioManager.setMusicAudio(AudioManager.music_bacchanale);
        audioManager.playMusicAudio();
        
        audioManager.setNormalRunAudio(AudioManager.footsteps_cave_normal);
        audioManager.setSprintRunAudio(AudioManager.footsteps_cave_sprint);
        
        return returnables;
    }

    @Override
    public void checks(Scene scene) {
    }

    @Override
    public void update() {
        this.playerModel.getLocalPosition(this.playerPosition);
        this.playerLight.setLocalPosition(this.playerPosition.x, this.playerPosition.y+1.5f, this.playerPosition.z);
        spotlight.direction.set(this.cameraReference.getDirection());
        
        if ((this.playerPosition.x < 357f && this.playerPosition.x > 347f) && (this.playerPosition.z < 176f && this.playerPosition.z > 162f)) {
            this.mainReference.switchLevel(3);
        }
        
    }   
}

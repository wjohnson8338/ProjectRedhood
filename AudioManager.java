/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
/**
 *
 * @author wjohn
 */
public class AudioManager {
    // For the repeating attack audio in alternate mode 
    private static float SWORD_WOOSH_INTERVAL = 0.33f;
    private static float BOSS_MUSIC_VOLUME = 15f;
    // Alternate Game Sword Sound Effects
    public static Sound sword_woosh = Gdx.audio.newSound(Gdx.files.internal("swordWoosh.mp3"));
    public static Sound sword_hit = Gdx.audio.newSound(Gdx.files.internal("swordHit.mp3"));
    public static Sound sword_finish = Gdx.audio.newSound(Gdx.files.internal("swordHitFinal.mp3"));
    // Musics
    public static Music music_bacchanale = Gdx.audio.newMusic(Gdx.files.internal("bacchanale.mp3"));
    public static Music music_storyteller = Gdx.audio.newMusic(Gdx.files.internal("theStoryteller.mp3"));
    // Transition
    public static Sound transition_zoom = Gdx.audio.newSound(Gdx.files.internal("transitionZoom.mp3"));
    // Footsteps
    public static Music footsteps_grass_normal = Gdx.audio.newMusic(Gdx.files.internal("grass_footsteps_normal.wav"));
    public static Music footsteps_grass_sprint = Gdx.audio.newMusic(Gdx.files.internal("grass_footsteps_sprint.wav"));
    
    public static Music footsteps_cave_normal = Gdx.audio.newMusic(Gdx.files.internal("cave_footsteps_normal.mp3"));
    public static Music footsteps_cave_sprint = Gdx.audio.newMusic(Gdx.files.internal("cave_footsteps_sprint.mp3"));
    // Ambiences
    public static Music ambience_cave = Gdx.audio.newMusic(Gdx.files.internal("caveAmbience.wav"));
    public static Music ambience_grass = Gdx.audio.newMusic(Gdx.files.internal("grassAmbience.wav"));
    
    // Boss Music 
    public static Music softelgettio_3d = Gdx.audio.newMusic(Gdx.files.internal("softelgettio.mp3"));
    public static Music softelgettio_2d = Gdx.audio.newMusic(Gdx.files.internal("cleanedPixelSoftelgettio.mp3"));
    // Stored Data to Play
    private Music active_footsteps_normal;
    private Music active_footsteps_sprint;
    private Music active_ambience;
    private Music active_music;
    
    // Alternate Game SwordWhoosh Hit. NOTE: ALternateGame is the 2D Game.
    private float swordWooshTimer;

    
        
    
    private final Player playerReference;
    
    public AudioManager(Player playerReference) {
        /**
         * Handles the audio on our game. 
         * 
         * PRE-REQUISITES:// 
         * 1. Correctly init and setup Player in main 
         * 
         * @param playerReference Used to change audio based off of player state 
         */
        
        // Musics 
        this.music_bacchanale.setLooping(true);
        this.music_storyteller.setLooping(true);
        this.music_storyteller.setVolume(0.15f);
        this.music_bacchanale.setVolume(0.7f);
        
        // Sound  Effect Volumes 
        
        // Ambience Volume/Loop Edits
        this.ambience_grass.setVolume(0.25f);
        this.ambience_grass.setLooping(true);
        
        this.ambience_cave.setVolume(1.3f);
        this.ambience_cave.setLooping(true);
        // Footstep Volume/Loop Edits
        this.footsteps_grass_normal.setLooping(true);
        this.footsteps_grass_sprint.setLooping(true);
        //
        // Boss Music Settings
        this.softelgettio_2d.setLooping(true);
        this.softelgettio_3d.setLooping(true);
        
        
        // Defaults
        // Music
        this.active_music= AudioManager.music_storyteller;
        this.active_music.play();
        //Ambience
        this.active_ambience = AudioManager.ambience_grass;
        this.active_ambience.play();
        
        
        this.playerReference = playerReference;
        //Footsteps
        this.active_footsteps_normal = footsteps_grass_normal;
        this.active_footsteps_sprint = footsteps_grass_sprint;
        
        // Alternate Game 
        this.swordWooshTimer = AudioManager.SWORD_WOOSH_INTERVAL;
        
        // Also Change Max Simultaneous Sounds
    }
      
    public void stepAudio() {
        // Player is Sprinting and Sound is not being played 
        if (this.playerReference.isSprinting() && !this.active_footsteps_sprint.isPlaying()) { 
            this.active_footsteps_sprint.play();
            this.active_footsteps_normal.stop();
        } 
        // Player is moving, but not sprinting, and footsteps isn't playing
        else if (this.playerReference.isMoving() && !this.active_footsteps_normal.isPlaying() && !this.playerReference.isSprinting()) { 
            this.active_footsteps_normal.play();
            this.active_footsteps_sprint.stop();
        }
        // Player is not moving, immediately stop all footsteps sounds 
        else if (!this.playerReference.isMoving()) {
            this.active_footsteps_normal.stop();
            this.active_footsteps_normal.stop();
        }
    }
 
   public void stepSwordWoosh(float dt) {
       this.swordWooshTimer -= dt; 
       if (swordWooshTimer < 0f) {
           AudioManager.sword_woosh.play();
           this.swordWooshTimer = AudioManager.SWORD_WOOSH_INTERVAL;
       }
   }
    
    public void setAmbienceAudio(Music ambienceAudio) {
        /**
         * Changes the ambience audio to the passed parameter 
         * Will also stop audio for the currently playing ambience audio
         * 
         * @param ambienceAudio a Music object for the audio manager to play 
         */
        this.active_ambience.stop();
        this.active_ambience = ambienceAudio;
    }
    
    public void setMusicAudio(Music musicAudio) {
        this.active_music.stop();
        this.active_music = musicAudio;
    }
    
    public void setNormalRunAudio(Music normalRunAudio) {
        /**
         * Changes non-sprint movement audio to passed parameter
         * 
         * @param normalRunAudio a Music object to play when character moves (non-sprinting)
         */
//        this.active_footsteps_normal.stop();
        this.active_footsteps_normal.stop();
        this.active_footsteps_normal = normalRunAudio;
    }
    
    public void setSprintRunAudio(Music sprintRunAudio) {
        /**
         * Changes sprint movement audio to passed parameter
         * 
         * @param normalRunAudio a Music object to play when character moves (non-sprinting)
         */
//        this.active_footsteps_sprint.stop();
        this.active_footsteps_sprint.stop();
        this.active_footsteps_sprint = sprintRunAudio;
    }
    public void playAmbienceAudio() {
        /**
         * Play Audio Manager's Active Ambience Audio 
         * 
         * PRE REQUISITES:// 
         * 1. setAmbienceAudio(music Music) must have been called at least once. 
         */
        this.active_ambience.play();
    }
    
    public void playMusicAudio() {
        /**
         * Play Audio Manager's Active Music Audio
         * 
         * PRE REQUISITES://
         * 1. setMusicAudio(music Music) must have been called at least once. 
         */
        this.active_music.play();
    }
    
    public void enable3D() {
        softelgettio_3d.setVolume(BOSS_MUSIC_VOLUME);
        softelgettio_2d.setVolume(0f);
    }
    
    public void enable2D() {
        softelgettio_3d.setVolume(0f);
        softelgettio_2d.setVolume(BOSS_MUSIC_VOLUME);
    }
    
    public void activateBossMusic() {
        // Turning off active musics/ambiences
        this.active_ambience.stop();
        this.active_music.stop();
        
        softelgettio_3d.play();
        softelgettio_2d.play();
        softelgettio_2d.setVolume(0f);
        softelgettio_3d.setVolume(BOSS_MUSIC_VOLUME);
    }
    
    public void dispose() {
        sword_woosh.dispose();
        sword_hit.dispose();
        sword_finish.dispose();
        // Footsteps
        footsteps_grass_normal.dispose();
        footsteps_grass_sprint.dispose();

        footsteps_cave_normal.dispose();
        footsteps_cave_sprint.dispose();
        // Ambiences
        ambience_cave.dispose();
        ambience_grass.dispose();
        
        transition_zoom.dispose();
    }
}

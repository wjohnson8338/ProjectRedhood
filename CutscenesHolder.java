/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector3;
/**
 *
 * @author wjohn
 */
public class CutscenesHolder {
    private Array<CutsceneData> cutscenes;
    private Player player;
    
    public CutscenesHolder(Player player) {
        this.cutscenes = new Array<CutsceneData>();
        this.player = player;
        // Create our test cutscene 
        
        Array<CutscenePoint> testPoints = new Array<CutscenePoint>();
        testPoints.add(new CutscenePoint(new Vector3(183f, 17f, 186f), player.getPlayerModelPosVector())); 
        testPoints.add(new CutscenePoint(new Vector3(281f, 50f, 180f), player.getPlayerModelPosVector()));
        testPoints.add(new CutscenePoint(new Vector3(106f, 15f, 116f)));
        this.cutscenes.add(new CutsceneData("test", testPoints, 10f));
        
        // Level One Cutscene
        
        Array<CutscenePoint> levelOnePoints = new Array<CutscenePoint>();
        levelOnePoints.add(new CutscenePoint(new Vector3(100, 40, 40), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(150, 40, 32), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(180, 40, 42), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(200f, 40f, 60f), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(200f, 40f, 120f), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(120f, 40f, 155f), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(110f, 40f, 180f), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(96f, 40f, 211f), player.getPlayerModelPosVector()));
        levelOnePoints.add(new CutscenePoint(new Vector3(96f, 8f, 211f), player.getPlayerModelPosVector()));
        this.cutscenes.add(new CutsceneData("levelOneCutscene", levelOnePoints, 11f));
       


    }
    
    public CutsceneData getCutsceneByName(String name) {
        for (int i = 0; i < this.cutscenes.size; i++) {
            if (this.cutscenes.get(i).getCutsceneName() == name) {
                return this.cutscenes.get(i);
            }
        }
        // If we cannot find the name, just return the first one.
        return this.cutscenes.get(0);
    }
    
}

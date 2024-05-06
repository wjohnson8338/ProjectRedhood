package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

// My Imports 
import com.mygdx.game.levels.Level;
import com.mygdx.game.levels.LevelOne;
import com.mygdx.game.levels.LevelTwo;
import com.mygdx.game.levels.TestLevel;
// Scene2D Imports
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
// Mundus Imports
import com.mbrlabs.mundus.runtime.*;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.*;
import com.mbrlabs.mundus.commons.scene3d.components.*;

// Bullet Import
import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
// GLTF Imports
// GDX Native 3D Imports
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Array;
// GDX Native Imports 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.game.levels.LevelThree;


public class Main extends ApplicationAdapter {
    // Scene 2D and UI  // TODO All of this information should be moved to a class.
    private Stage stage;
    private Image image;
    private Image imageTwo;
    private UserInterfaceManager uiManager;
    // Scene Building Information
    private Mundus mundus;
    private Scene scene;   
    private GameObject terrainObject;
    private TerrainComponent terrainComponent;
    
    // Camera Information
    private GameCamera camera;
    // Player Information
    private Player player;
    private AnimationController animController; // Debug 
    // Bullet Physics Handling
    PhysicsSystem physicsSystem;
    private DebugDrawer debugDrawer;
    private btRigidBody playerRigidBody;
    private btRigidBody terrainBody;
//    private btCollisionConfiguration bulletConfig; // Configure bullet collision, and algorithms associated 
//    private btDispatcher bulletDispatcher; // Iterates over pairs looking for collisions. // trash 
    // Input Handling 
    private GameInputProcessor inputProcessor; 
    // Cutscene Management
    private CutscenesHolder cutscenesHolder;
    // Audio Management
    private AudioManager audioManager;
    // Level Management
    private Level activeLevel;
    private Level debugLevel;
    private Level levelOne;
    private Level levelTwo;
    private Level levelThree;
    private int levelNumber;
    // Alternate Level Management (Switching to 3d Mode)
    private OrthographicCamera altCamera;
    private AlternatePlayer altPlayer;
    private AlternateScene altScene;
    private SpriteBatch spriteBatch;
    
    public boolean is2DMode;
    public boolean isTransitioning2D;
    private boolean isTransitioning3D;
    
    private AlternateEnemyAnimationManager enemyAnimManager;
    private AlternateEnemyManager altEnemyManager;
    
    private float introTimer;
    public boolean isGameFinished;

    @Override
    public void create () {
        levelNumber = 1;
        // Scene2d // TODO Eventually move this stuff to a class.
        uiManager = new UserInterfaceManager();
        uiManager.initHealthBar("health", new Vector2(Gdx.graphics.getWidth() * 0.98f, Gdx.graphics.getHeight() * 0.5f), 1.5f, 100f, 0f, 100f, new Image(new Texture(Gdx.files.internal("healthBar.png"))));
        uiManager.initStaminaBar("stamina", new Vector2(Gdx.graphics.getWidth() * 0.955f, Gdx.graphics.getHeight() * 0.5f), 1f, 100f, 0f, 100f, new Image(new Texture(Gdx.files.internal("staminaBar.png"))));
        uiManager.initGoalFrame(new Vector2(Gdx.graphics.getWidth() * 0.007f, Gdx.graphics.getHeight() * 0.87f), 0.35f);
        uiManager.enableTitle();
        // Scene Information
        mundus = new Mundus(Gdx.files.internal("RRRProject"));
        scene = mundus.loadScene("levelOne.mundus");
        
        // Terrain Info
        terrainObject = scene.sceneGraph.findByName("terrainAreaOne");
        terrainComponent = (TerrainComponent) terrainObject.findComponentByType(Component.Type.TERRAIN);  
        
        // Player Information
        player = new Player(uiManager.getHealthValueBar(), uiManager.getStaminaValueBar());
        player.initPlayerModel(scene.sceneGraph.findByName("rrrAnimed.gltf"));
        // Camera Information
        camera = new GameCamera(new PerspectiveCamera(80f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), player);
        player.setCamera(camera);
        // Physics Handling  
        Bullet.init(); // Required before making any further calls to Bullet
        physicsSystem = new PhysicsSystem(true);
        player.initPhysicsSystemReference(physicsSystem);
        // Animation Handling  
        animController = new AnimationController(player.getModelInstance());
        player.initAnimationController(animController);
        // Cutscene Management
        cutscenesHolder = new CutscenesHolder(player);
        // Audio Manager
        audioManager = new AudioManager(player);
        // Set camera for scene
        scene.cam = camera.getCamera();        
        // Game Ending?
        isGameFinished = false;
        // Now add our Physics Body
        terrainBody = physicsSystem.addTerrain(terrainObject, terrainComponent);
        physicsSystem.addBody(player.initPlayerEntity());
        // CUTSCENE TESTING
        camera.playCutscene(cutscenesHolder.getCutsceneByName("levelOneCutscene"));
        camera.getCamera().far = 240f;
        // Alternate Level 
        float zoom = 0.4f;
        spriteBatch = new SpriteBatch();
        altCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        altCamera.setToOrtho(false, Gdx.graphics.getWidth()*zoom, Gdx.graphics.getHeight()*zoom);
        altScene = new AlternateScene(altCamera.viewportWidth, altCamera.viewportHeight);
        
        enemyAnimManager = new AlternateEnemyAnimationManager();
        altEnemyManager = new AlternateEnemyManager(enemyAnimManager, altCamera.viewportWidth, altCamera.viewportHeight, this, audioManager, altScene);
        
        altPlayer = new AlternatePlayer(altCamera.viewportWidth, altCamera.viewportHeight, uiManager.getStaminaValueBar(), audioManager, altEnemyManager);
        // Level Making
        debugLevel = new TestLevel();
        levelOne = new LevelOne(this, player);
        levelTwo = new LevelTwo(camera, player, this);
        levelThree = new LevelThree(player, altEnemyManager, this, audioManager);
        activeLevel = levelOne; 

        // Main 3D -- > Alternate Level Transition
        isTransitioning2D = false;
        isTransitioning3D = false;
        is2DMode = false;
        // Input Handling 
        inputProcessor = new GameInputProcessor(camera, player, physicsSystem, altPlayer);
        Gdx.input.setInputProcessor(inputProcessor);
        Gdx.input.setCursorCatched(true);
        
        this.introTimer = 0f;
        
        
    }
    
    @Override
    public void resize(int width, int height) {
        // This block of code will handle resizing and camera viewport.
    }

    @Override
    public void render () {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();
        // If transitiong 2D or 3D, update the camera's field of view through it's method 
        if (this.isTransitioning2D) {
            this.enable2D();
        }
        else if (this.isTransitioning3D) {
            this.disable2D();
        }
        // It's not 2D Mode, so keep rending our 3D Stuff.
        if (!is2DMode) {
            physicsSystem.update(Gdx.graphics.getDeltaTime()); 
            // If we are currently transitioning into 2D or 3D, stop any input or updates from player and any other visual effecting updates.
            if ((!this.isTransitioning2D && !this.isTransitioning3D) || this.isGameFinished) {
                animController.update(Gdx.graphics.getDeltaTime());
                inputProcessor.processInput();
                camera.updateCamera(dt);
                player.update(dt);
            }
            scene.sceneGraph.update();
            scene.render();
            physicsSystem.render(camera, player); // Rendering Debug Lines
            uiManager.render();
            audioManager.stepAudio();
        }
        // It's 2D mode, render our 2D Stuff.
        else {
            inputProcessor.altProcessInput();
            altCamera.update();
            spriteBatch.setProjectionMatrix(altCamera.combined);
            spriteBatch.begin(); // SPRITE BATCH STAR
            altPlayer.update(dt);
            altScene.update(altPlayer.getPositionVector());
            altScene.render(spriteBatch);
            altPlayer.render(spriteBatch);
            altEnemyManager.update(altPlayer.getPositionVector(), dt);
            altEnemyManager.render(spriteBatch, altPlayer.getPositionVector());
            spriteBatch.end();// SPRITE BATCH END 
            uiManager.render();
            audioManager.stepAudio();
            
        }
        
        if (this.introTimer < 30f) {
            this.introTimer += dt;
            if (this.introTimer > 28f) {
                uiManager.disableTitle();
            }
        }
        
        // Level Checks/Updates
        activeLevel.checks(scene);
        activeLevel.update();
        
        
        
        // Debug Key Presses
        if (Gdx.input.isKeyPressed(Keys.X)) {
//            Object[] returnedItems = debugLevel.load(mundus, player, physicsSystem, camera);
//            scene = (Scene) returnedItems[0];
//            animController = (AnimationController) returnedItems[1];
              switchLevel(2);
        }
        else if (Gdx.input.isKeyPressed(Keys.C)) {
//            Object[] returnedItems = levelOne.load(mundus, player, physicsSystem, camera);
//            scene = (Scene) returnedItems[0];
//            animController = (AnimationController) returnedItems[1];
              switchLevel(3);
        }
        else if (Gdx.input.isKeyJustPressed(Keys.P)) {
            this.enable2D();
            audioManager.enable2D();
            AudioManager.transition_zoom.play();
        }
        else if (Gdx.input.isKeyPressed(Keys.M)) {
            this.disable2D();
            audioManager.enable3D();
            AudioManager.transition_zoom.play();
        }



    }

    @Override
    public void dispose () {
        mundus.dispose();
        scene.dispose();
        physicsSystem.dispose();
        stage.dispose();
        spriteBatch.dispose();
        audioManager.dispose();
        
    }
    
    public void switchLevel(int levelNum) {
        int levelToChange = levelNum;
        switch (levelToChange) {
            case 1:
                basicLoad(levelOne);
                break;
            case 2:
                basicLoad(levelTwo);
                break;
            case 3:
                basicLoad(levelThree);
        }
    }
    
    public void enable2D() {
        this.isTransitioning2D = true;
        if (camera.enable2D(Gdx.graphics.getDeltaTime())) {
            this.is2DMode = true;
            this.isTransitioning2D = false;
        }
    }
    
    public void disable2D() {
        this.isTransitioning3D = true;
        this.is2DMode = false; 
        if (camera.disable2D(Gdx.graphics.getDeltaTime())) {
            this.isTransitioning3D = false;
        }
    }
    public void basicLoad(Level level) {
        Object[] returnedItems = level.load(mundus, player, physicsSystem, camera, audioManager);
        scene = (Scene) returnedItems[0];
        animController = (AnimationController) returnedItems[1];
        activeLevel = level;
    }
    
    public void endGame() {
        this.uiManager.enableTitle();
        this.isGameFinished = true;
    }
    
    public boolean checkIsGameFinished() {
        return this.isGameFinished;
    }
    
    
}

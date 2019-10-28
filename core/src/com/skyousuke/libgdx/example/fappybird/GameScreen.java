package com.skyousuke.libgdx.example.fappybird;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.FloorComponent;
import com.skyousuke.libgdx.example.fappybird.component.MovingComponent;
import com.skyousuke.libgdx.example.fappybird.component.PipeComponent;
import com.skyousuke.libgdx.example.fappybird.entity.EntityHelper;
import com.skyousuke.libgdx.example.fappybird.system.BackgroundSystem;
import com.skyousuke.libgdx.example.fappybird.system.BirdSystem;
import com.skyousuke.libgdx.example.fappybird.system.CameraSystem;
import com.skyousuke.libgdx.example.fappybird.system.CollisionSystem;
import com.skyousuke.libgdx.example.fappybird.system.FloorSystem;
import com.skyousuke.libgdx.example.fappybird.system.MovingSystem;
import com.skyousuke.libgdx.example.fappybird.system.PipeSystem;
import com.skyousuke.libgdx.example.fappybird.system.PlayerSystem;
import com.skyousuke.libgdx.example.fappybird.system.RenderSystem;
import com.skyousuke.libgdx.example.fappybird.system.ScoreSystem;

public class GameScreen extends AbstractGameScreen implements CollisionSystem.CollisionListener, ScoreSystem.ScoreListener {

    private Engine ashlyEngine;
    private Hud hud;
    private Entity cameraEntity;
    private Entity bird;

    private boolean gameStart;
    private boolean gameOver;
    private int score;

    private WorldListener worldListener;
    private Array<Entity> pipeFamilyEntities = new Array<>();

    public GameScreen() {
        hud = new Hud();
        worldListener = hud;

        ashlyEngine = new PooledEngine();

        //Add System
        ashlyEngine.addSystem(new MovingSystem());
        ashlyEngine.addSystem(new BirdSystem());
        ashlyEngine.addSystem(new PlayerSystem());

        ashlyEngine.addSystem(new CameraSystem(true));
        ashlyEngine.addSystem(new BackgroundSystem());
        ashlyEngine.addSystem(new FloorSystem());
        ashlyEngine.addSystem(new PipeSystem());
        ashlyEngine.addSystem(new CollisionSystem(this));
        ashlyEngine.addSystem(new ScoreSystem(this));
        ashlyEngine.addSystem(new RenderSystem());

        // Add Entity
        cameraEntity = EntityHelper.newCamera(ashlyEngine.getSystem(RenderSystem.class).getCamera());
        ashlyEngine.addEntity(cameraEntity);
        ashlyEngine.addEntity(EntityHelper.newBackground(cameraEntity));

        for (int i = 0; i < 5; i++) {
            Entity floor = EntityHelper.newFloor(cameraEntity);
            ashlyEngine.addEntity(floor);
        }

        float floorHeight = ComponentMappers.transformMapper.get(ashlyEngine.getEntitiesFor(Family.all(FloorComponent.class).get()).first()).dimension.y;
        BackgroundSystem.setFloorHeight(floorHeight);
        PipeSystem.setFloorHeight(floorHeight);

        init();
    }

    private void init() {
        if (bird != null)
            ashlyEngine.removeEntity(bird);

        bird = EntityHelper.newBird();
        ashlyEngine.addEntity(bird);

        ComponentMappers.cameraMapper.get(cameraEntity).target = bird;

        if (worldListener != null)
            worldListener.gameRestart();

        ashlyEngine.getSystem(FloorSystem.class).initFloors();

        for (int i = 0; i < pipeFamilyEntities.size; i++) {
            ashlyEngine.removeEntity(pipeFamilyEntities.get(i));
        }
        pipeFamilyEntities.clear();

        ashlyEngine.getSystem(CollisionSystem.class).setProcessing(true);

        gameStart = false;
        gameOver = false;
        setScore(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            RenderSystem renderSystem = ashlyEngine.getSystem(RenderSystem.class);
            renderSystem.setDebugMode(!renderSystem.isDebugMode());
        }

        if (Gdx.input.justTouched()) {
            if (!gameStart) {
                startGame();
            } else if (gameOver) {
                init();
            } else {
                BirdSystem.flyUp(bird);
            }
        }

        ashlyEngine.update(delta);

        hud.update(delta);
        hud.render();
    }

    private void startGame() {
        for (int i = 0; i < 3; i++) {
            Entity lowerPipeBody = EntityHelper.newPipeBody();
            Entity lowerPipeHead = EntityHelper.newPipeHead();
            Entity lowerPipe = EntityHelper.newPipe(lowerPipeBody, lowerPipeHead, cameraEntity);

            ashlyEngine.addEntity(lowerPipeBody);
            ashlyEngine.addEntity(lowerPipeHead);
            ashlyEngine.addEntity(lowerPipe);

            Entity upperPipeBody = EntityHelper.newPipeBody();
            Entity upperPipeHead = EntityHelper.newPipeHead();
            Entity upperPipe = EntityHelper.newPipe(upperPipeBody, upperPipeHead, cameraEntity);

            ashlyEngine.addEntity(upperPipeBody);
            ashlyEngine.addEntity(upperPipeHead);
            ashlyEngine.addEntity(upperPipe);

            pipeFamilyEntities.addAll(lowerPipeBody, lowerPipeHead, lowerPipe, upperPipeBody, upperPipeHead, upperPipe);
        }

        float birdPositionX = ComponentMappers.transformMapper.get(bird).position.x;
        ashlyEngine.getSystem(PipeSystem.class).initPipes(birdPositionX + PipeComponent.START_PIPE_DISTANCE);

        BirdSystem.start(bird);
        gameStart = true;
        if (worldListener != null)
            worldListener.gameStart();

    }

    @Override
    public void birdHitFloor() {
        BirdComponent birdComponent = ComponentMappers.birdMapper.get(bird);
        MovingComponent birdMoving = ComponentMappers.movingMapper.get(bird);

        if (!gameOver)
        {
            birdComponent.dead = true;
            birdComponent.oldFlyAcceleration = 0;
            birdComponent.flyAcceleration = 0;
            birdComponent.playAnimation = false;

            birdMoving.velocity.setZero();
            birdMoving.acceleration.set(0, 0);

            gameOver = true;

            ashlyEngine.getSystem(CollisionSystem.class).setProcessing(false);
        }
    }

    @Override
    public void birdHitPipe() {
        BirdComponent birdComponent = ComponentMappers.birdMapper.get(bird);
        MovingComponent birdMoving = ComponentMappers.movingMapper.get(bird);

        if (!birdComponent.dead)
        {
            birdComponent.dead = true;
            birdComponent.oldFlyAcceleration = 0;
            birdComponent.flyAcceleration = 0;
            birdComponent.playAnimation = false;

            birdMoving.velocity.setZero();
            birdMoving.acceleration.set(0, BirdComponent.GRAVITY_ACCELERATION);
        }
    }

    private void setScore(int score) {
        this.score = score;
        if (worldListener != null)
            worldListener.scoreUpdate(score);
    }

    @Override
    public void score() {
        score++;
        setScore(score);
    }

    @Override
    public void resize(int width, int height) {
        ashlyEngine.getSystem(RenderSystem.class).getViewport().update(width, height);
        hud.resize(width, height);
    }

    @Override
    public void hide() {
        hud.dispose();
        for (EntitySystem system : ashlyEngine.getSystems()) {
            if (system instanceof Disposable)
                ((Disposable) system).dispose();
        }
    }
}

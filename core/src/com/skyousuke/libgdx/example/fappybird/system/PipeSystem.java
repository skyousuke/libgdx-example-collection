package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.skyousuke.libgdx.example.fappybird.FlappyBird;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.PipeComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class PipeSystem extends IteratingSystem {

    private static float floorHeight;

    private int pipeProcessedCount;
    private Entity lowerPipe;
    private Entity upperPipe;

    public PipeSystem() {
        super(Family.all(PipeComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        pipeProcessedCount = 0;
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PipeComponent pipe = ComponentMappers.pipeMapper.get(entity);
        TransformComponent pipeBodyTransform = ComponentMappers.transformMapper.get(pipe.pipeBody);
        TransformComponent pipeHeadTransform = ComponentMappers.transformMapper.get(pipe.pipeHead);
        TransformComponent cameraTransform = ComponentMappers.transformMapper.get(pipe.camera);

        boolean isPipeOutOfScreen = pipeHeadTransform.position.x + pipeHeadTransform.dimension.x
                < cameraTransform.position.x - FlappyBird.SCENE_WIDTH * 0.5f;

        boolean isLowerPipe = (pipeProcessedCount % 2 == 0);
        if (isLowerPipe)
            lowerPipe = entity;
        else
            upperPipe = entity;

        if (isPipeOutOfScreen)
        {
            float newPositionX = pipeBodyTransform.position.x + PipeComponent.DISTANCE_BETWEEN_DOUBLE_PIPES * PipeComponent.DOUBLE_PIPE_COUNT;
            setPipePositionX(entity, newPositionX);
            pipe.birdPassedOver = false;

            if (!isLowerPipe)
                randomPipeHeight(lowerPipe, upperPipe, pipeHeadTransform.dimension.y);
        }
        pipeProcessedCount++;
    }

    public void initPipes(float startPositionX) {
        ImmutableArray<Entity> pipes = getEntities();
        for (int i = 0; i < pipes.size() / 2; i++) {
            Entity lowerPipe = pipes.get(i * 2);
            Entity upperPipe = pipes.get(i * 2 +1);
            Entity pipeHead = ComponentMappers.pipeMapper.get(lowerPipe).pipeHead;
            float pipeHeadHeight = ComponentMappers.transformMapper.get(pipeHead).dimension.y;

            PipeSystem.setPipePositionX(lowerPipe, startPositionX + i * PipeComponent.DISTANCE_BETWEEN_DOUBLE_PIPES);

            ComponentMappers.pipeMapper.get(upperPipe).invert = true;
            PipeSystem.setPipePositionX(upperPipe, startPositionX + i * PipeComponent.DISTANCE_BETWEEN_DOUBLE_PIPES);

            PipeSystem.randomPipeHeight(lowerPipe, upperPipe, pipeHeadHeight);


            PipeComponent lowerPipeComponent = ComponentMappers.pipeMapper.get(lowerPipe);
            PipeComponent upperPipeComponent = ComponentMappers.pipeMapper.get(upperPipe);

            lowerPipeComponent.birdPassedOver = false;
            upperPipeComponent.birdPassedOver = false;
        }
    }

    public static void setFloorHeight(float floorHeight) {
        PipeSystem.floorHeight = floorHeight;
    }

    public static void setPipePositionX(Entity pipe, float x) {
        PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
        TransformComponent bodyTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeBody);
        updatePipe(pipe, x, bodyTransform.dimension.y, pipeComponent.invert);
    }

    public static void setPipeHeight(Entity pipe, float height) {
        PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
        TransformComponent bodyTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeBody);
        updatePipe(pipe, bodyTransform.position.x, height, pipeComponent.invert);
    }

    public static void updatePipe(Entity pipe, float x, float height, boolean invert) {
        PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
        TransformComponent bodyTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeBody);
        TransformComponent headTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeHead);

        bodyTransform.dimension.y = height;
        pipeComponent.invert = invert;

        final int halfScreenHeight = FlappyBird.SCENE_HEIGHT / 2;

        if (!invert) {
            bodyTransform.position.set(x, floorHeight - halfScreenHeight);
            headTransform.position.set(x - PipeComponent.HALF_WIDTH_DIFFERENCE, floorHeight + height - halfScreenHeight);
        } else {
            bodyTransform.position.set(x, halfScreenHeight - height);
            headTransform.position.set(x - PipeComponent.HALF_WIDTH_DIFFERENCE, halfScreenHeight - height - PipeComponent.HEAD_HEIGHT);
        }

        bodyTransform.bound.setPosition(bodyTransform.position.x, bodyTransform.position.y);
        headTransform.bound.setPosition(headTransform.position.x, headTransform.position.y);

        float bodyWidth = bodyTransform.dimension.x;
        float headWidth = headTransform.dimension.x;
        float headHeight = headTransform.dimension.y;

        bodyTransform.bound.setVertices(new float[]{0, 0, 0, height, bodyWidth, height, bodyWidth, 0});
        headTransform.bound.setVertices(new float[]{0, 0, 0, headHeight, headWidth, headHeight, headWidth, 0});
    }

    private static void randomPipeHeight(Entity lowerPipe, Entity upperPipe, float pipeHeadHeight)
    {
        float sumPipeHeight = FlappyBird.SCENE_HEIGHT - floorHeight -  PipeComponent.SPACE_HEIGHT - pipeHeadHeight * 2;
        float minHeight = PipeComponent.MINIMUM_PIPE_HEIGHT;
        float maxHeight = sumPipeHeight - PipeComponent.MINIMUM_PIPE_HEIGHT;
        float lowerPipeHeight = MathUtils.random(minHeight, maxHeight);
        float upperPipeHeight = sumPipeHeight - lowerPipeHeight;

        PipeSystem.setPipeHeight(lowerPipe, lowerPipeHeight);
        PipeSystem.setPipeHeight(upperPipe, upperPipeHeight);
    }
}

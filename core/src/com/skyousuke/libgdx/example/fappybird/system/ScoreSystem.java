package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.PipeComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class ScoreSystem extends EntitySystem {

    private ImmutableArray<Entity> birds;
    private ImmutableArray<Entity> pipes;

    private ScoreListener listener;

    public interface ScoreListener {
        void score();
    }

    public ScoreSystem(ScoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void addedToEngine(Engine engine) {
        birds = engine.getEntitiesFor(Family.all(BirdComponent.class).get());
        pipes = engine.getEntitiesFor(Family.all(PipeComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < birds.size(); i++) {
            Entity bird = birds.get(i);
            TransformComponent birdTransform = ComponentMappers.transformMapper.get(bird);

            for (int j = 0; j < pipes.size(); j++) {
                Entity pipe = pipes.get(j);
                PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
                TransformComponent pipeHeadTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeHead);

                if (!pipeComponent.birdPassedOver && pipeHeadTransform.position.x + pipeHeadTransform.dimension.x * 0.5f < birdTransform.position.x) {
                    pipeComponent.birdPassedOver = true;

                    Entity nearPipe = findNearPipe(pipe);
                    if (nearPipe != null)
                        ComponentMappers.pipeMapper.get(nearPipe).birdPassedOver = true;

                    if (listener != null)
                        listener.score();
                }
            }
        }
    }

    private Entity findNearPipe(Entity pipe) {
        PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
        TransformComponent pipeHeadTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeHead);

        for (int i = 0; i < pipes.size(); i++) {
            Entity otherPipe = pipes.get(i);
            if (otherPipe != pipe) {
                PipeComponent otherPipeComponent = ComponentMappers.pipeMapper.get(otherPipe);
                TransformComponent otherPipeHeadTransform = ComponentMappers.transformMapper.get(otherPipeComponent.pipeHead);

                if (Math.abs(pipeHeadTransform.position.x - otherPipeHeadTransform.position.x) < 1)
                    return otherPipe;
            }
        }
        return null;
    }
}

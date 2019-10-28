package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.FloorComponent;
import com.skyousuke.libgdx.example.fappybird.component.PipeComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class CollisionSystem extends EntitySystem {

    private CollisionListener listener;
    private ImmutableArray<Entity> birds;
    private ImmutableArray<Entity> pipes;
    private ImmutableArray<Entity> floors;

    public interface CollisionListener {
        void birdHitFloor();
        void birdHitPipe();
    }

    public CollisionSystem(CollisionListener listener) {
        this.listener = listener;
    }

    @Override
    public void addedToEngine(Engine engine) {
        birds = engine.getEntitiesFor(Family.all(BirdComponent.class).get());
        pipes = engine.getEntitiesFor(Family.all(PipeComponent.class).get());
        floors = engine.getEntitiesFor(Family.all(FloorComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < birds.size(); i++) {
            Entity bird = birds.get(i);
            TransformComponent birdTransform = ComponentMappers.transformMapper.get(bird);

            for (int j = 0; j < pipes.size(); j++) {
                Entity pipe = pipes.get(j);
                PipeComponent pipeComponent = ComponentMappers.pipeMapper.get(pipe);
                TransformComponent pipeBodyTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeBody);
                TransformComponent pipeHeadTransform = ComponentMappers.transformMapper.get(pipeComponent.pipeHead);

                if (Intersector.overlapConvexPolygons(pipeBodyTransform.bound, birdTransform.bound)
                        || Intersector.overlapConvexPolygons(pipeHeadTransform.bound, birdTransform.bound)) {
                    if (listener != null)
                        listener.birdHitPipe();
                }
            }

            for (int j = 0; j < floors.size(); j++) {
                Entity floor = floors.get(j);
                TransformComponent floorTransform= ComponentMappers.transformMapper.get(floor);
                if (Intersector.overlapConvexPolygons(floorTransform.bound, birdTransform.bound)) {
                    if (listener != null)
                        listener.birdHitFloor();
                }
            }
        }
    }
}

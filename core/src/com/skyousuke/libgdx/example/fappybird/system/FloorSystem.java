package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.skyousuke.libgdx.example.fappybird.FlappyBird;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.FloorComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class FloorSystem extends IteratingSystem {

    public FloorSystem() {
        super(Family.all(FloorComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FloorComponent floor = ComponentMappers.floorMapper.get(entity);
        TransformComponent transform = ComponentMappers.transformMapper.get(entity);
        TransformComponent cameraTransform = ComponentMappers.transformMapper.get(floor.cameraEntity);

        final float floorWidth = transform.dimension.x;
        final float cameraPositionX = cameraTransform.position.x;
        final float halfScreenWidth = FlappyBird.SCENE_WIDTH * 0.5f;

        if (transform.position.x + floorWidth < cameraPositionX - halfScreenWidth) {
            float deltaX = getEntities().size() * floorWidth - FloorComponent.POSITION_X_CORRECTION * (getEntities().size());
            transform.position.set(transform.position.x + deltaX, transform.position.y);
            transform.bound.setPosition(transform.position.x, transform.position.y);
        }
    }

    public void initFloors()  {
        ImmutableArray<Entity> floors = getEntities();
        for (int i = 0; i < floors.size(); i++) {
            Entity floor = floors.get(i);

            TransformComponent floorTransform = ComponentMappers.transformMapper.get(floor);
            float positionX = -FlappyBird.SCENE_WIDTH * 0.5f + i * floorTransform.dimension.x - FloorComponent.POSITION_X_CORRECTION * i;
            float positionY = -FlappyBird.SCENE_HEIGHT * 0.5f;
            floorTransform.position.set(positionX, positionY);
            floorTransform.bound.setPosition(positionX, positionY);
        }
    }
}

package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.skyousuke.libgdx.example.fappybird.FlappyBird;
import com.skyousuke.libgdx.example.fappybird.component.BackgroundComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class BackgroundSystem extends IteratingSystem {

    private static float floorHeight;

    public BackgroundSystem() {
        super(Family.all(BackgroundComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BackgroundComponent background = ComponentMappers.backgroundMapper.get(entity);
        TransformComponent backgroundTransform = ComponentMappers.transformMapper.get(entity);
        TransformComponent cameraTransform = ComponentMappers.transformMapper.get(background.cameraEntity);

        backgroundTransform.position.x = -FlappyBird.SCENE_WIDTH * 0.5f + cameraTransform.position.x;
        backgroundTransform.position.y = floorHeight - FlappyBird.SCENE_HEIGHT * 0.5f;
    }

    public static void setFloorHeight(float floorHeight) {
        BackgroundSystem.floorHeight = floorHeight;
    }
}

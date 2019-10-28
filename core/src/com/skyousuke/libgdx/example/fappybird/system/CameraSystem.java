package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.skyousuke.libgdx.example.fappybird.component.CameraComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class CameraSystem extends IteratingSystem {

    private boolean lockY;

    public CameraSystem() {
        this(false);
    }

    public CameraSystem(boolean lockY) {
        super(Family.all(CameraComponent.class).get());
        this.lockY = lockY;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cameraComponent = ComponentMappers.cameraMapper.get(entity);
        TransformComponent transform = ComponentMappers.transformMapper.get(entity);

        if (cameraComponent.target != null)
        {
            TransformComponent targetTransform = ComponentMappers.transformMapper.get(cameraComponent.target);

            cameraComponent.camera.position.x = targetTransform.position.x + targetTransform.origin.x;
            transform.position.x = cameraComponent.camera.position.x;

            if (!lockY)
            {
                cameraComponent.camera.position.y = targetTransform.position.y + targetTransform.origin.y;
                transform.position.y = cameraComponent.camera.position.y;
            }
        }
        else
        {
            cameraComponent.camera.position.x = transform.position.x;
            cameraComponent.camera.position.y = transform.position.y;
        }
        cameraComponent.camera.update();
    }

    public void setLockY(boolean lockY) {
        this.lockY = lockY;
    }
}

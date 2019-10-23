package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyousuke.libgdx.example.fappybird.CameraHelper;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;
import com.skyousuke.libgdx.example.fappybird.component.VisualComponent;

// TODO: Don't forget to dispose Batch!!!
public class RenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);

    private CameraHelper cameraHelper;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public RenderSystem(CameraHelper cameraHelper, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, VisualComponent.class).get());
        this.cameraHelper = cameraHelper;
        this.camera = camera;

        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = transformMapper.get(entity);
        VisualComponent visual = visualMapper.get(entity);

        if (visual.region == null) return;

        batch.draw(visual.region,
                transform.position.x, transform.position.y,
                transform.origin.x, transform.origin.y,
                transform.dimension.x, transform.dimension.y,
                transform.scale.x, transform.scale.y,
                transform.rotation);
    }
}

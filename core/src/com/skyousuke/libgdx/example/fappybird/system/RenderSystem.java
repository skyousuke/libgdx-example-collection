package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyousuke.libgdx.example.fappybird.FlappyBird;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;
import com.skyousuke.libgdx.example.fappybird.component.VisualComponent;

import java.util.Comparator;

public class RenderSystem extends EntitySystem implements Disposable, EntityListener {
    private static Comparator<Entity> comparator = new RenderSystemComparator();

    private Family family;
    private Array<Entity> sortedEntities;

    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private boolean debugMode;

    public RenderSystem() {
        this.family = (Family.all(VisualComponent.class).get());
        sortedEntities = new Array<>(false, 16);

        camera = new OrthographicCamera();
        viewport = new FitViewport(FlappyBird.SCENE_WIDTH, FlappyBird.SCENE_HEIGHT, camera);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        sortedEntities.sort(comparator);

        batch.begin();
        for (int i = 0; i < sortedEntities.size; i++) {
            Entity entity = sortedEntities.get(i);
            TransformComponent transform = ComponentMappers.transformMapper.get(entity);
            VisualComponent visual = ComponentMappers.visualMapper.get(entity);

            if (visual.region == null) return;

            batch.draw(visual.region,
                    transform.position.x, transform.position.y,
                    transform.origin.x, transform.origin.y,
                    transform.dimension.x, transform.dimension.y,
                    transform.scale.x, transform.scale.y,
                    transform.rotation);
        }
        batch.end();

        if (debugMode)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < sortedEntities.size; i++) {
                Entity entity = sortedEntities.get(i);
                TransformComponent transform = ComponentMappers.transformMapper.get(entity);
                float[] boundVertices = transform.bound.getTransformedVertices();
                if (boundVertices.length >= 6)
                    shapeRenderer.polygon(boundVertices);
            }
            shapeRenderer.end();
        }
    }

    @Override
    public void addedToEngine (Engine engine) {
        ImmutableArray<Entity> newEntities = engine.getEntitiesFor(family);
        sortedEntities.clear();
        for (int i = 0; i < newEntities.size(); ++i) {
            sortedEntities.add(newEntities.get(i));
        }
        engine.addEntityListener(family, this);
    }

    @Override
    public void removedFromEngine (Engine engine) {
        sortedEntities.clear();
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        sortedEntities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        sortedEntities.removeValue(entity, true);
    }


    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }

    public Viewport getViewport()
    {
        return viewport;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    private static class RenderSystemComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entity1, Entity entity2) {
            VisualComponent visual1 = ComponentMappers.visualMapper.get(entity1);
            VisualComponent visual2 = ComponentMappers.visualMapper.get(entity2);
            return Integer.compare(visual1.zIndex, visual2.zIndex);
        }
    }
}

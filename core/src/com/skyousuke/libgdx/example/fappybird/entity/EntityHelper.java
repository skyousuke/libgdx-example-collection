package com.skyousuke.libgdx.example.fappybird.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.skyousuke.libgdx.example.fappybird.Assets;
import com.skyousuke.libgdx.example.fappybird.component.BackgroundComponent;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.CameraComponent;
import com.skyousuke.libgdx.example.fappybird.component.FloorComponent;
import com.skyousuke.libgdx.example.fappybird.component.MovingComponent;
import com.skyousuke.libgdx.example.fappybird.component.PipeComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;
import com.skyousuke.libgdx.example.fappybird.component.VisualComponent;

public class EntityHelper {

    public static Entity newBird() {
        Entity entity = new Entity();

        VisualComponent visual = new VisualComponent();
        visual.region = Assets.instance.image.findRegion("bird", 0);
        visual.zIndex = 1;
        entity.add(visual);

        TransformComponent transform = new TransformComponent();
        float width = visual.region.getRegionWidth();
        float height = visual.region.getRegionHeight();
        transform.dimension.set(width, height);
        transform.origin.set(width * 0.5f, height * 0.5f);
        transform.scale.set(1, 1);
        transform.bound.setVertices(new float[]{0, 0, 0, height, width, height, width, 0});
        transform.bound.setOrigin(transform.origin.x, transform.origin.y);
        entity.add(transform);

        BirdComponent bird = new BirdComponent();
        Array<TextureRegion> flyAnimation = new Array<>();
        flyAnimation.add(Assets.instance.image.findRegion("bird", 1));
        flyAnimation.add(Assets.instance.image.findRegion("bird", 2));
        bird.animation = new Animation<>(BirdComponent.FRAME_DURATION, flyAnimation, Animation.PlayMode.NORMAL);
        bird.standbyPhase = true;
        entity.add(bird);

        MovingComponent movingComponent = new MovingComponent();
        movingComponent.velocity.set(BirdComponent.SPEED_X, BirdComponent.STANDBY_SPEED_Y);
        entity.add(movingComponent);

        return entity;
    }

    public static Entity newCamera(OrthographicCamera camera) {
        Entity entity = new Entity();

        TransformComponent transform = new TransformComponent();
        entity.add(transform);

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = camera;
        entity.add(cameraComponent);

        return entity;
    }

    public static Entity newBackground(Entity cameraEntity) {
        Entity entity = new Entity();

        VisualComponent visual = new VisualComponent();
        visual.region = Assets.instance.image.findRegion("background");
        entity.add(visual);

        TransformComponent transform = new TransformComponent();
        float width = visual.region.getRegionWidth();
        float height = visual.region.getRegionHeight();
        transform.dimension.set(width, height);
        transform.origin.set(width * 0.5f, height * 0.5f);
        transform.scale.set(1, 1);
        entity.add(transform);

        BackgroundComponent background = new BackgroundComponent();
        background.cameraEntity = cameraEntity;
        entity.add(background);

        return entity;
    }

    public static Entity newFloor(Entity cameraEntity) {
        Entity entity = new Entity();

        VisualComponent visual = new VisualComponent();
        visual.region = Assets.instance.image.findRegion("floor");
        entity.add(visual);

        TransformComponent transform = new TransformComponent();
        float width = visual.region.getRegionWidth();
        float height = visual.region.getRegionHeight();
        transform.dimension.set(width, height);
        transform.origin.set(width * 0.5f, height * 0.5f);
        transform.bound.setVertices(new float[]{0, 0, 0, height, width, height, width, 0});
        entity.add(transform);

        FloorComponent floorComponent = new FloorComponent();
        floorComponent.cameraEntity = cameraEntity;
        entity.add(floorComponent);

        return entity;
    }

    public static Entity newPipeHead() {
        Entity entity = new Entity();

        VisualComponent visual = new VisualComponent();
        visual.region = Assets.instance.image.findRegion("pipeHead");
        entity.add(visual);

        TransformComponent transform = new TransformComponent();
        float width = visual.region.getRegionWidth();
        float height = visual.region.getRegionHeight();
        transform.dimension.set(width, height);
        transform.origin.set(width * 0.5f, height * 0.5f);
        entity.add(transform);

        return entity;
    }

    public static Entity newPipeBody() {
        Entity entity = new Entity();

        VisualComponent visual = new VisualComponent();
        visual.region = Assets.instance.image.findRegion("pipeBody");
        entity.add(visual);

        TransformComponent transform = new TransformComponent();
        float width = visual.region.getRegionWidth();
        float height = visual.region.getRegionHeight();
        transform.dimension.set(width, height);
        transform.origin.set(width * 0.5f, height * 0.5f);
        transform.bound.setVertices(new float[]{0, 0, 0, height, width, height, width, 0});
        entity.add(transform);

        return entity;
    }

    public static Entity newPipe(Entity pipeBody, Entity pipeHead, Entity camera) {
        Entity entity = new Entity();

        TransformComponent transform = new TransformComponent();
        entity.add(transform);

        PipeComponent pipe = new PipeComponent();
        pipe.pipeBody = pipeBody;
        pipe.pipeHead = pipeHead;
        pipe.camera = camera;
        entity.add(pipe);

        return entity;
    }
}

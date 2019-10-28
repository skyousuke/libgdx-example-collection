package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
    public static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<MovingComponent> movingMapper = ComponentMapper.getFor(MovingComponent.class);
    public static final ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);
    public static final ComponentMapper<CameraComponent> cameraMapper = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<BackgroundComponent> backgroundMapper = ComponentMapper.getFor(BackgroundComponent.class);
    public static final ComponentMapper<FloorComponent> floorMapper = ComponentMapper.getFor(FloorComponent.class);
    public static final ComponentMapper<PipeComponent> pipeMapper = ComponentMapper.getFor(PipeComponent.class);

    public static final ComponentMapper<BirdComponent> birdMapper = ComponentMapper.getFor(BirdComponent.class);
}

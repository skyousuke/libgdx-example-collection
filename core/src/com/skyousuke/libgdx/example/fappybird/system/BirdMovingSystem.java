package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.MovingComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;

public class BirdMovingSystem extends IteratingSystem {

    private static final float SPEED_LIMIT_X = 1000;
    private static final float SPEED_LIMIT_Y = 420;

    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<MovingComponent> movingMapper = ComponentMapper.getFor(MovingComponent.class);
//    private ComponentMapper<BirdComponent> birdMapper = ComponentMapper.getFor(BirdComponent.class);

    public BirdMovingSystem() {
        super(Family.all(TransformComponent.class, MovingComponent.class, BirdComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = transformMapper.get(entity);
        MovingComponent moving = movingMapper.get(entity);
//        BirdComponent bird = birdMapper.get(entity);

        if (Float.compare(moving.velocity.x, 0) != 0) {
            setPositionX(transform.position.x + moving.velocity.x * deltaTime, transform.position, transform.bound);
        }
        if (Float.compare(moving.velocity.y, 0) != 0) {
            setPositionY(transform.position.y + moving.velocity.y * deltaTime, transform.position, transform.bound);
        }

        if (Float.compare(moving.acceleration.x, 0) != 0) {
            moving.velocity.x += moving.acceleration.x * deltaTime;
        }
        if (Float.compare(moving.acceleration.y, 0) != 0) {
            moving.velocity.y += moving.acceleration.y * deltaTime;
        }

        moving.velocity.x = MathUtils.clamp(moving.velocity.x, -SPEED_LIMIT_X, SPEED_LIMIT_X);
        moving.velocity.y = MathUtils.clamp(moving.velocity.y, -SPEED_LIMIT_Y, SPEED_LIMIT_Y);
    }

    private void setPositionX(float x, Vector2 position, Polygon bound) {
        setPosition(x, position.y, position, bound);
    }

    private void setPositionY(float y, Vector2 position, Polygon bound) {
        setPosition(position.x, y, position, bound);
    }

    private void setPosition(float x, float y, Vector2 position, Polygon bound) {
        position.x = x;
        position.y = y;
        bound.setPosition(position.x, position.y);
    }
}

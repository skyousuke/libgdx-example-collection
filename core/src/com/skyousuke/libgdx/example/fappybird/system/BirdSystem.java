package com.skyousuke.libgdx.example.fappybird.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.skyousuke.libgdx.example.fappybird.Assets;
import com.skyousuke.libgdx.example.fappybird.component.BirdComponent;
import com.skyousuke.libgdx.example.fappybird.component.ComponentMappers;
import com.skyousuke.libgdx.example.fappybird.component.MovingComponent;
import com.skyousuke.libgdx.example.fappybird.component.TransformComponent;
import com.skyousuke.libgdx.example.fappybird.component.VisualComponent;

public class BirdSystem extends IteratingSystem {

    public BirdSystem() {
        super(Family.all(BirdComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BirdComponent bird = ComponentMappers.birdMapper.get(entity);
        TransformComponent transform = ComponentMappers.transformMapper.get(entity);
        MovingComponent movingComponent = ComponentMappers.movingMapper.get(entity);
        VisualComponent visual = ComponentMappers.visualMapper.get(entity);

        if (bird.standbyPhase) {
            visual.region = Assets.instance.image.findRegion("bird", 0);
            float velocityY = MathUtils.sin(bird.standbyTime * 10) * BirdComponent.STANDBY_SPEED_Y;
            movingComponent.velocity.set(movingComponent.velocity.x, velocityY);
            bird.standbyTime += deltaTime;
        } else {
            // Update animation & rotation
            if (bird.playAnimation) {
                if (bird.animation.isAnimationFinished(bird.animationTime)) {
                    bird.animationTime = 0;
                    bird.flyCount++;
                    if (bird.flyCount == BirdComponent.MAX_FLY_COUNT) {
                        bird.playAnimation = false;
                    }
                }
                bird.animationTime += deltaTime;
                visual.region = bird.animation.getKeyFrame(bird.animationTime);
            } else {
                visual.region = Assets.instance.image.findRegion("bird", 0);
                float newRotation = transform.rotation - BirdComponent.ROTATION_SPEED * deltaTime;
                if (newRotation <= BirdComponent.MIN_ROTATION) {
                    newRotation = BirdComponent.MIN_ROTATION;
                }
                transform.rotation = newRotation;
                transform.bound.setRotation(newRotation);
            }
            // Apply fly acceleration
            bird.flyAcceleration -= deltaTime * BirdComponent.FLY_ACCELERATION_DECREASE_RATE;
            if (bird.flyAcceleration < 0) {
                bird.flyAcceleration = 0;
            }
            float newAcceleration =  movingComponent.acceleration.y - bird.oldFlyAcceleration + bird.flyAcceleration;
            movingComponent.acceleration.set(movingComponent.acceleration.x, newAcceleration);
            bird.oldFlyAcceleration = bird.flyAcceleration;
        }
    }

    public static void flyUp(Entity bird) {
        BirdComponent birdComponent = ComponentMappers.birdMapper.get(bird);
        MovingComponent movingComponent = ComponentMappers.movingMapper.get(bird);
        TransformComponent transform = ComponentMappers.transformMapper.get(bird);

        if (birdComponent.dead || birdComponent.standbyPhase)
            return;

        movingComponent.velocity.y = 0;
        birdComponent.flyAcceleration = BirdComponent.INITIAL_FLY_ACCELERATION;
        birdComponent.playAnimation = true;
        birdComponent.flyCount = 0;
        transform.rotation = BirdComponent.FLY_ROTATION;
        transform.bound.setRotation(transform.rotation);
    }


    public static void start(Entity bird) {
        BirdComponent birdComponent = ComponentMappers.birdMapper.get(bird);
        MovingComponent movingComponent = ComponentMappers.movingMapper.get(bird);

        movingComponent.acceleration.set(0, BirdComponent.GRAVITY_ACCELERATION);
        birdComponent.standbyPhase = false;
        flyUp(bird);
    }
}

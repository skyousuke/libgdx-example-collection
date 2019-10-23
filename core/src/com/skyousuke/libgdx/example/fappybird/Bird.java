/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skyousuke.libgdx.example.fappybird;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class Bird extends AbstractGameObject {

    private static final float FRAME_DURATION = 0.08f;
    private static final int MAX_FLY_COUNT = 4;

    private static final float SPEED_X = 200f;
    private static final float STANDBY_SPEED_Y = 80f;
    private static final float INITIAL_FLY_ACCELERATION = 10000f;
    private static final float FLY_ACCELERATION_DECREASE_RATE = 40000f;
    private static final float ROTATION_SPEED = 400f;
    private static final float FLY_ROTATION = 20;
    private static final float MIN_ROTATION = -90;
    private static final float GRAVITY_ACCELERATION = -3000;

    private final BirdListener listener;

    private Animation<TextureRegion> animation;
    private float animationTime;
    private boolean playAnimation;
    private int flyCount;

    private float oldFlyAcceleration;
    private float flyAcceleration;
    private TextureRegion currentFrame;

    private boolean dead;

    private boolean standbyPhase;
    private float standbyTime;

    public Bird(BirdListener listener) {
        this.listener = listener;

        TextureRegion birdRegion = Assets.instance.image.findRegion("bird", 0);
        final float width = birdRegion.getRegionWidth();
        final float height = birdRegion.getRegionHeight();

        setDimension(width, height);
        setBound(new float[]{0, 0, 0, height, width, height, width, 0});

        Array<TextureRegion> flyAnimation = new Array<>();
        flyAnimation.add(Assets.instance.image.findRegion("bird", 1));
        flyAnimation.add(Assets.instance.image.findRegion("bird", 2));

        animation = new Animation<>(FRAME_DURATION, flyAnimation, Animation.PlayMode.NORMAL);

        standbyPhase = true;
        setVelocity(SPEED_X, STANDBY_SPEED_Y);
    }

    public void start() {
        setAcceleration(0, GRAVITY_ACCELERATION);
        standbyPhase = false;
        flyUp();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (standbyPhase) {
            currentFrame = Assets.instance.image.findRegion("bird", 0);
            float velocityY = MathUtils.sin(standbyTime * 10) * STANDBY_SPEED_Y;
            setVelocity(getVelocity().x, velocityY);
            standbyTime += deltaTime;
        } else {
            // Update animation & rotation
            if (playAnimation) {
                if (animation.isAnimationFinished(animationTime)) {
                    animationTime = 0;
                    flyCount++;
                    if (flyCount == MAX_FLY_COUNT) {
                        playAnimation = false;
                    }
                }
                animationTime += deltaTime;
                currentFrame = animation.getKeyFrame(animationTime);
            } else {
                currentFrame = Assets.instance.image.findRegion("bird", 0);
                float newRotation = getRotation() - ROTATION_SPEED * deltaTime;
                if (newRotation <= MIN_ROTATION) {
                    newRotation = MIN_ROTATION;
                }
                setRotation(newRotation);
            }
            // Apply fly acceleration
            flyAcceleration -= deltaTime * FLY_ACCELERATION_DECREASE_RATE;
            if (flyAcceleration < 0) {
                flyAcceleration = 0;
            }
            setAcceleration(getAcceleration().x, getAcceleration().y - oldFlyAcceleration);
            setAcceleration(getAcceleration().x, getAcceleration().y + flyAcceleration);
            oldFlyAcceleration = flyAcceleration;
        }
    }

    public void flyUp() {
        if (dead || standbyPhase)
            return;

        setVelocity(getVelocity().x, 0);
        flyAcceleration = INITIAL_FLY_ACCELERATION;
        playAnimation = true;
        flyCount = 0;
        setRotation(FLY_ROTATION);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentFrame,
                getPosition().x, getPosition().y, getOrigin().x, getOrigin().y,
                getDimension().x, getDimension().y,
                getScale().x, getScale().y, getRotation());
    }

    public void die() {
        dead = true;
    }

    public void hitPipe() {
        die();
        setVelocity(0, 0);
        oldFlyAcceleration = 0;
        flyAcceleration = 0;
        playAnimation = false;
        setAcceleration(0, GRAVITY_ACCELERATION);
        listener.birdHitPipe();
    }

    public void hitFloor() {
        die();
        setVelocity(0, 0);
        oldFlyAcceleration = 0;
        flyAcceleration = 0;
        playAnimation = false;
        setAcceleration(0, 0);
        listener.birdHitFloor();
    }

    public void passOverPipe() {
        listener.birdPassedOverPipe();
    }
}

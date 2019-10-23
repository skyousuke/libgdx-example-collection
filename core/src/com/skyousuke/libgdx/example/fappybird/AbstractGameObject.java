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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;


public abstract class AbstractGameObject {

    private static final float SPEED_LIMIT_X = 1000;
    private static final float SPEED_LIMIT_Y = 420;

    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 acceleration;
    private final Vector2 origin;
    private final Vector2 scale;
    private final Vector2 dimension;
    private final Polygon bound;
    private float rotation;

    public AbstractGameObject() {
        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2();

        origin = new Vector2();
        scale = new Vector2(1, 1);
        dimension = new Vector2();
        bound = new Polygon();
    }

    protected void setDimension(float width, float height) {
        dimension.set(width, height);
        setOrigin(width / 2, height / 2);
    }

    public void update(float deltaTime) {
        if (Float.compare(velocity.x, 0) != 0) {
            setPositionX(position.x + velocity.x * deltaTime);
        }
        if (Float.compare(velocity.y, 0) != 0) {
            setPositionY(position.y + velocity.y * deltaTime);
        }

        if (Float.compare(acceleration.x, 0) != 0) {
            velocity.x += acceleration.x * deltaTime;
        }
        if (Float.compare(acceleration.y, 0) != 0) {
            velocity.y += acceleration.y * deltaTime;
        }

        velocity.x = MathUtils.clamp(velocity.x, -SPEED_LIMIT_X, SPEED_LIMIT_X);
        velocity.y = MathUtils.clamp(velocity.y, -SPEED_LIMIT_Y, SPEED_LIMIT_Y);
    }

    public abstract void render(SpriteBatch batch);

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;

        bound.setPosition(position.x, position.y);
    }

    public void setPositionX(float x) {
        setPosition(x, position.y);
    }

    public void setPositionY(float y) {
        setPosition(position.x, y);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float x, float y) {
        acceleration.x = x;
        acceleration.y = y;
    }

    public Vector2 getScale() {
        return scale;
    }

    public Polygon getBound() {
        return bound;
    }

    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.polygon(bound.getTransformedVertices());
    }

    public boolean overlaps(AbstractGameObject object) {
        return Intersector.overlapConvexPolygons(bound, object.getBound());
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        bound.setRotation(rotation);
    }

    public void setOrigin(float x, float y) {
        origin.x = x;
        origin.y = y;
        bound.setOrigin(x, y);
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public Vector2 getDimension() {
        return dimension;
    }

    public void setBound(float[] vertices) {
        bound.setVertices(vertices);
    }
}

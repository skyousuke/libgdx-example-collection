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
import com.badlogic.gdx.math.Vector2;

public class ScrollingFloor {

    private static final int FLOOR_COUNT = 5;
    private static final float POSITION_CORRECTION = 1;

    private Floor[] floors;

    public ScrollingFloor() {
        floors = new Floor[FLOOR_COUNT];

        for (int i = 0; i < floors.length; i++) {
            floors[i] = new Floor();
            floors[i].setPosition(
                    -FlappyBird.SCENE_WIDTH / 2 + i * floors[i].getDimension().x - POSITION_CORRECTION * i,
                    -FlappyBird.SCENE_HEIGHT / 2);
        }
    }

    public void update(CameraHelper cameraHelper) {
        for (Floor floor : floors) {
            final Vector2 floorPosition = floor.getPosition();
            final float floorWidth = floor.getDimension().x;
            final float cameraPositionX = cameraHelper.getPosition().x;
            final float halfScreenWidth = FlappyBird.SCENE_WIDTH / 2;

            if (floorPosition.x + floorWidth <
                    cameraPositionX - halfScreenWidth) {
                float deltaX = (FLOOR_COUNT) * floorWidth - POSITION_CORRECTION * (FLOOR_COUNT);
                floor.setPosition(floorPosition.x + deltaX, floorPosition.y);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Floor floor : floors) {
            floor.render(batch);
        }
    }

    public boolean overlaps(AbstractGameObject object) {
        for (Floor floor : floors) {
            if (floor.overlaps(object))
                return true;
        }
        return false;
    }

    public void debug(ShapeRenderer shapeRenderer) {
        for (Floor floor : floors) {
            floor.debug(shapeRenderer);
        }

    }
}

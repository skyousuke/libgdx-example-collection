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
import com.badlogic.gdx.utils.Array;

public class ScrollingPipe {

    private static final int DOUBLE_PIPE_COUNT = 3;

    private static final int DISTANCE_BETWEEN_DOUBLE_PIPES = 250;

    private Array<DoublePipe> doublePipes;

    public ScrollingPipe() {
        doublePipes = new Array<>();
    }

    public void update(CameraHelper cameraHelper, Bird bird) {
        for (DoublePipe doublePipe : doublePipes) {
            if (doublePipe.outOfScene(cameraHelper)) {
                doublePipe.init(doublePipe.getPositionX() + DISTANCE_BETWEEN_DOUBLE_PIPES * DOUBLE_PIPE_COUNT);
                break;
            }
        }
        for (DoublePipe doublePipe : doublePipes) {
            if (!doublePipe.isBirdPassedOver() && doublePipe.getPositionX() < bird.getPosition().x) {
                doublePipe.birdPassOver();
                bird.passOverPipe();
                break;
            }
        }
    }

    public void startGeneratePipe(float startPositionX) {
        for (int i = 0; i < DOUBLE_PIPE_COUNT; i++) {
            DoublePipe doublePipe = new DoublePipe();
            doublePipe.init(startPositionX + i * DISTANCE_BETWEEN_DOUBLE_PIPES);
            doublePipes.add(doublePipe);
        }
    }

    public void render(SpriteBatch batch) {
        for (DoublePipe doublePipe : doublePipes) {
            doublePipe.render(batch);
        }
    }

    public void debug(ShapeRenderer shapeRenderer) {
        for (DoublePipe doublePipe : doublePipes) {
            doublePipe.debug(shapeRenderer);
        }
    }

    public boolean overlaps(AbstractGameObject object) {
        for (DoublePipe doublePipe : doublePipes) {
            if (doublePipe.overlaps(object)) {
                return true;
            }
        }
        return false;
    }

}

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
import com.badlogic.gdx.math.MathUtils;

public class DoublePipe {

    private static final int RANDOM_MARGIN = 40;
    private static final int SPACE_HEIGHT = 180;
    private static final int REMAINING_HEIGHT =
            FlappyBird.SCENE_HEIGHT - SPACE_HEIGHT - PipeHead.HEIGHT * 2 - Floor.HEIGHT;

    private Pipe lowerPipe;
    private Pipe upperPipe;

    private boolean birdPassedOver;

    public DoublePipe() {
        lowerPipe = new Pipe();
        upperPipe = new Pipe(true);
    }

    public void init(float positionX) {
        randomHeight();
        setPositionX(positionX);
        birdPassedOver = false;
    }

    public void randomHeight() {
        final int pipeBodyHeight = MathUtils.random(RANDOM_MARGIN, REMAINING_HEIGHT - RANDOM_MARGIN);

        lowerPipe.setBodyHeight(pipeBodyHeight);
        upperPipe.setBodyHeight(REMAINING_HEIGHT - pipeBodyHeight);
    }

    public void setPositionX(float x) {
        lowerPipe.setPositionX(x);
        upperPipe.setPositionX(x);
    }

    public float getPositionX() {
        return lowerPipe.getPositionX();
    }

    public void render(SpriteBatch batch) {
        lowerPipe.render(batch);
        upperPipe.render(batch);
    }

    public boolean outOfScene(CameraHelper cameraHelper) {
        return lowerPipe.outOfScene(cameraHelper);
    }

    public void debug(ShapeRenderer shapeRenderer) {
        lowerPipe.debug(shapeRenderer);
        upperPipe.debug(shapeRenderer);
    }

    public boolean overlaps(AbstractGameObject object) {
        return lowerPipe.overlaps(object) || upperPipe.overlaps(object);
    }

    public boolean isBirdPassedOver() {
        return birdPassedOver;
    }

    public void birdPassOver() {
        birdPassedOver = true;
    }
}


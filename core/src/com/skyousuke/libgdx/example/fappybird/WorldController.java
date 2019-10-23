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

import com.badlogic.gdx.Gdx;


public class WorldController implements BirdListener {

    private CameraHelper cameraHelper;
    private ScrollingFloor scrollingFloor;
    private Background background;
    private ScrollingPipe scrollingPipe;
    private Bird bird;

    private boolean birdHitPipe;
    private boolean gameStart;
    private boolean gameOver;

    private WorldListener listener;
    private int score;

    public WorldController() {
        init();
    }

    private void init() {
        cameraHelper = new CameraHelper();
        scrollingFloor = new ScrollingFloor();
        background = new Background();
        scrollingPipe = new ScrollingPipe();
        bird = new Bird(this);

        cameraHelper.setTarget(bird);

        birdHitPipe = false;
        gameStart = false;
        gameOver = false;

        setScore(0);

        if (listener != null)
            listener.gameRestart();
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            if (!gameStart) {
                startGame();
            } else if (gameOver) {
                init();
            } else {
                bird.flyUp();
            }
        }
    }

    private void startGame() {
        bird.start();
        scrollingPipe.startGeneratePipe(bird.getPosition().x + 500);
        gameStart = true;
        if (listener != null)
            listener.gameStart();
    }

    public void update(float deltaTime) {
        handleInput();

        bird.update(deltaTime);
        cameraHelper.update();

        scrollingPipe.update(cameraHelper, bird);
        background.update(cameraHelper);
        scrollingFloor.update(cameraHelper);

        if (!gameOver) {
            if (scrollingFloor.overlaps(bird)) {
                bird.hitFloor();
            } else if (!birdHitPipe && scrollingPipe.overlaps(bird)) {
                bird.hitPipe();
            }
        }
    }

    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    public ScrollingFloor getScrollingFloor() {
        return scrollingFloor;
    }

    public Background getBackground() {
        return background;
    }

    public Bird getBird() {
        return bird;
    }

    @Override
    public void birdHitPipe() {
        birdHitPipe = true;
    }

    @Override
    public void birdHitFloor() {
        gameOver = true;
    }

    public void setListener(WorldListener listener) {
        this.listener = listener;
    }

    public ScrollingPipe getScrollingPipe() {
        return scrollingPipe;
    }

    private void setScore(int score) {
        this.score = score;
        if (listener != null)
            listener.scoreUpdate(score);
    }

    @Override
    public void birdPassedOverPipe() {
        setScore(score + 1);
    }

}

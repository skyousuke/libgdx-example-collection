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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class WorldRenderer implements Disposable {

    private WorldController worldController;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;

        camera = new OrthographicCamera();
        viewport = new FitViewport(FlappyBird.SCENE_WIDTH, FlappyBird.SCENE_HEIGHT, camera);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
    }

    public void render() {
        worldController.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        worldController.getBackground().render(batch);
        worldController.getScrollingFloor().render(batch);
        worldController.getScrollingPipe().render(batch);
        worldController.getBird().render(batch);
        batch.end();
    }

    public void debug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        worldController.getBird().debug(shapeRenderer);
        worldController.getScrollingFloor().debug(shapeRenderer);
        worldController.getScrollingPipe().debug(shapeRenderer);
        shapeRenderer.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}

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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

    private WorldRenderer worldRenderer;
    private WorldController worldController;
    private Hud hud;

    private boolean debug;

    public GameScreen() {
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        hud = new Hud();

        worldController.setListener(hud);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
            debug = !debug;

        worldController.update(delta);
        worldRenderer.render();
        if (debug)
            worldRenderer.debug();

        hud.update(delta);
        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        hud.dispose();
    }

}

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
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class PipeHead extends AbstractGameObject {

    public static final int HEIGHT;

    private static final int WIDTH;
    private static final TextureRegion region;

    static {
        region = Assets.instance.image.findRegion("pipeHead");
        WIDTH = region.getRegionWidth();
        HEIGHT = region.getRegionHeight();
    }

    public PipeHead() {
        setDimension(WIDTH, HEIGHT);
        setBound(new float[]{0, 0, 0, HEIGHT, WIDTH, HEIGHT, WIDTH, 0});
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(region,
                getPosition().x, getPosition().y, getOrigin().x, getOrigin().y,
                getDimension().x, getDimension().y,
                getScale().x, getScale().y, getRotation());
    }
}

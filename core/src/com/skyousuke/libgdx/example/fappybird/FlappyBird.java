package com.skyousuke.libgdx.example.fappybird;

import com.skyousuke.libgdx.example.GdxExample;

public class FlappyBird extends GdxExample {

    public static final int SCENE_HEIGHT = 640;
    public static final int SCENE_WIDTH = 360;

    @Override
    public void create() {
        Assets.instance.init();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}

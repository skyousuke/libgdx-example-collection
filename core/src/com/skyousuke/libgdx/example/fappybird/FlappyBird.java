package com.skyousuke.libgdx.example.fappybird;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.skyousuke.libgdx.example.GdxExample;

public class FlappyBird extends GdxExample {

    public static final int SCENE_HEIGHT = 640;
    public static final int SCENE_WIDTH = 360;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Assets.instance.init();
//        setScreen(new GameScreen());
        setScreen(new GameScreenAshley());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}

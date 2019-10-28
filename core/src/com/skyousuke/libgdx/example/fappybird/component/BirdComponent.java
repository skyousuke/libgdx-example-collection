package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BirdComponent implements Component {

    public static final float FRAME_DURATION = 0.08f;
    public static final int MAX_FLY_COUNT = 4;

    public static final float SPEED_X = 200f;
    public static final float STANDBY_SPEED_Y = 80f;
    public static final float INITIAL_FLY_ACCELERATION = 10000f;
    public static final float FLY_ACCELERATION_DECREASE_RATE = 40000f;
    public static final float ROTATION_SPEED = 400f;
    public static final float FLY_ROTATION = 20;
    public static final float MIN_ROTATION = -90;
    public static final float GRAVITY_ACCELERATION = -3000;

    public Animation<TextureRegion> animation;
    public float animationTime;
    public boolean playAnimation;
    public int flyCount;

    public float oldFlyAcceleration;
    public float flyAcceleration;

    public boolean dead;

    public boolean standbyPhase;
    public float standbyTime;
}

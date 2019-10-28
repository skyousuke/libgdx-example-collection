package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class PipeComponent implements Component {
    public static final int HALF_WIDTH_DIFFERENCE = 6;
    public static final int HEAD_HEIGHT = 36;

    public static final int DOUBLE_PIPE_COUNT = 3;
    public static final int DISTANCE_BETWEEN_DOUBLE_PIPES = 250;

    public static final int MINIMUM_PIPE_HEIGHT = 50;
    public static final int SPACE_HEIGHT = 160;

    public static final int START_PIPE_DISTANCE = 500;

    public Entity camera;
    public Entity pipeBody;
    public Entity pipeHead;
    public boolean invert;

    public boolean birdPassedOver;
}

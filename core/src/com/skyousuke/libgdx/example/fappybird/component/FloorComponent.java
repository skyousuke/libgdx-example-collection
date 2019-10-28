package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class FloorComponent implements Component {
    public static final float POSITION_X_CORRECTION = 1;

    public Entity cameraEntity;
}

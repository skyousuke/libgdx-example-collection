package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovingComponent implements Component {

    public Vector2 velocity = new Vector2(0, 0);
    public Vector2 acceleration = new Vector2(0, 0);

}

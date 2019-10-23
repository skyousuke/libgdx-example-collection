package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {

    public Vector2 position = new Vector2(0, 0);
    public Vector2 origin = new Vector2(0, 0);
    public Vector2 scale = new Vector2(0, 0);
    public Vector2 dimension = new Vector2(0, 0);
    public Polygon bound = new Polygon();
    public float rotation = 0f;

}

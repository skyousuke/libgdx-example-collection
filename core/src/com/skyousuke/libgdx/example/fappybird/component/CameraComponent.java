package com.skyousuke.libgdx.example.fappybird.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraComponent implements Component {

    public Entity target;
    public OrthographicCamera camera;

}

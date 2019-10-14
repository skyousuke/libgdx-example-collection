package com.skyousuke.libgdx.example;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.skyousuke.libgdx.example.fappybird.FlappyBird;
import com.skyousuke.libgdx.example.shadertest.ShaderTest;

public class GdxExamples {
    private static final Array<Class<? extends GdxExample>> examples;
    static {
        examples = new Array<>();
        examples.addAll(
                FlappyBird.class,
                ShaderTest.class
        );
    }

    public static Array<String> getNames () {
        Array<String> names = new Array<>(examples.size);
        for (Class clazz : examples)
            names.add(clazz.getSimpleName());
        names.sort();
        return names;
    }

    private static Class<? extends GdxExample> forName (String name) {
        for (Class clazz : examples)
            if (clazz.getSimpleName().equals(name)) return clazz;
        return null;
    }

    public static GdxExample newSample (String testName) {
        try {
            return ClassReflection.newInstance(forName(testName));
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

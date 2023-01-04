package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

public class Sun {
    private static final float MIDNIGHT = 0.5f; //todo:change
    private static final int SUN_SIZE = 100;
    private static final String tagName = "sun";

    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky){
        return Vector2.ZERO; //todo: change
//        return sun.setCenter();

    }
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        Renderable sunCircle = new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW));
        GameObject sun = new GameObject(
                Vector2.ZERO,
                new Vector2(SUN_SIZE, SUN_SIZE),
                sunCircle
        );
        sun.setCenter(new Vector2(windowDimensions.x() / 2, 100));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(tagName);
//        Transition<Float> transition = new Transition<>(
//                sun,
//                sun.renderer()::setOpaqueness /**todo: change */,
//                0f,
//                MIDNIGHT,
//                Transition.LINEAR_INTERPOLATOR_FLOAT,
//                cycleLength,
//                Transition.TransitionType.TRANSITION_LOOP,
//                null);
        return sun;
    }
}

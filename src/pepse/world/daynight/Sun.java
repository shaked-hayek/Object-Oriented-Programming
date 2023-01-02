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


    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky){
        return Vector2.ZERO; //todo: change

    }
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        Renderable sunCircle = new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW));
        GameObject sun = new GameObject(Vector2.ZERO,new Vector2(100,100), sunCircle);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag("sun");
//        Transition<Float> transition = new Transition<>(sun,
//                sun.renderer()::setOpaqueness /**todo: change */,
//                0f, MIDNIGHT, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
//                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return sun;
    }
}

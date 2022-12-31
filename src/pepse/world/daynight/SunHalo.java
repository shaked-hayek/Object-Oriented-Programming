package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

public class SunHalo {
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        Renderable haloCircle = new OvalRenderable(ColorSupplier.approximateColor(color));
        GameObject halo = new GameObject(Vector2.ZERO,sun.getDimensions(), haloCircle);
        gameObjects.addGameObject(halo, layer);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun halo");
//        Transition<Float> transition = new Transition<>(sun,
//                sun.renderer()::setOpaqueness /**todo: change */,
//                0f, MIDNIGHT, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
//                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return sun;
    }

    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);

    }
}

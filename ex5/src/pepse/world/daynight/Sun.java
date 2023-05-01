package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.Color;

public class Sun {
    /**
     * size of sun object
     */
    private static final int SUN_SIZE = 100;
    /**
     * finale sun angle going around
     */
    private static final float FINAL_ANGLE = 360;
    /**
     * relative Y location
     */
    private static final float SUN_Y_RELATIVE_LOCATION = 1.5f;
    /**
     * sun tag name
     */
    public static final String TAG_NAME = "sun";
    private static final Float INIT_VAL = 0f;
    private static final float BASE = 2;

    /**
     *
     * @param windowDimensions of game
     * @param angleInSky the angle of sun in sky
     * @return sun next position
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky){
        Vector2 rotatedVector = Vector2.UP.mult(windowDimensions.y() / BASE).rotated(angleInSky);
        float x = (rotatedVector.x() * windowDimensions.x() / windowDimensions.y()) +
                (windowDimensions.x() / BASE);
        float y = rotatedVector.y() + windowDimensions.y() / SUN_Y_RELATIVE_LOCATION;
        return new Vector2(x, y);
    }

    /**
     * creates an object of sun in the game
     * @param gameObjects the collection of all game objects currently in the game
     * @param layer of sun in the game
     * @param windowDimensions of game
     * @param cycleLength of sun going around
     * @return sun game object
     */
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
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(TAG_NAME);
        Transition<Float> transition = new Transition<>(
                sun,
                angle -> sun.setCenter(calcSunPosition(windowDimensions, angle)),
                INIT_VAL,
                FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength * BASE,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }
}

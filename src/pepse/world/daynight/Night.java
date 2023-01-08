package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.Color;

public class Night {
    /**
     * darkest opacity
     */
    private static final float MIDNIGHT_OPACITY = 0.5f;
    /**
     * night name tag
     */
    public static final String TAG_NAME = "night";

    /**
     * creates an object of night in the game
     * @param gameObjects the collection of all game objects currently in the game
     * @param layer of night rectangle
     * @param windowDimensions of game
     * @param cycleLength for the night to return
     * @return night game object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength){
        Renderable nightRectRend = new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRectRend);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(TAG_NAME);
        Transition<Float> transition = new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }


}

package pepse.world;

import java.awt.Color;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

public class Sky {
    /**
     * sky color
     */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    /**
     * sky name tag
     */
    public static final String TAG_NAME = "sky";

    /**
     * creates an object of sky in the game
     * @param gameObjects the collection of all game objects currently in the game
     * @param windowDimensions of game
     * @param skyLayer in the game
     * @return a sky game object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(TAG_NAME);
        return sky;
    }
}

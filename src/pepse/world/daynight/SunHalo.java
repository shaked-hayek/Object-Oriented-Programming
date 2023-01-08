package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.Color;

public class SunHalo {
    /**
     * sun halo name tag
     */
    private static final String TAG_NAME = "sun halo";
    /**
     * size of sun halo
     */
    private static final int HALO_SIZE = 70;

    /**
     *
     * @param gameObjects the collection of all game objects currently in the game
     * @param layer of sun halo in the game
     * @param sun the sun game object in game
     * @param color of sun halo
     * @return aun halo game object
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        Renderable haloCircle = new OvalRenderable(color);
        GameObject halo = new GameObject(
                Vector2.ZERO,
                sun.getDimensions().add(new Vector2(HALO_SIZE, HALO_SIZE)),
                haloCircle
        );
        halo.setCenter(sun.getCenter());
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo, layer);
        halo.setTag(TAG_NAME);
        return halo;
    }
}

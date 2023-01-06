package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.Color;

public class SunHalo {
    private static final String tagName = "sun halo";
    private static final int HALO_SIZE = 70;

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
        halo.setTag(tagName);
        return halo;
    }

//    @FunctionalInterface
//    public interface Component {
//        void update(float deltaTime);
//
//    }

}

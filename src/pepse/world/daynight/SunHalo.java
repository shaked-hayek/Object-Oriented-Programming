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
//        Renderable haloCircle = new OvalRenderable(ColorSupplier.approximateColor(color));
        Renderable haloCircle = new OvalRenderable(color);
//        GameObject halo = new GameObject(Vector2.ZERO, new Vector2(150,150), haloCircle);
        GameObject halo = new GameObject(Vector2.ZERO, sun.getDimensions().add(new Vector2(30,30)),
        haloCircle);
        halo.setCenter(sun.getCenter());
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo, layer);
        halo.setTag("sun halo");
        return halo;
    }

//    @FunctionalInterface
//    public interface Component {
//        void update(float deltaTime);
//
//    }

}

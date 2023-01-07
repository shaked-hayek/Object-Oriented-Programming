package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

//
public class Leaf extends Block {
    private static final String LEAF_TAG = "leaf";
    private static final int CYCLE_LENGTH = 60;
    private final Random rand;
    private final GameObjectCollection gameObjects;
    private final Vector2 topLeftCorner;

    public Leaf(GameObjectCollection gameObjects,
                Vector2 topLeftCorner,
                Color color){

        super(topLeftCorner,new RectangleRenderable(ColorSupplier.approximateColor(color)));

        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.rand = new Random();
//        block leaf = new GameObject(Vector2.ZERO, topLeftCorner, super.renderer().getRenderable());
//        leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        physics().setMass(10);
        gameObjects.addGameObject(this, Layer.BACKGROUND);
        this.setTag(LEAF_TAG);


    }
//
//
//    public GameObject create(){
//        Renderable leafRend = new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));
//        GameObject leaf = new GameObject(Vector2.ZERO, topLeftCorner, leafRend);
////        leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
//        gameObjects.addGameObject(leaf, layer);
//        leaf.setTag(LEAF_TAG);
//
//        float waitTime = rand.nextFloat(0, 0.5f);
//
//        Transition<Float> transition = new Transition<>(
//                leaf,
//                leaf.renderer()::setRenderableAngle,
//                0f,
//                20f,
//                Transition.CUBIC_INTERPOLATOR_FLOAT,
//                CYCLE_LENGTH,
//                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
//                null);
//
////        ScheduledTask scheduledTask = new ScheduledTask(leaf, waitTime, true, transition);
//
//        return leaf;
//    }
}

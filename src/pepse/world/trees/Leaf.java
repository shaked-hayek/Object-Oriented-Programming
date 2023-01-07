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
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

//
public class Leaf extends Block {
    private static final int MOVING_WAIT_TIME_BASE = 25;
    /**
     * Factor by which to divide the random wait time for moving transition
     */
    private static final float MOVING_WAIT_TIME_DENOMINATOR = 10;
    private static final String LEAF_TAG = "leaf";
    private static final int CYCLE_LENGTH = 5;
    private final Random rand;
    private final GameObjectCollection gameObjects;
    private final Vector2 topLeftCorner;
    private Transition<Float> angleTransition;
    private Transition<Vector2> widthTransition;
    private ScheduledTask scheduledMoveTask;

    public Leaf(GameObjectCollection gameObjects,
                Vector2 topLeftCorner,
                Color color){

        super(topLeftCorner,new RectangleRenderable(ColorSupplier.approximateColor(color)));

        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.rand = new Random();
//        block leaf = new GameObject(Vector2.ZERO, topLeftCorner, super.renderer().getRenderable());
//        leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        physics().setMass(0);
        gameObjects.addGameObject(this, Layer.BACKGROUND);
        this.setTag(LEAF_TAG);
        //add movement to leaves at different time
        scheduledMoveTask();
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    void moveTransition(){

        this.angleTransition = new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                10f,
                -10f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        widthTransition = new Transition<>(
                this,
                this::setDimensions,
                new Vector2(getDimensions().mult(0.8f)),
                new Vector2(getDimensions().mult(1.1f)),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    void scheduledMoveTask (){
        float waitTime = (rand.nextInt(300))/(float)100;
        scheduledMoveTask = new ScheduledTask(this, waitTime, false, this::moveTransition);
    }

}

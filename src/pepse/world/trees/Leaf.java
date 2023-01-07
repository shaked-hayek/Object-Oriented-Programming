package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

//
public class Leaf extends Block {
    private static final String LEAF_TAG = "leaf";
    private static final int CYCLE_LENGTH = 5;
    private static final Float ANGLE = 8f;
    private final Random rand;
    private final GameObjectCollection gameObjects;
    private final Vector2 topLeftCorner;
    private final int seed;
    //    private final int fadeOutTime;
    private Transition<Float> angleTransition;
    private Transition<Vector2> widthTransition;
    private ScheduledTask scheduledMoveTask;
    private ScheduledTask scheduledFallTask;
    private Transition<Float> fallTransition;

    public Leaf(GameObjectCollection gameObjects,
                Vector2 topLeftCorner,
                Color color, int seed){

        super(topLeftCorner,new RectangleRenderable(ColorSupplier.approximateColor(color)));

        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.seed = seed;
        this.rand = new Random();
//        leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        physics().setMass(0);
        gameObjects.addGameObject(this, Layer.BACKGROUND);
        this.setTag(LEAF_TAG);

        //add movement to leaves at different time
        scheduledTransitionTask();

    }

    /**
     *
     * @param other any other object
     * @return false - so no collision will be made with any obj
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Objects.equals(other.getTag(), Terrain.TAG_NAME);
    }

    void moveTransition(){

        this.angleTransition = new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                ANGLE,
                -ANGLE,
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

    void scheduledTransitionTask(){
        float waitTimeMove = (rand.nextInt(300))/(float)100;
        float waitTimeFall = (rand.nextInt(20000))/(float)100;
        scheduledMoveTask = new ScheduledTask(this, waitTimeMove, false, this::moveTransition);
        scheduledFallTask = new ScheduledTask(this, waitTimeFall, true, this::fallTransition);

    }

    void fallTransition() {

        this.transform().setVelocityY(60);

        int fadeOutTime = rand.nextInt(120);

        this.fallTransition = new Transition<>(
                this,
                this.renderer()::fadeOut,
                10f,
                -10f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                fadeOutTime,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }

}

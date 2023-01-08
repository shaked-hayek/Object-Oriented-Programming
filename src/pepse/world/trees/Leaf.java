package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.Color;
import java.util.Objects;
import java.util.Random;


public class Leaf extends Block {
    /**
     * Leaf name tag
     */
    public static final String LEAF_TAG = "leaf";
    /**
     * length of moving transition
     */
    private static final int CYCLE_LENGTH = 5;
    /**
     * moving leaf angle
     */
    private static final Float ANGLE = 8f;
    /**
     * minimum leaf dim
     */
    private static final float DIM_MIN = 0.8f;
    /**
     * maximum leaf dim
     */
    private static final float DIM_MAX = 1.1f;
    /**
     * wait time for transition - base
     */
    private static final float WAIT_TIME_BASE = 100;
    /**
     * max time for moving transition
     */
    private static final int MOVE_MAX_TIME = 300;
    /**
     * max time for falling transition
     */
    private static final int FALL_MAX_TIME = 20000;
    /**
     * max time for fade out
     */
    private static final int FADEOUT_MAX_TIME = 30;
    /**
     * min time for a leaf to be born again
     */
    private static final int BORN_MIN_TIME = 4;
    /**
     * max time for a leaf to be born again
     */
    private static final int BORN_MAX_TIME = 20;
    /**
     * falling leaf velocity
     */
    private static final float FALLING_TREE_VELOCITY = 60;
    /**
     * Opaqueness of leaf when being born again
     */
    private static final float BORN_OPAQ = 1f;
    /**
     * Random object
     */
    private final Random rand;
    /**
     * global game object collection
     */
    private final GameObjectCollection gameObjects;
    /**
     * the top left corner of the position of leaf
     */
    private final Vector2 topLeftCorner;
    /**
     * leaf color
     */
    private final Color color;
    /**
     * leaf layer
     */
    private final int layer;
    /**
     * moving angle transition
     */
    private Transition<Float> angleTransition;
    /**
     * changing width transition
     */
    private Transition<Vector2> widthTransition;
    /**
     * falling leaf transition
     */
    private Transition<Float> fallTransition;


    /**
     * constructor
     * @param gameObjects the collection of all game objects currently in the game
     * @param topLeftCorner the top left corner of the position of the leaf object
     * @param color of the leaf
     */
    public Leaf(GameObjectCollection gameObjects,
                Vector2 topLeftCorner,
                Color color, int layer){

        super(topLeftCorner,new RectangleRenderable(ColorSupplier.approximateColor(color)));

        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.color = color;
        this.layer = layer;
        this.rand = new Random();
        physics().setMass(0);
        this.setTag(LEAF_TAG);

        //add movement to leaves at different time
        scheduledTransitionTask();
    }

    /**
     *
     * @param other any other object
     * @return false - so no collision will be made with any obj except ground
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Objects.equals(other.getTag(), Terrain.GROUND_TAG);
    }

    /**
     * leaf stops falling when collided with ground - sets velocity to 0
     * @param other the object that the snowflake collided with
     * @param collision the collision parameters
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
//        removeComponent(this.fallTransition);
//        fallTransition = null;
        transform().setVelocity(Vector2.ZERO);
    }

    /**
     * leaf stops falling when collided with ground - sets velocity to 0
     * @param other the object that the snowflake collided with
     * @param collision the collision parameters
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        transform().setVelocity(Vector2.ZERO);
    }

    /**
     * adding moving angle and changing width transition
     */
    void moveTransition(){
        //moving angle transition
        this.angleTransition = new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                ANGLE,
                -ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        //changing width transition
        widthTransition = new Transition<>(
                this,
                this::setDimensions,
                getDimensions().mult(DIM_MIN),
                getDimensions().mult(DIM_MAX),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * schedule all transition tasks
     */
    void scheduledTransitionTask(){
        float waitTimeMove = (rand.nextInt(MOVE_MAX_TIME))/WAIT_TIME_BASE;
        float waitTimeFall = (rand.nextInt(FALL_MAX_TIME))/WAIT_TIME_BASE;
        ScheduledTask scheduledMoveTask = new ScheduledTask(
                this, waitTimeMove, false, this::moveTransition);
        ScheduledTask scheduledFallTask = new ScheduledTask(
                this, waitTimeFall, false, this::fallTransition);
    }

    /**
     * add falling leaf transition
     */
    void fallTransition() {
        removeComponent(angleTransition);
        removeComponent(widthTransition);

        int fadeOutTime = rand.nextInt(FADEOUT_MAX_TIME);
        int bornAgainTime = Utils.randIntInRange(rand, BORN_MIN_TIME, BORN_MAX_TIME);

        this.renderer().fadeOut(fadeOutTime, ()-> new ScheduledTask(
                this,
                bornAgainTime,
                false,
                this::bornAgain));
        this.transform().setVelocityY(FALLING_TREE_VELOCITY);
    }

    /**
     * creating leaf after it fell from tree
     */
    void bornAgain(){
        setTopLeftCorner(topLeftCorner);
        this.transform().setVelocity(Vector2.ZERO);
        renderer().setOpaqueness(BORN_OPAQ);
        scheduledTransitionTask();
    }

    /**
     * remove all components from tree
     */
    public void removeLeaf() {
        removeComponent(angleTransition);
        angleTransition = null;
        removeComponent(widthTransition);
        widthTransition = null;
        removeComponent(fallTransition);
        fallTransition = null;
        gameObjects.removeGameObject(this, layer);
    }
}

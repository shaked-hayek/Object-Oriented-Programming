package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class SnowFlake extends Block{

    private static final String SNOW_TAG = "snow";
    private static final float FADE_OUT_TIME = 0.3f ;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final int seed;
    private final Random rand;
//    private final Color SNOW_COLOR = new Color(200,220,220);

    private static final float BALL_SPEED = 70;
//    private final RectangleRenderable renderer;

    public SnowFlake(GameObjectCollection gameObjects, Vector2 windowDimensions, Vector2 topLeftCorner,
                     int layer, int seed, Color color){
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(color)));

        physics().setMass(0);
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.rand = new Random();
    }

    /**
     *
     * @param other any other object
     * @return false - so no collision will be made with any obj
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Objects.equals(other.getTag(), Terrain.GROUND_TAG);
//        return Objects.equals(other.getTag(), Terrain.GROUND_TAG) ||
//                Objects.equals(other.getTag(), Tree.TREE_TAG) ||
//                Objects.equals(other.getTag(), Leaf.LEAF_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.renderer().fadeOut(FADE_OUT_TIME);
        this.gameObjects.removeGameObject(this, Layer.STATIC_OBJECTS);
    }

    public void setRandomVelocity(){
        float ballVelX = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        this.setVelocity(new Vector2(ballVelX, BALL_SPEED));
    }

}

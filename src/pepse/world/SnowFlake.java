package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class SnowFlake extends Block{

    /**
     * snow tag name
     */
    private static final String SNOW_TAG = "snow";
    /**
     * time for the snow to disappear after touching the ground
     */
    private static final float FADE_OUT_TIME = 0.3f ;
    /**
     * global game object collection
     */
    private final GameObjectCollection gameObjects;
    /**
     * speed of snowflake falling down
     */
    private static final float SNOW_SPEED = 70;

    /**
     *
     * @param gameObjects the collection of all game objects currently in the game
     * @param topLeftCorner the top left corner of the position of the snowflake object
     * @param color of the snowflake
     */
    public SnowFlake(GameObjectCollection gameObjects, Vector2 topLeftCorner, Color color){
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(color)));

        physics().setMass(0);
        this.gameObjects = gameObjects;
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
     *
     * @param other the object that the snowflake collided with
     * @param collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.renderer().fadeOut(FADE_OUT_TIME);
        this.gameObjects.removeGameObject(this, Layer.STATIC_OBJECTS);
    }

    /**
     * set random velocity for the snowflake
     */
    public void setRandomVelocity(){
        float ballVelX = SNOW_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        this.setVelocity(new Vector2(ballVelX, SNOW_SPEED));
    }

}

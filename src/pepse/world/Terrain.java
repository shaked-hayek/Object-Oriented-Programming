package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.Color;
import java.util.Random;


public class Terrain {
    private static final int TERRAIN_DEPTH = 20;
    private final float groundHeightAtX0;
    private final int seed;
    private final Vector2 windowDimensions;
    private GameObjectCollection gameObjects;
    private final int groundLayer;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final float GROUND_START_HEIGHT = (float) 2 / 3;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.x() * GROUND_START_HEIGHT;
        this.seed = seed;

        createInRange(0, 1280);
    }

    public void createInRange(int minX, int maxX) { // TODO
        double curr_x = groundHeightAt(groundHeightAtX0);
        Renderable blockRender = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        //added to create random blocks
        int minXFixed = (int)(Block.SIZE*(Math.floor((double)minX/Block.SIZE)));
        int maxXFixed = (int)(Block.SIZE*(Math.ceil((double)maxX/Block.SIZE)));
        Random rand = new Random();
        int currMin = minXFixed;
        int blockRand = 0;
        for (int i = minXFixed; i < maxXFixed; i+=blockRand) {
            blockRand = (rand.nextInt((maxXFixed - currMin)/Block.SIZE))*Block.SIZE;
            Block block = new Block(new Vector2(currMin, windowDimensions.y()-30), blockRender);
            block.setTopLeftCorner(new Vector2(currMin, windowDimensions.y()-30));
            gameObjects.addGameObject(block, groundLayer);
            block.setTag("ground");
            currMin+=blockRand;
            if (maxXFixed-currMin <= Block.SIZE){ //added because otherwise get stuck
                break;
            }
        }
        Block block = new Block(new Vector2(currMin, windowDimensions.y()-30), blockRender);
        block.setTopLeftCorner(new Vector2(currMin, windowDimensions.y()-30));
        gameObjects.addGameObject(block, groundLayer);
        block.setTag("ground"); //added in case not all full, not a problem to fill extra

//        Block block = new Block(Vector2.ZERO, blockRender);
//        Block block = new Block(new Vector2(0, windowDimensions.y()-30), blockRender);
//        gameObjects.addGameObject(block, groundLayer);
//        block.setTag("ground");
    }

    public double groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        return noiseGenerator.noise(x);
    }
}

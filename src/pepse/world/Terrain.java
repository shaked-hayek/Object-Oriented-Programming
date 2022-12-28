package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.Color;


public class Terrain {
    private final float groundHeightAtX0;
    private final int seed;
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
        this.groundHeightAtX0 = windowDimensions.x() * GROUND_START_HEIGHT;
        this.seed = seed;

        createInRange(0, 0);
    }

    public void createInRange(int minX, int maxX) { // TODO
        double curr_x = groundHeightAt(groundHeightAtX0);
        Renderable blockRender = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block block = new Block(Vector2.ZERO, blockRender);
        gameObjects.addGameObject(block, groundLayer);
        block.setTag("block");
    }

    public double groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        return noiseGenerator.noise(x);
    }
}

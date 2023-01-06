package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.Color;


public class Terrain {
    /**
     * needs to be added to every value of ground. this way, ground will always be covered by 20+
     */
    private static final int TERRAIN_DEPTH = 20;
    private final float groundHeightAtX0;
    private final int seed;
    private GameObjectCollection gameObjects;
    private final int groundLayer;
    private static final String TAG_NAME = "ground";
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final float GROUND_START_HEIGHT = (float) 2 / 3;
    private static final int GROUND_SPREAD = 15;
    private static final int GROUND_SHARPNESS = 6 * Block.SIZE;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * GROUND_START_HEIGHT;
        this.seed = seed;

        createInRange(0, 1280);
    }

    public void createInRange(int minX, int maxX) {
        int minXFixed = (int) (Block.SIZE * (Math.floor((double) minX / Block.SIZE)));
        int maxXFixed = (int) (Block.SIZE * (Math.ceil((double) maxX / Block.SIZE)));

        for (int i = minXFixed; i < maxXFixed; i++) {
            float currentX = minXFixed + (i * Block.SIZE);
            float topY = groundHeightAt(currentX);
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Renderable blockRender = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(
                        new Vector2(currentX, topY + j * Block.SIZE),
                        blockRender
                );
                gameObjects.addGameObject(block, groundLayer);
                block.setTag(TAG_NAME);
            }
        }
    }

    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        float y = (float) noiseGenerator.noise(x / GROUND_SPREAD);
        return (y * GROUND_SHARPNESS) + groundHeightAtX0;
    }
}

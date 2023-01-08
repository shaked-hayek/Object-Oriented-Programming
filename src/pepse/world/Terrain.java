package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import pepse.util.Utils;

import java.awt.Color;
import java.util.HashMap;


/**
 * Class that create and control the Terrain in the world
 */
public class Terrain {
    /** Ground tag name */
    public static final String GROUND_TAG = "ground";
    /** Size of world to build in addition to screen size */
    public static final int WORLD_BUFFER = 6 * Block.SIZE;

    private static final int TERRAIN_DEPTH = 20;
    private static final Color BASE_GROUND_COLOR = new Color(206, 222, 227);
    private static final float GROUND_START_HEIGHT = (float) 3 / 4;
    private static final int GROUND_SPREAD = 15;
    private static final int GROUND_SHARPNESS = 6 * Block.SIZE;
    private final float groundHeightAtX0;
    private final int seed;
    private final int widthInBlocks;
    private final int groundLayer;
    private GameObjectCollection gameObjects;
    private HashMap<Integer, Block[]> blockMap;

    /**
     * Constructor.
     * @param gameObjects       Game object collection
     * @param groundLayer       The layer to put all ground blocks
     * @param windowDimensions  Size of the window
     * @param seed              Random seed
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * GROUND_START_HEIGHT;
        this.seed = seed;
        blockMap = new HashMap<>();

        widthInBlocks = (int) (
                Math.abs(Math.floor((double) -WORLD_BUFFER / Block.SIZE)) +
                Math.abs(Math.ceil((double) (windowDimensions.x() + WORLD_BUFFER) / Block.SIZE))
            ) + 1; // Added 1 in order to get uneven number of blocks
        createInRange(-WORLD_BUFFER, (int) windowDimensions.x() + WORLD_BUFFER + Block.SIZE);
    }

    /**
     * The width in blocks of the terrain.
     * @return width as int
     */
    public int getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
     * Create ground blocks in given range.
     * @param minX  start range
     * @param maxX  end range
     */
    public void createInRange(int minX, int maxX) {
        int minXFixed = Utils.getFixedMin(minX);
        int maxXFixed = Utils.getFixedMax(maxX);

        for (int currentX = minXFixed; currentX < maxXFixed; currentX+=Block.SIZE) {
            float topY = groundHeightAt(currentX);
            if (blockMap.containsKey(currentX)) {  // Don't run again on x if blocks already exists
                continue;
            }
            Block[] blocksList = new Block[TERRAIN_DEPTH];
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Renderable blockRender = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(
                        new Vector2(currentX, topY + j * Block.SIZE),
                        blockRender
                );
                // Put only the top block in groundLayer
                if (j == 0) {
                    gameObjects.addGameObject(block, groundLayer);
                } else {
                    gameObjects.addGameObject(block, groundLayer - 1);
                }
                blocksList[j] = block;
                block.setTag(GROUND_TAG);
            }
            blockMap.put(currentX, blocksList);
        }
    }

    /**
     * Remove ground blocks in given range.
     * @param minX  start range
     * @param maxX  end range
     */
    public void removeInRange(int minX, int maxX) {
        int minXFixed = Utils.getFixedMin(minX);
        int maxXFixed = Utils.getFixedMax(maxX);

        for (int currentX = minXFixed; currentX < maxXFixed; currentX+=Block.SIZE) {
            boolean first = true;
            if (!blockMap.containsKey(currentX)) {
                continue;
            }
            for (Block block : blockMap.get(currentX)) {
                if (first) {
                    gameObjects.removeGameObject(block, groundLayer);
                    first = false;
                } else {
                    gameObjects.removeGameObject(block, groundLayer - 1);
                }
            }
            blockMap.remove(currentX);
        }
    }

    /**
     * Get the ground's height at place x, by noise generator.
     * @param x the place
     * @return  ground's height
     */
    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        float y = (float) noiseGenerator.noise(x / GROUND_SPREAD);
        return (y * GROUND_SHARPNESS) + groundHeightAtX0;
    }
}

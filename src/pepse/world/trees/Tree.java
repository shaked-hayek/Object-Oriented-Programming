package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.Block;

import java.awt.Color;
import java.util.*;
import java.util.function.Function;

public class Tree {
    public static final String TREE_TAG = "tree stem";
    public static final Color STEM_COLOR = new Color(100, 50, 20);
    public static final Color LEAVES_COLOR = new Color(50, 200, 30);
    public static final int GET_RAND_SIZE = -1;
    private static final float THRESHOLD_TREE = 0.1f;
    public final Random rand;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final Function<Float, Float> groundHeightAt;
    private final HashMap<Integer, Block[]> treeStemMap;
    private final Map<Float, List<Leaf>> leafMap;

    /**
     * constructor
     * @param gameObjects the collection of all game objects currently in the game
     * @param layer of tree
     * @param windowDimensions of game
     * @param groundHeightAt function that gets x-coordinate and returns the height of the ground at x
     * @param seed of game, for random
     */
    public Tree(GameObjectCollection gameObjects,
                int layer,
                Vector2 windowDimensions,
                Function<Float, Float> groundHeightAt, int seed) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.rand = new Random(seed);
        treeStemMap = new HashMap<>();
        leafMap = new TreeMap<>();

        this.groundHeightAt = groundHeightAt;
    }

    /**
     *  Checks if coordinate x is next to a tree.
     * @param x coordinate
     * @return true if x coordinate is near a tree, false otherwise
     */
    private boolean isNextToTree(float x) {
        int prevX = (int) x - Block.SIZE;
        int nextX = (int) x - Block.SIZE;
        return (treeStemMap.containsKey(prevX) && treeStemMap.get(prevX) != null) ||
                (treeStemMap.containsKey(nextX) && treeStemMap.get(nextX) != null);
    }

    /**
     * creates trees randomly between minX-maxX coordinates
     * @param minX coordinate
     * @param maxX coordinate
     */
    public void createInRange(int minX, int maxX) {
        float randToPlant;

        int minXFixed = Utils.getFixedMin(minX);
        int maxXFixed = Utils.getFixedMax(maxX);

        for (int i = minXFixed; i <= maxXFixed; i += Block.SIZE) {
            // Don't plant tree in the middle
            if (i < windowDimensions.x() / 2 + Block.SIZE && i > windowDimensions.x() / 2 - Block.SIZE) {
                continue;
            }
            // Check if used to exist tree
            if (treeStemMap.containsKey(i)) {
                if (treeStemMap.get(i) != null && treeStemMap.get(i)[0] == null) {
                    plant((float) i, treeStemMap.get(i).length);
                }
            } else {
                Block[] stemBlocks = null;
                if (!isNextToTree(i)) {
                    randToPlant = rand.nextFloat();
                    if (randToPlant < THRESHOLD_TREE) {
                        stemBlocks = plant((float) i, GET_RAND_SIZE);
                    }
                }
                treeStemMap.put(i, stemBlocks);
            }
        }
    }

    /**
     * Removes trees between minX-maxX coordinates - and saves in the hashMap
     * @param minX coordinate
     * @param maxX coordinate
     */
    public void RemoveInRange(int minX, int maxX) {
        int minXFixed = Utils.getFixedMin(minX);
        int maxXFixed = Utils.getFixedMax(maxX);

        for (int currentX = minXFixed; currentX < maxXFixed; currentX += Block.SIZE) {
            // remove trees' stem
            if (!treeStemMap.containsKey(currentX) || treeStemMap.get(currentX) == null) {
                continue;
            }
            for (Block block : treeStemMap.get(currentX)) {
                gameObjects.removeGameObject(block, layer);
            }
            treeStemMap.get(currentX)[0] = null; // Change first block to null to indicate deleted

            // remove trees' leaves
            if (!leafMap.containsKey((float) currentX) || leafMap.get((float) currentX) == null) {
                continue;
            }
            for (Leaf leaf:leafMap.get((float) currentX)){
                leaf.removeLeaf();
            }
        }
    }

    /**
     * plants a tree in x-coordinate, adding all tree stem blocks to an array
     * @param x coordinate
     * @param size of tree
     * @return an array of blocks of tree stem
     */
    private Block[] plant(float x, int size) {
        Block[] stemBlocks = plantTreeStem(x, size);
        plantTreeLeaves(stemBlocks[stemBlocks.length - 1]);
        return stemBlocks;
    }

    /**
     * plants a tree stem in x-coordinate, adding all tree stem blocks to an array
     * @param x coordinate
     * @param numBlocks in tree
     * @return an array of blocks of tree stem
     */
    private Block[] plantTreeStem(float x, int numBlocks) {
        float stemBottom = groundHeightAt.apply(x);
        if (numBlocks == GET_RAND_SIZE) {
            float stemTop = Utils.randFloatInRange(rand,
                    windowDimensions.y() / 4, stemBottom - Block.SIZE * 4);
            numBlocks = Utils.blocksInDist(stemTop, stemBottom);
        }
        Block[] stemBlocks = new Block[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            Renderable blockRender =
                    new RectangleRenderable(ColorSupplier.approximateColor(STEM_COLOR));
            float y = (stemBottom - Block.SIZE) - (Block.SIZE * i);
            Block block = new Block(new Vector2(x, y), blockRender);
            gameObjects.addGameObject(block, layer);
            block.setTag(TREE_TAG);
            stemBlocks[i] = block;
        }
        return stemBlocks;
    }

    /**
     * plants tree leaves for the tree, adding all leaves blocks to a list
     * @param topBlock of tree - so plant leaves around it
     */
    private void plantTreeLeaves(Block topBlock){
        int blocksInTree =
                (int) ((groundHeightAt.apply(topBlock.getTopLeftCorner().x())) - topBlock.getTopLeftCorner().y())
                        /Block.SIZE;
        int leavesSquareEdge = Math.max(blocksInTree/4, 1) * Block.SIZE;

        // create a square of leaves at size leavesSquareEdge where topBlock.center is the center
        Vector2 topLeftSquare = topBlock.getTopLeftCorner().subtract(new Vector2(leavesSquareEdge,
                leavesSquareEdge));
        List<Leaf> leafList = new ArrayList<>();
        for (float i = topLeftSquare.x(); i <= topLeftSquare.x()+leavesSquareEdge*2; i+=Block.SIZE) {
            for (float j = topLeftSquare.y(); j <= topLeftSquare.y() + leavesSquareEdge*2; j+=Block.SIZE) {
                Leaf leaf = new Leaf(gameObjects, new Vector2(i,j), LEAVES_COLOR);
                gameObjects.addGameObject(leaf, leaf.LAYER);
                leafList.add(leaf);
            }
        }
        leafMap.put(topBlock.getTopLeftCorner().x(), leafList);
    }
}

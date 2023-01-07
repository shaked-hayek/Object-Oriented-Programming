package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final String TREE_TAG = "tree stem";
    public static final Color STEM_COLOR = new Color(100, 50, 20);
    public static final Color LEAVES_COLOR = new Color(50, 200, 30);
    public static final int GET_RAND_SIZE = -1;
    public final Random rand;
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final Function<Float, Float> groundHeightAt;
    private HashMap<Integer, Block[]> treeStemMap;
    private HashMap<Float, List<Leaf>> leafMap;

    public Tree(GameObjectCollection gameObjects,
                int layer,
                Vector2 windowDimensions,
                Function<Float, Float> groundHeightAt, int seed) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.rand = new Random(seed);
        treeStemMap = new HashMap<>();
        leafMap = new HashMap<>();


        this.groundHeightAt = groundHeightAt;
    }

    private boolean isNextToTree(float x) {
        int prevX = (int) x - Block.SIZE;
        int nextX = (int) x - Block.SIZE;
        return (treeStemMap.containsKey(prevX) && treeStemMap.get(prevX) != null) ||
                (treeStemMap.containsKey(nextX) && treeStemMap.get(nextX) != null);
    }

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
                if (treeStemMap.get(i) != null) {
                    plant((float) i, treeStemMap.get(i).length);
                }
            } else {
                Block[] stemBlocks = null;
                if (!isNextToTree(i)) {
                    randToPlant = rand.nextFloat(0, 1);
                    if (randToPlant < 0.1) {
                        stemBlocks = plant((float) i, GET_RAND_SIZE);
                    }
                }
                treeStemMap.put(i, stemBlocks);
            }

        }

    }

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

            // remove trees' leaves
            if (!leafMap.containsKey((float) currentX) || leafMap.get((float) currentX) == null) {
                continue;
            }
            for (Leaf leaf:leafMap.get((float) currentX)){
                gameObjects.removeGameObject(leaf);
            }
        }
    }

    private Block[] plant(float x, int size) {
        Block[] stemBlocks = plantTreeStem(x, size);
        plantTreeLeaves(stemBlocks[stemBlocks.length - 1]);
        return stemBlocks;
    }

    private Block[] plantTreeStem(float x, int numBlocks) {
        float stemBottom = groundHeightAt.apply(x);
        if (numBlocks == GET_RAND_SIZE) {
            float stemTop = rand.nextFloat(windowDimensions.y() / 4, stemBottom - Block.SIZE * 4);
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

    private void plantTreeLeaves(Block topBlock){
        int blocksInTree =
                (int) ((groundHeightAt.apply(topBlock.getTopLeftCorner().x())) - topBlock.getTopLeftCorner().y())
                        /Block.SIZE;
        int leavesSquareEdge = Math.max(blocksInTree/4, 1) * Block.SIZE;

        // create a square of leaves at size leavesSquareEdge where topBlock.center is the center
        Vector2 topLeftSquare = topBlock.getTopLeftCorner().subtract(new Vector2(leavesSquareEdge,
                leavesSquareEdge));
        for (float i = topLeftSquare.x(); i <= topLeftSquare.x()+leavesSquareEdge*2; i+=Block.SIZE) {
                leafMap.put(i, new ArrayList<>());
            for (float j = topLeftSquare.y(); j <= topLeftSquare.y() + leavesSquareEdge*2; j+=Block.SIZE) {
                Leaf leaf = new Leaf(gameObjects, new Vector2(i,j), LEAVES_COLOR, seed);
//                gameObjects.addGameObject(leaf);
                leafMap.get(i).add(leaf);
            }
        }
    }
}

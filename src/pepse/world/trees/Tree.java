package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final String TREE_TAG = "tree stem";
    public static Color STEM_COLOR =  new Color(100, 50, 20);
    public static Color LEAVES_COLOR =  new Color(50, 200, 30);
    public final float STEM_MIN_SIZE = Block.SIZE;
    public static final Random rand = new Random();
    private GameObjectCollection gameObjects;
    private int layer;
    private Vector2 windowDimensions;
    private Function<Float, Float> groundHeightAt;

    public Tree(GameObjectCollection gameObjects,
                int layer,
                Vector2 windowDimensions,
                Function<Float, Float> groundHeightAt){
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;

        this.groundHeightAt = groundHeightAt;
    }

    public void createInRange(int minX, int maxX) {
        float randToPlant;

        int minXFixed = (int) (Block.SIZE * (Math.floor((double) minX / Block.SIZE)));
        int maxXFixed = (int) (Block.SIZE * (Math.ceil((double) maxX / Block.SIZE)));

        //TODO: check if <= or <
        for (int i = minXFixed; i <= maxXFixed; i+=Block.SIZE) {
            if (i<windowDimensions.x()/2+Block.SIZE && i>windowDimensions.x()/2-Block.SIZE){
                continue;
            }
            randToPlant = rand.nextFloat(0,1);
            if (randToPlant<0.15){
                plant((float) i);
                i+=Block.SIZE;
            }

        }

    }

    private void plant(float i) {
        Block topBlock = plantTreeStem(i);
        if (topBlock!=null){
            plantTreeLeaves(topBlock);
        }


    }

    private Block plantTreeStem(float i){
        float stemBottom = groundHeightAt.apply(i);
        float stemTop = rand.nextFloat(windowDimensions.y()/4,stemBottom-Block.SIZE*4);
        Block highestBlock = null;
        for (float j = stemBottom-Block.SIZE; j > stemTop; j-=Block.SIZE) {
            Renderable blockRender =
                    new RectangleRenderable(ColorSupplier.approximateColor(STEM_COLOR));
            Block block = new Block(new Vector2(i, j), blockRender);
            gameObjects.addGameObject(block, layer);
            block.setTag(TREE_TAG);
            highestBlock=block;
        }
        return highestBlock;
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
            for (float j = topLeftSquare.y(); j <= topLeftSquare.y() + leavesSquareEdge*2; j+=Block.SIZE) {
                Leaf leaf = new Leaf(gameObjects, new Vector2(i,j), LEAVES_COLOR);
                gameObjects.addGameObject(leaf);


            }

        }



    }
}

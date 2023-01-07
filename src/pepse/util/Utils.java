package pepse.util;

import pepse.world.Block;

public class Utils {
    public static int getFixedMin(float size) {
        return (int) (Block.SIZE * (Math.floor((double) size / Block.SIZE)));
    }

    public static int getFixedMax(float size) {
        return (int) (Block.SIZE * (Math.ceil((double) size / Block.SIZE)));
    }

    public static int blocksInDist(float min, float max) {
        return (int) Math.floor(Math.abs(max - min) / Block.SIZE);
    }
}

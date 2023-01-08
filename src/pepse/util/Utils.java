package pepse.util;

import pepse.world.Block;

public class Utils {
    /**
     *
     * @param size as float
     * @return closest floor val int that divides by Block size
     */
    public static int getFixedMin(float size) {
        return (int) (Block.SIZE * (Math.floor((double) size / Block.SIZE)));
    }

    /**
     *
     * @param size as float
     * @return closest ceil val int that divides by Block size
     */
    public static int getFixedMax(float size) {
        return (int) (Block.SIZE * (Math.ceil((double) size / Block.SIZE)));
    }

    /**
     *
     * @param min as float
     * @param max as float
     * @return number of Blocks between min&max
     */
    public static int blocksInDist(float min, float max) {
        return (int) Math.floor(Math.abs(max - min) / Block.SIZE);
    }
}

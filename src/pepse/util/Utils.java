package pepse.util;

import pepse.world.Block;

import java.util.Random;

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

    /**
     * Get random number between 2 values.
     * @param rand  Random object
     * @param start of range
     * @param end   of range
     * @return  random int in range
     */
    public static int randIntInRange(Random rand, int start, int end) {
        return start + rand.nextInt(end - start);

    }
}

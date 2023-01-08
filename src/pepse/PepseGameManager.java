package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.Utils;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.Color;
import java.util.Random;


public class PepseGameManager extends GameManager {
    private static final String GAME_NAME = "Pepse";
    private static final Vector2 GAME_RES = new Vector2(1280, 720);
    private static final float BUFFER_BASE = 2f;
    private static final float AVATAR_MULT_FACTOR = 0.5f;
    private static final float MAX_RAND_SNOW = 10;
    private static final float THRESHOLD_SNOW = 0.15f;
    private static final float INIT_Y_SNOW = -400;
    private static final float TWO_PARTS = 2;
    private static final int ADD_1 = 1;
    private static final int ADD_2 = 2;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private GameObjectCollection gameObjectCollection;
    private static final int CYCLE_LENGTH = 30;
    private static final int SEED = 2;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private final Color SNOW_COLOR = new Color(200,220,220);
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int TREE_LAYER = Layer.STATIC_OBJECTS;
    private static final int TERRAIN_BASE_LAYER = Layer.STATIC_OBJECTS - 1;
    private static final int ENERGY_TEXT_DIST = 10;
    private static final int ENERGY_TEXT_SIZE = 40;

    private static float currentMiddleX;
    private static float worldEndRight;
    private static float worldEndLeft;
    private Terrain terrain;
    private Avatar avatar;
    private Tree tree;
    private int blocksSideFromAvatar;
    private Random rand;

    /**
     * runs the game
     * @param args arguments
     */
    public static void main(String[] args) {
        new PepseGameManager(GAME_NAME, GAME_RES).run();
    }

    /**
     * This is the constructor of a pepse, which calls its super (GameManager)'s constructor.
     * @param windowTitle the game's title
     * @param windowDimensions the game's dimensions
     */
    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * initializes the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                       a given key is currently pressed by the user or not.
     * @param windowController Contains an array of helpful, self-explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        this.gameObjectCollection = gameObjects();
        this.rand = new Random(SEED);
        worldEndRight = windowDimensions.x() + Terrain.WORLD_BUFFER;
        worldEndLeft = -Terrain.WORLD_BUFFER;

        // Create world
        createWorld();

        // Create avatar
        createAvatar();

        //create energy text box
        createEnergy();

    }

    /**
     * creates bar representing the energy left
     */
    private void createEnergy() {
        EnergyText energyText = new EnergyText(new Vector2(ENERGY_TEXT_DIST, ENERGY_TEXT_DIST),
                new Vector2(ENERGY_TEXT_SIZE, ENERGY_TEXT_SIZE));
        this.gameObjects().addGameObject(energyText, Layer.UI);
    }

    /**
     * creates an avatar - shaped as penguin
     */
    private void createAvatar() {
        float avatarLeftCorr =
                (float) (Block.SIZE * (Math.floor((windowDimensions.x() / TWO_PARTS) / Block.SIZE)));
        float avatarTopCorr = terrain.groundHeightAt(avatarLeftCorr) - Avatar.AVATAR_HEIGHT;
        Vector2 initialAvatarLocation = new Vector2(avatarLeftCorr, avatarTopCorr);
        avatar = Avatar.create(gameObjectCollection, AVATAR_LAYER,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(
                avatar,
                windowController.getWindowDimensions().mult(AVATAR_MULT_FACTOR).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()
        ));
        currentMiddleX = avatar.getCenter().x();
        blocksSideFromAvatar = (int) Math.floor(terrain.getWidthInBlocks() / BUFFER_BASE);

    }

    /**
     * creates the game world.
     */
    private void createWorld() {
        GameObject sky = Sky.create(gameObjectCollection, windowDimensions, Layer.BACKGROUND);
        terrain = new Terrain(gameObjectCollection, TERRAIN_BASE_LAYER, windowDimensions, SEED);
        GameObject night = Night.create(gameObjectCollection, Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjectCollection,Layer.BACKGROUND + ADD_1, windowDimensions,
                CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(gameObjectCollection,Layer.BACKGROUND + ADD_2, sun, HALO_COLOR);
        sunHalo.addComponent(deltaTime-> sunHalo.setCenter(sun.getCenter()));
        tree = new Tree(gameObjectCollection, TREE_LAYER, LEAF_LAYER, windowDimensions,
                terrain::groundHeightAt, SEED);
        tree.createInRange(-Terrain.WORLD_BUFFER, (int) windowDimensions.x() + Terrain.WORLD_BUFFER);

        gameObjects().layers().shouldLayersCollide(TERRAIN_BASE_LAYER, LEAF_LAYER, true);
        gameObjects().layers().shouldLayersCollide(TERRAIN_BASE_LAYER, AVATAR_LAYER, true);

    }

    /**
     * creates world (ground and trees), between the range of minX&maxX coordinates
     * @param minX coordinate
     * @param maxX coordinate
     */
    private void createWorldInRange(int minX, int maxX) {
        terrain.createInRange(minX, maxX);
        tree.createInRange(minX, maxX);
    }

    /**
     * removes world (ground and trees), between the range of minX&maxX coordinates
     * @param minX coordinate
     * @param maxX coordinate
     */
    private void removeWorldInRange(int minX, int maxX) {
        terrain.removeInRange(minX, maxX);
        tree.RemoveInRange(minX, maxX);
    }

    /**
     * updates world on the screen (as avatar moves)
     */
    private void updateEndlessWorld() {
        double movementSize = avatar.getCenter().x() - currentMiddleX;
        int distToAdd = 0;
        if (Math.abs(movementSize) > (Terrain.WORLD_BUFFER / BUFFER_BASE)) {
            // We moved right
            if (movementSize > (Terrain.WORLD_BUFFER / BUFFER_BASE)) {
                int numBlocksRight = (int) (Math.floor((worldEndRight - avatar.getCenter().x()) / Block.SIZE));
                distToAdd = (blocksSideFromAvatar - numBlocksRight) * Block.SIZE;
                createWorldInRange((int) worldEndRight, (int) worldEndRight + distToAdd);
                removeWorldInRange((int) worldEndLeft, (int) worldEndLeft + distToAdd);
                worldEndLeft = worldEndLeft + distToAdd;
                worldEndRight = worldEndRight + distToAdd;
                // We moved left
            } else if (movementSize < -(Terrain.WORLD_BUFFER / BUFFER_BASE)) {
                int numBlocksLeft = (int) (Math.floor((avatar.getCenter().x() - worldEndLeft) / Block.SIZE));
                distToAdd = (blocksSideFromAvatar - numBlocksLeft) * Block.SIZE;
                createWorldInRange((int) worldEndLeft - distToAdd, (int) worldEndLeft);
                removeWorldInRange((int) worldEndRight - distToAdd, (int) worldEndRight);
                worldEndLeft = worldEndLeft - distToAdd;
                worldEndRight = worldEndRight - distToAdd;
            }
            currentMiddleX = avatar.getCenter().x();
        }
        // else - no move so no need to update
    }

    /**
     * creates more random snowflakes as a part of the game update
     */
    void updateSnow(){
        int i = Utils.randIntInRange(rand, (int) worldEndLeft, (int) worldEndRight);
        float randToSnow = Utils.randFloatInRange(rand, 0, MAX_RAND_SNOW);
        if (randToSnow < THRESHOLD_SNOW) {
            SnowFlake snowFlake = new SnowFlake(gameObjectCollection, new Vector2(i, 0), SNOW_COLOR);
            snowFlake.setCenter(new Vector2(i, INIT_Y_SNOW));
            snowFlake.setRandomVelocity();
            gameObjectCollection.addGameObject(snowFlake);
        }
    }

    /**
     * updates the game features
     * @param deltaTime The time, in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEndlessWorld();
        updateSnow();
    }
}

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
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.Color;


public class PepseGameManager extends GameManager {
    private ImageReader imageReader;
    private SoundReader soundReader;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private GameObjectCollection gameObjectCollection;
    private static final int CYCLE_LENGTH = 30;
    private static final int SEED = 2;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static float currentMiddleX;
    private static float worldEndRight;
    private static float worldEndLeft;
    private Terrain terrain;
    private Avatar avatar;
    private Tree tree;
    private int blocksSideFromAvatar;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

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
        worldEndRight = windowDimensions.x() + Terrain.WORLD_BUFFER;
        worldEndLeft = -Terrain.WORLD_BUFFER;

        // Create world
        GameObject sky = Sky.create(gameObjectCollection, windowDimensions, Layer.BACKGROUND);
        terrain = new Terrain(gameObjectCollection, Layer.STATIC_OBJECTS, windowDimensions, SEED);
        GameObject night = Night.create(gameObjectCollection, Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjectCollection,Layer.BACKGROUND + 1, windowDimensions,CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(gameObjectCollection,Layer.BACKGROUND + 2, sun, HALO_COLOR);
        sunHalo.addComponent(deltaTime-> {sunHalo.setCenter(sun.getCenter());});
        tree = new Tree(gameObjectCollection, Layer.STATIC_OBJECTS, windowDimensions,
                terrain::groundHeightAt, SEED);
        tree.createInRange(-Terrain.WORLD_BUFFER, (int) windowDimensions.x() + Terrain.WORLD_BUFFER);

        // Create avatar
        float avatarLeftCorr = (float) (Block.SIZE * (Math.floor((windowDimensions.x() / 2) / Block.SIZE)));
        float avatarTopCorr = terrain.groundHeightAt(avatarLeftCorr) - Avatar.AVATAR_HEIGHT;
        Vector2 initialAvatarLocation = new Vector2(avatarLeftCorr, avatarTopCorr);
        avatar = Avatar.create(gameObjectCollection, Layer.DEFAULT,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(
                avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()
        ));
        currentMiddleX = avatar.getCenter().x();
        blocksSideFromAvatar = (int) Math.floor(terrain.getWidthInBlocks() / 2f);

        EnergyText energyText = new EnergyText(new Vector2(10, 10), new Vector2(40, 40));
        this.gameObjects().addGameObject(energyText, Layer.BACKGROUND + 1);

    }

    private void createWorldInRange(int minX, int maxX) {
        terrain.createInRange(minX, maxX);
        tree.createInRange(minX, maxX);
    }

    private void removeWorldInRange(int minX, int maxX) {
        terrain.removeInRange(minX, maxX);
        tree.RemoveInRange(minX, maxX);
    }

    private void updateEndlessWorld() {
        double movementSize = avatar.getCenter().x() - currentMiddleX;
        int distToAdd = 0;
        if (Math.abs(movementSize) > (Terrain.WORLD_BUFFER / 2f)) { // We moved
            // We moved right
            if (movementSize > (Terrain.WORLD_BUFFER / 2f)) {
                int numBlocksRight = (int) (Math.floor((worldEndRight - avatar.getCenter().x()) / Block.SIZE));
                distToAdd = (blocksSideFromAvatar - numBlocksRight) * Block.SIZE;
                createWorldInRange((int) worldEndRight, (int) worldEndRight + distToAdd);
                removeWorldInRange((int) worldEndLeft, (int) worldEndLeft + distToAdd);
                worldEndLeft = worldEndLeft + distToAdd;
                worldEndRight = worldEndRight + distToAdd;
                // We moved left
            } else if (movementSize < -(Terrain.WORLD_BUFFER / 2f)) {
                int numBlocksLeft = (int) (Math.floor((avatar.getCenter().x() - worldEndLeft) / Block.SIZE));
                distToAdd = (blocksSideFromAvatar - numBlocksLeft) * Block.SIZE;
                createWorldInRange((int) worldEndLeft - distToAdd, (int) worldEndLeft);
                removeWorldInRange((int) worldEndRight - distToAdd, (int) worldEndRight);
                worldEndLeft = worldEndLeft - distToAdd;
                worldEndRight = worldEndRight - distToAdd;
            }
            currentMiddleX = avatar.getCenter().x();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEndlessWorld();
    }
}

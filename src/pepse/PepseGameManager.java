package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
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
    private static final Color HALO_COLOR = new Color(255, 255,0, 20);

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

        // Create world
        GameObject sky = Sky.create(gameObjectCollection, windowDimensions, Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjectCollection, Layer.STATIC_OBJECTS, windowDimensions, 0);
        GameObject night = Night.create(gameObjectCollection, Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjectCollection,Layer.BACKGROUND + 1, windowDimensions,CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(gameObjectCollection,Layer.BACKGROUND + 2, sun, HALO_COLOR);
        sunHalo.addComponent(deltaTime-> {sunHalo.setCenter(sun.getCenter());});
        Tree tree = new Tree(gameObjectCollection, Layer.STATIC_OBJECTS, windowDimensions,
                terrain::groundHeightAt);
        tree.createInRange(0, (int)windowDimensions.x());

        // Create avatar
        float avatarLeftCorr = (float) (Block.SIZE * (Math.floor((windowDimensions.x() / 2) / Block.SIZE)));
        float avatarTopCorr = terrain.groundHeightAt(avatarLeftCorr) - Avatar.AVATAR_HEIGHT;
        Vector2 initialAvatarLocation = new Vector2(avatarLeftCorr, avatarTopCorr);
        Avatar avatar = Avatar.create(gameObjectCollection, Layer.DEFAULT,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(
                avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()
        ));
    }
}

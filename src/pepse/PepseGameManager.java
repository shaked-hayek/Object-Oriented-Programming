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
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;


public class PepseGameManager extends GameManager {
    private ImageReader imageReader;
    private SoundReader soundReader;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private GameObjectCollection gameObjectCollection;

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

        GameObject sky = Sky.create(gameObjectCollection, windowDimensions, Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjectCollection, Layer.STATIC_OBJECTS, windowDimensions, 0);
        GameObject night = Night.create(gameObjectCollection, Layer.FOREGROUND, windowDimensions, 30);
        GameObject sun = Sun.create(gameObjectCollection,Layer.BACKGROUND+1, windowDimensions,30);
        GameObject sunHalo = SunHalo.create(gameObjectCollection,Layer.BACKGROUND+2, sun,
                new Color(255, 255,0, 20));
        sunHalo.addComponent(deltaTime-> {sunHalo.setCenter(sun.getCenter());});
    }
}

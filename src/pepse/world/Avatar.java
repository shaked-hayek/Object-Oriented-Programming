package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;


public class Avatar extends GameObject {
    public static final float AVATAR_HEIGHT = 34;
    public static final float AVATAR_WIDTH = 30;
    public static final float INIT_ENERGY = 100;
    public static final float MIN_ENERGY = 0;
    public static final double ENERGY_CHANGE = 0.5;
    public static final int VELOCITY_X = 300;
    public static final int VELOCITY_Y = 300;
    public static final int ACCELERATION_Y = 400;
    public static final int FLY_Y = 100;
    private static final String AVATAR_IMG_FOLDER = "assets/";
    private static final String AVATAR_STANDING = "avatar_standing.png";
    private static final String TAG_NAME = "avatar";
    private static UserInputListener inputListener;
    private static double energy;
    private static boolean flyingMode;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        energy = INIT_ENERGY;
        flyingMode = false;
        transform().setAccelerationY(ACCELERATION_Y);
    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.inputListener = inputListener;
        Renderable avatarStandingImg = imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_STANDING, true);
        Avatar avatar = new Avatar(
                topLeftCorner,
                new Vector2(AVATAR_HEIGHT, AVATAR_WIDTH),
                avatarStandingImg);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(TAG_NAME);
        return avatar;
    }

    private void horizontalMovement() {
        transform().setVelocityX(0);
        // Move left
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            renderer().setIsFlippedHorizontally(true);
            transform().setVelocityX(-VELOCITY_X);
        }
        // Move right
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            renderer().setIsFlippedHorizontally(false);
            transform().setVelocityX(VELOCITY_X);
        }
    }

    private void jump() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityX(0);
            transform().setVelocityY(-VELOCITY_Y);
        }
    }

    private void fly() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                energy > MIN_ENERGY) {
            transform().setVelocityY(- (VELOCITY_Y + FLY_Y));
            flyingMode = true;
        } else {
            flyingMode = false;
        }
    }

    private void updateEnergy() {
        if (flyingMode) {
            if (energy < INIT_ENERGY) {
                energy += ENERGY_CHANGE;
            }
        } else {
            if (energy > MIN_ENERGY) {
                energy -= ENERGY_CHANGE;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        horizontalMovement();
        jump();
        fly();
        updateEnergy();
    }

}

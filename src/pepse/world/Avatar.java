package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transform;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;


public class Avatar extends GameObject {
    public static final float AVATAR_HEIGHT = 34;
    public static final float AVATAR_WIDTH = 30;
    public static final float INIT_ENERGY = 100;
    public static final double ENERGY_SUBTRACT = 0.5;
    public static final int VELOCITY_X = 300;
    public static final int VELOCITY_Y = -300;
    public static final int ACCELERATION_Y = 500;
    private static final String AVATAR_IMG_FOLDER = "assets/";
    private static final String AVATAR_STANDING = "avatar_standing.png";
    private static final String TAG_NAME = "avatar";
    private static UserInputListener inputListener;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    ()->physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            transform().setVelocityY(VELOCITY_Y);
    }

}

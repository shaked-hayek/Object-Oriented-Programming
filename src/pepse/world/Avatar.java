package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transform;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class Avatar extends GameObject {
    public static final float AVATAR_HEIGHT = 34;
    public static final float AVATAR_WIDTH = 30;
    public static final float INIT_ENERGY = 100;
    public static final double ENERGY_SUBTRACT = 0.5;
    public static final int HORIZONTAL_SPEED = 300;
    public static final int ACCELERATION_Y = 500;
    private static final String AVATAR_IMG_FOLDER = "assets/";
    private static final String AVATAR_STANDING = "avatar_standing.png";
    private static final String TAG_NAME = "avatar";

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

        transform().setAccelerationY(ACCELERATION_Y);
    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
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



}

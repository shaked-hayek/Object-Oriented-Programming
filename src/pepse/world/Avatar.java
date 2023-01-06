package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;


public class Avatar extends GameObject {
    private static final String AVATAR_IMG_FOLDER = "assets/";
    private static final String AVATAR_STANDING = "avatar_standing.png";
    private static final float AVATAR_HEIGHT = 72;
    private static final float AVATAR_WIDTH = 64;
    private static final String TAG_NAME = "avatar";

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
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
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(TAG_NAME);
        return avatar;
    }
}

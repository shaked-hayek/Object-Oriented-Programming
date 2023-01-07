package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Objects;


public class Avatar extends GameObject {
    public static final float AVATAR_HEIGHT = 34;
    public static final float AVATAR_WIDTH = 30;
    private static final float INIT_ENERGY = 100;
    private static final float MIN_ENERGY = 0;
    private static final double ENERGY_CHANGE = 0.5;
    private static final int VELOCITY_X = 200;
    private static final int VELOCITY_Y = 200;
    private static final int ACCELERATION_Y = 200;
    private static final float ANIMATION_TIME = .5f;
    private static final String AVATAR_IMG_FOLDER = "assets/";
    private static final String AVATAR_STAND = "avatar_stand.png";
    private static final String AVATAR_WALK = "avatar_walk_1.png";
    private static final String AVATAR_WALK_2 = "avatar_walk_2.png";
    private static final String AVATAR_JUMP_UP = "avatar_jump_up.png";
    private static final String AVATAR_JUMP_DOWN = "avatar_jump_down.png";
    private static final String AVATAR_FLY = "avatar_fly_1.png";
    private static final String AVATAR_FLY_2 = "avatar_fly_2.png";
    private static final String TAG_NAME = "avatar";
    private static UserInputListener inputListener;
    private static ImageReader imageReader;
    private static Renderable animationStanding;
    private static AnimationRenderable animationWalking;
    private static Renderable animationJumpingUp;
    private static Renderable animationJumpingDown;
    private static AnimationRenderable animationFlying;
    private static double energy;
    private static boolean isFlying = false;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        energy = INIT_ENERGY;
        transform().setAccelerationY(ACCELERATION_Y);
    }

    private static void createAnimations() {
        animationStanding = imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_STAND, true);
        animationWalking = new AnimationRenderable(new Renderable[] {
                imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_WALK, true),
                imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_WALK_2, true)
        }, ANIMATION_TIME);
        animationJumpingUp = imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_JUMP_UP, true);
        animationJumpingDown = imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_JUMP_DOWN, true);
        animationFlying = new AnimationRenderable(new Renderable[] {
                imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_FLY, true),
                imageReader.readImage(AVATAR_IMG_FOLDER + AVATAR_FLY_2, true)
        }, ANIMATION_TIME);
    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;
        createAnimations();

        Avatar avatar = new Avatar(
                topLeftCorner,
                new Vector2(AVATAR_HEIGHT, AVATAR_WIDTH),
                animationStanding);
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
            transform().setVelocityY(-VELOCITY_Y);
            isFlying = true;
        }
    }

    private void updateEnergy() {
        if (energy == MIN_ENERGY) {
            isFlying = false;
        }
        if (getVelocity().y() == 0 && !isFlying) {
            if (energy < INIT_ENERGY) {
                energy += ENERGY_CHANGE;
            }
        } else if (isFlying) {
            if (energy > MIN_ENERGY) {
                energy -= ENERGY_CHANGE;
            }
        }
    }

    private void updateAnimation() {
        if (getVelocity().x() != 0 && getVelocity().y() == 0) {
            renderer().setRenderable(animationWalking);
        } else if (isFlying) {
            renderer().setRenderable(animationFlying);
        } else if (getVelocity().y() > 0) {
            renderer().setRenderable(animationJumpingDown);
        } else if (getVelocity().y() < 0) {
            renderer().setRenderable(animationJumpingUp);
        } else {
            renderer().setRenderable(animationStanding);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        horizontalMovement();
        jump();
        fly();
        updateEnergy();
        updateAnimation();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        isFlying = false;
        if (Objects.equals(other.getTag(), Terrain.TAG_NAME)) {
            transform().setVelocityY(0);
        }
    }
}

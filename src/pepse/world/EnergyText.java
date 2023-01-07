package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.world.Avatar.getEnergy;

public class EnergyText extends GameObject {
    public static final Color GREEN = new Color(47, 200, 30);
    private static final String TEXT_ENERGY = "Energy Left: %.1f";
    private static final String FONT_NAME = "Impact";
    private static final double HIGH_ENERGY = 70;
    private static final double LOW_ENERGY = 10;


    private final TextRenderable textRenderable;

    public EnergyText(Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions, null);

        TextRenderable textRenderable = new TextRenderable(String.format(TEXT_ENERGY, getEnergy()),
                FONT_NAME);
        this.renderer().setRenderable(textRenderable);
        textRenderable.setColor(Color.GREEN);
        this.textRenderable = textRenderable;

        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getEnergy()>HIGH_ENERGY){
            textRenderable.setColor(GREEN);
        }
        else if (getEnergy() < LOW_ENERGY) {
            textRenderable.setColor(Color.RED);
        }
        else {
            textRenderable.setColor(Color.YELLOW);
        }
        textRenderable.setString(String.format(TEXT_ENERGY, getEnergy()));
    }
}

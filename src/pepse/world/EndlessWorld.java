package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class EndlessWorld extends GameObject {

    private Terrain terrain;

    public EndlessWorld(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        Terrain terrain) {
        super(topLeftCorner, dimensions, renderable);
        this.terrain = terrain;
    }

    private void addToWorld() {
//        terrain.createInRange();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        addToWorld();
    }
}

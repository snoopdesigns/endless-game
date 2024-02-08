package org.snoopdesigns.endless.world;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;
import org.snoopdesigns.endless.world.ship.SteerableEnemyShip;

public class ObjectsRenderer implements Renderer {

    private final List<Renderable> renderables = new ArrayList<>();

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        renderables.add(Context.getInstance().getPlayerShip());

        IntStream.range(0, 1).forEach(i ->
                renderables.add(new SteerableEnemyShip()));

        renderables.forEach(Renderable::create);
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(Context.getInstance().getCameraContext().getCameraProjection());
        batch.begin();
        renderables.forEach(renderable -> renderable.render(batch));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderables.forEach(Renderable::dispose);
    }
}

package org.snoopdesigns.endless.physics;

import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;

public class Box2DDebugRenderer implements Renderer {

    private com.badlogic.gdx.physics.box2d.Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        debugRenderer = new com.badlogic.gdx.physics.box2d.Box2DDebugRenderer();
        debugRenderer.setDrawBodies(true);
        debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawAABBs(true);
    }

    @Override
    public void render() {
        debugRenderer.render(
                Context.getInstance().getWorldContext().getWorld(),
                Context.getInstance().getCameraContext().getCameraProjection());
    }

    @Override
    public void dispose() {
    }
}

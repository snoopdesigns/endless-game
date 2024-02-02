package org.snoopdesigns.endless.physics;

import com.badlogic.gdx.physics.box2d.Box2D;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;

public class Box2DRenderer implements Renderer {

    @Override
    public void create() {
        Box2D.init();
    }

    @Override
    public void render() {
        Context.getInstance().getWorldContext().getWorld().step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {
    }
}

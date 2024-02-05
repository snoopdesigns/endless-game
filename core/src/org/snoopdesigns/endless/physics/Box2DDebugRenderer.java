package org.snoopdesigns.endless.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;

public class Box2DDebugRenderer implements Renderer {

    private com.badlogic.gdx.physics.box2d.Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    private SpriteBatch batch;

    @Override
    public void create() {
        debugRenderer = new com.badlogic.gdx.physics.box2d.Box2DDebugRenderer();
        debugRenderer.setDrawBodies(true);
        debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawAABBs(true);

        font = new BitmapFont(Gdx.files.internal("calibri.fnt"), false);
        font.setColor(Color.CYAN);
        font.getData().setScale(0.15f);

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        debugRenderer.render(
                Context.getInstance().getWorldContext().getWorld(),
                Context.getInstance().getCameraContext().getCameraProjection());

        final Array<Body> bodies = new Array<>();
        Context.getInstance().getWorldContext().getWorld().getBodies(bodies);

        batch.setProjectionMatrix(Context.getInstance().getCameraContext().getCameraProjection());
        batch.begin();
        bodies.forEach(body -> {
            final String debugText = String.format("""
                p %.1f %.1f
                a %.1f
                s %.1f m/s
                v %.1f %.1f
                """,
                    Context.getInstance().getCameraContext().getPosition().x,
                    Context.getInstance().getCameraContext().getPosition().y,
                    Context.getInstance().getPlayerShip().getBody().getAngle(),
                    Context.getInstance().getPlayerShip().getBody().getLinearVelocity().len(),
                    Context.getInstance().getPlayerShip().getBody().getLinearVelocity().x,
                    Context.getInstance().getPlayerShip().getBody().getLinearVelocity().y);
            font.draw(batch, debugText, body.getPosition().x + 5f, body.getPosition().y - 5f);
            //ont.draw(batch, debugText, 10f, 190f);
        });
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

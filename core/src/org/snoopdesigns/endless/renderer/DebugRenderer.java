package org.snoopdesigns.endless.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.snoopdesigns.endless.context.Context;

public class DebugRenderer implements Renderer {

    private BitmapFont font;
    private SpriteBatch batch;

    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("calibri.fnt"), false);
        font.setColor(Color.GREEN);
        font.getData().setScale(0.7f);
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        batch.begin();
        final String debugText = String.format("""
                window size %d x %d
                viewport %f %f
                zoom %f
                position %f %f
                angle %f
                speed %f
                velocity %f %f
                """,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                Context.getInstance().getCameraContext().getViewport().x,
                Context.getInstance().getCameraContext().getViewport().y,
                Context.getInstance().getCameraContext().getZoom(),
                Context.getInstance().getCameraContext().getPosition().x,
                Context.getInstance().getCameraContext().getPosition().y,
                Context.getInstance().getPlayerShip().getBody().getAngle(),
                Context.getInstance().getPlayerShip().getBody().getLinearVelocity().len(),
                Context.getInstance().getPlayerShip().getBody().getLinearVelocity().x,
                Context.getInstance().getPlayerShip().getBody().getLinearVelocity().y);
        font.draw(batch, debugText, 10f, 190f);
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}

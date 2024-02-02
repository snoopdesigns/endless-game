package org.snoopdesigns.endless.world.bg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;

public class BackgroundRenderer implements Renderer {

    private SpriteBatch batch;
    private Sprite bgSprite;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bgSprite = new Sprite(new Texture(Gdx.files.internal("bg.png")));
        bgSprite.setCenter(0, 0);
        bgSprite.setSize(1000, 1000);
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(Context.getInstance().getCameraContext().getCameraProjection());
        batch.begin();
        bgSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

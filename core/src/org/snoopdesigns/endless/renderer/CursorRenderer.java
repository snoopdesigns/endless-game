package org.snoopdesigns.endless.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class CursorRenderer implements Renderer {

    private Pixmap crosshair;

    @Override
    public void create() {
        crosshair = new Pixmap(Gdx.files.internal("crosshair.png"));
        final Cursor cursor = Gdx.graphics.newCursor(crosshair,
                crosshair.getWidth() / 2,
                crosshair.getHeight() / 2);
        Gdx.graphics.setCursor(cursor);
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        crosshair.dispose();
    }
}

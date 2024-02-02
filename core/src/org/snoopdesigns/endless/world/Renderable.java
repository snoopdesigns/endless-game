package org.snoopdesigns.endless.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    void create();
    void render(final SpriteBatch batch);
    void dispose();
}

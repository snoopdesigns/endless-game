package org.snoopdesigns.endless.renderer;

import org.snoopdesigns.endless.input.DefaultInputProcessor;

public interface Renderer extends DefaultInputProcessor {
    void create();
    void render();
    void dispose();
    default void resize(int width, int height) {
    }
}

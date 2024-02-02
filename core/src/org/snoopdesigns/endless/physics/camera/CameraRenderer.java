package org.snoopdesigns.endless.physics.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.renderer.Renderer;

public class CameraRenderer implements Renderer {

    private static final float DEFAULT_WORLD_WIDTH = 300.0f;
    private static final float DEFAULT_WORLD_HEIGHT = 300.0f;

    private OrthographicCamera camera;
    private Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom = Context.getInstance().getCameraContext().getZoom();
        viewport = new ExtendViewport(
                Context.getInstance().getCameraContext().getViewport().x,
                Context.getInstance().getCameraContext().getViewport().y,
                camera);
        camera.update();
        Context.getInstance().getCameraContext().setCameraProjection(camera.combined);
    }

    @Override
    public void render() {
        final Vector2 cameraPosition = Context.getInstance().getCameraContext().getPosition();
        camera.position.set(cameraPosition.x, cameraPosition.y, 0);
        camera.update();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        Context.getInstance().getCameraContext().getViewport().set(
                camera.viewportWidth,
                camera.viewportHeight);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY / 10f;
        Context.getInstance().getCameraContext().setZoom(camera.zoom);
        return true;
    }
}

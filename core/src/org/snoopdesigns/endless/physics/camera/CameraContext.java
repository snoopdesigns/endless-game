package org.snoopdesigns.endless.physics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public final class CameraContext {

    private static final float DEFAULT_WORLD_WIDTH = 300.0f;

    private final Vector2 position;
    private final Vector2 viewport;
    private float zoom;
    private Matrix4 cameraProjection;

    public CameraContext() {
        final float w = Gdx.graphics.getWidth();
        final float h = Gdx.graphics.getHeight();

        this.position = new Vector2(0, 0);
        this.zoom = 1.0f;
        this.viewport = new Vector2(DEFAULT_WORLD_WIDTH, DEFAULT_WORLD_WIDTH * (h / w));
    }

    public Matrix4 getCameraProjection() {
        return cameraProjection;
    }

    public void setCameraProjection(Matrix4 cameraProjection) {
        this.cameraProjection = cameraProjection;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public Vector2 getViewport() {
        return viewport;
    }
}

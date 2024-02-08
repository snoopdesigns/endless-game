package org.snoopdesigns.endless.world.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.physics.PhysicalBody;
import org.snoopdesigns.endless.world.Controllable;
import org.snoopdesigns.endless.world.Renderable;
import org.snoopdesigns.endless.world.effects.EngineEffect;

public final class PlayerShip extends PhysicalBody implements Controllable, Renderable {
    private Sprite sprite;
    private EngineEffect effect;

    @Override
    public BodyType getBodyType() {
        return BodyType.DynamicBody;
    }

    @Override
    public FixtureDef getFixture() {
        final CircleShape circle = new CircleShape();
        circle.setRadius(5f);
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        return fixtureDef;
    }

    @Override
    public float getMass() {
        return 10000; // 10 tons
    }

    @Override
    public float getLinearDamping() {
        return 1.5f;
    }

    @Override
    public float getMaxVelocity() {
        return 100f; // meters per second
    }

    @Override
    public void create() {
        initBody();

        final Texture texture = new Texture(Gdx.files.internal("ship.png"));
        sprite = new Sprite(texture);

        final float expectedSizeInMeters = 15f;
        final Vector2 scale = new Vector2(
                expectedSizeInMeters / sprite.getHeight(),
                expectedSizeInMeters / sprite.getWidth());

        sprite.setScale(scale.x, scale.y);

        effect = new EngineEffect(this);
    }

    @Override
    public void render(SpriteBatch batch) {
        handleInput();
        handleMousePosition();

        final float x = getBody().getPosition().x;
        final float y = getBody().getPosition().y;
        Context.getInstance().getCameraContext().getPosition().set(x, y);

        sprite.setCenter(x, y);
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());

        sprite.draw(batch);
        effect.render(batch);

        limitVelocity();
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            effect.start();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.SPACE) {
            effect.stop();
            return true;
        }
        return false;
    }

    private void speedUp() {
        final float force = getBody().getMass() * 200; // force 200x times more than self mass
        final Vector2 impulse = new Vector2(force, 0).rotateRad(getBody().getAngle());
        getBody().applyForceToCenter(impulse, true);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            speedUp();
        }
    }

    private void handleMousePosition() {
        final float screenX = Gdx.input.getX();
        final float screenY = Gdx.input.getY();
        final float screenCenterX = (float) Gdx.graphics.getWidth() / 2;
        final float screenCenterY = (float) Gdx.graphics.getHeight() / 2;
        final float effectiveViewportWidth = Context.getInstance().getCameraContext().getViewport().x *
                Context.getInstance().getCameraContext().getZoom();
        final float effectiveViewportHeight = Context.getInstance().getCameraContext().getViewport().y *
                Context.getInstance().getCameraContext().getZoom();

        final float directionX = (screenX - screenCenterX) * (effectiveViewportWidth / Gdx.graphics.getWidth());
        final float directionY = ((Gdx.graphics.getHeight() - screenY) - screenCenterY) * (effectiveViewportHeight / Gdx.graphics.getHeight());

        final Vector2 direction = new Vector2(directionX, directionY);
        float rotationRad = direction.angleRad();

        float c = 10; //speed of rotation
        getBody().setAngularVelocity(c * (angleDifference(rotationRad, getBody().getAngle())));
    }

    public static float angleDifference(float angle1, float angle2) {
        float diff = (angle1 - angle2) % (MathUtils.PI * 2);
        return diff < -1 * MathUtils.PI ? diff + 2 * MathUtils.PI : diff;
    }
}

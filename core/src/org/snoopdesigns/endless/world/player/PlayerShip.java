package org.snoopdesigns.endless.world.player;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

public final class PlayerShip extends PhysicalBody implements Controllable, Renderable {

    private final Queue<ParticleEffect> effects = new LinkedList<>();
    private Sprite sprite;

    @Override
    public BodyType getBodyType() {
        return BodyType.DynamicBody;
    }

    @Override
    public FixtureDef getFixture() {
        final CircleShape circle = new CircleShape();
        circle.setRadius(7f);
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f; // weight of body
        return fixtureDef;
    }

    @Override
    public void create() {
        initBody();

        final Texture texture = new Texture(Gdx.files.internal("ship.png"));
        sprite = new Sprite(texture);
        sprite.setScale(0.1f);
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

        effects.removeIf(ParticleEffect::isComplete);
        effects.forEach(effect -> {
            final Vector2 effectOffset = new Vector2(-5f, 0f).rotateRad(getBody().getAngle());
            effect.setPosition(x + effectOffset.x, y + effectOffset.y);
            effect.getEmitters().forEach(emitter -> {
                final float effectRotation = MathUtils.radDeg * getBody().getAngle();
                emitter.getAngle().setLow(effectRotation);
                emitter.getAngle().setHigh(effectRotation);
            });
        });

        sprite.draw(batch);
        effects.forEach(effect ->
                effect.draw(batch, Gdx.graphics.getDeltaTime()));
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            final ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("particles.p"), Gdx.files.internal(""));
            effect.setPosition(
                    getBody().getPosition().x,
                    getBody().getPosition().y);
            effect.start();
            effects.add(effect);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.SPACE) {
            effects.forEach(ParticleEffect::allowCompletion);
            return true;
        }
        return false;
    }

    private void speedUp() {
        final float force = 100000.0f;
        final Vector2 impulse = new Vector2(force, 0).rotateRad(getBody().getAngle());
        getBody().applyForceToCenter(impulse, true);
    }

    private void slowDown() {
        getBody().setLinearVelocity(
                getBody().getLinearVelocity().x * 0.98f,
                getBody().getLinearVelocity().y * 0.98f);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            speedUp();
        } else {
            slowDown();
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

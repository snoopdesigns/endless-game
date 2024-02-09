package org.snoopdesigns.endless.world.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.snoopdesigns.endless.physics.SteerablePhysicalBody;
import org.snoopdesigns.endless.world.Renderable;
import org.snoopdesigns.endless.world.effects.EngineEffect;
import org.snoopdesigns.endless.world.steering.ChaseSteering;
import org.snoopdesigns.endless.world.steering.Steering;
import org.snoopdesigns.endless.world.steering.SteeringApplicator;

public class SteerableEnemyShip extends SteerablePhysicalBody implements Renderable {

    private Sprite sprite;
    private EngineEffect engineEffect;
    private Steering steering;

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
        return fixtureDef;
    }

    @Override
    public Vector2 getInitialPosition() {
        return new Vector2(MathUtils.random(150), MathUtils.random(150));
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
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());

        engineEffect = new EngineEffect(this);
        steering = new ChaseSteering(this, 120f, 90f, new SteeringApplicator() {

            private boolean speedUp = false;

            @Override
            public void applyAngularVelocity(float angular) {
                getBody().setAngularVelocity(angular);
            }

            @Override
            public void applyLinearVelocity(Vector2 linear) {
                speedUp(linear);
                if (!speedUp) {
                    engineEffect.start();
                }
                speedUp = true;
            }

            @Override
            public void stopLinearVelocity() {
                engineEffect.stop();
                speedUp = false;
            }
        });
    }
    @Override
    public void render(SpriteBatch batch) {
        steering.calculate();

        sprite.setCenter(getBody().getPosition().x, getBody().getPosition().y);
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());
        sprite.draw(batch);

        engineEffect.render(batch);

        // TODO
        limitVelocity();
    }

    @Override
    public void dispose() {
    }

    private void speedUp(final Vector2 force) {
        final float forceToApply = getBody().getMass() * 200; // force 200x times more than self mass
        final Vector2 impulse = new Vector2(forceToApply, 0).rotateRad(getBody().getAngle());
        getBody().applyForceToCenter(impulse, true);
    }

    @Override
    public float getMass() {
        return 10000;
    }

    @Override
    public float getLinearDamping() {
        return 1.5f;
    }

    @Override
    public float getMaxVelocity() {
        return getMaxSpeed();
    }

    @Override
    public float getMaxSpeed() {
        return 90f;
    }

    @Override
    public float getMaxAcceleration() {
        return 50000f;
    }

    @Override
    public float getMaxRotationSpeed() {
        return 5f;
    }

    @Override
    public float getMaxRotationAcceleration() {
        return 5f;
    }
}

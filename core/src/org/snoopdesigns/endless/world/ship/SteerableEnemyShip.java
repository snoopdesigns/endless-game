package org.snoopdesigns.endless.world.ship;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.physics.Box2DLocation;
import org.snoopdesigns.endless.physics.SteerablePhysicalBody;
import org.snoopdesigns.endless.world.Renderable;

public class SteerableEnemyShip extends SteerablePhysicalBody implements Renderable {

    private Sprite sprite;
    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    private BlendedSteering<Vector2> steeringCombination;

    private Location<Vector2> target;

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
    public float getMass() {
        return 10000;
    }

    @Override
    public float getLinearDamping() {
        return 1.5f;
    }

    @Override
    public float getMaxVelocity() {
        return 50;
    }

    @Override
    public Vector2 getInitialPosition() {
        return new Vector2(MathUtils.random(50), MathUtils.random(50));
    }

    @Override
    public void create() {
        initBody();
        final Texture texture = new Texture(Gdx.files.internal("ship.png"));
        sprite = new Sprite(texture);
        sprite.setScale(0.1f);
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());

        target = new Box2DLocation();
        target.getPosition().set(
                Context.getInstance().getPlayerShip().getBody().getPosition().x,
                Context.getInstance().getPlayerShip().getBody().getPosition().y);
        target.setOrientation(Context.getInstance().getPlayerShip().getBody().getAngle());

        steeringCombination = new BlendedSteering<>(this);
        getSteeringBehaviours().forEach(steeringBehaviour ->
                steeringCombination.add(steeringBehaviour, 0.5f));
    }
    @Override
    public void render(SpriteBatch batch) {
        target.getPosition().set(
                Context.getInstance().getPlayerShip().getBody().getPosition().x,
                Context.getInstance().getPlayerShip().getBody().getPosition().y);
        target.setOrientation(Context.getInstance().getPlayerShip().getBody().getAngle());

        steeringCombination.calculateSteering(steeringOutput);
        applySteering();

        sprite.setCenter(getBody().getPosition().x, getBody().getPosition().y);
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());
        sprite.draw(batch);

        limitVelocity();
    }

    @Override
    public void dispose() {
    }

    private List<SteeringBehavior<Vector2>> getSteeringBehaviours() {
        return List.of(
                new Face<>(this, target),
                /*new Arrive<>(this, target)
                        .setTimeToTarget(0.1f)
                        .setArrivalTolerance(1f)
                        .setDecelerationRadius(100f)*/
                new Wander<>(this)
                        .setTarget(target)
                        .setFaceEnabled(true) // We want to use Face internally (independent facing is on)
                        .setAlignTolerance(0.001f) // Used by Face
                        .setDecelerationRadius(100f) // Used by Face
                        .setTimeToTarget(0.1f) // Used by Face
                        .setWanderOffset(200) //
                        .setWanderOrientation(10) //
                        .setWanderRadius(200f) //
                        .setWanderRate(MathUtils.PI2 * 4)
        );
    }

    private void applySteering() {
        // Update position and linear velocity.
        if (steeringOutput.angular != 0) {
            // this method internally scales the torque by deltaTime
            getBody().setAngularVelocity(steeringOutput.angular);
        } else {
            // Update orientation and angular velocity
            final Vector2 linearVelocity = getLinearVelocity();
            if (!linearVelocity.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = linearVelocity.angleRad();
                getBody().setAngularVelocity(
                        getMaxRotationAcceleration() *
                        angleDifference(newOrientation, getBody().getAngle()));
            }
        }

        if (!steeringOutput.linear.isZero()) {
            speedUp(steeringOutput.linear);
        }
    }

    private void speedUp(final Vector2 force) {
        getBody().applyForceToCenter(
                force.x * 100,
                force.y * 100,
                true);
    }

    // TODO common
    private float angleDifference(float angle1, float angle2) {
        float diff = (angle1 - angle2) % (MathUtils.PI * 2);
        return diff < -1 * MathUtils.PI ? diff + 2 * MathUtils.PI : diff;
    }

    @Override
    public float getMaxSpeed() {
        return 70f;
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

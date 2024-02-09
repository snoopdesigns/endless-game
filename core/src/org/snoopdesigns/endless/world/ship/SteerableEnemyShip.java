package org.snoopdesigns.endless.world.ship;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
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
import org.snoopdesigns.endless.world.effects.EngineEffect;

public class SteerableEnemyShip extends SteerablePhysicalBody implements Renderable {

    private Sprite sprite;
    private Sprite targetSprite; //target position
    private Sprite farAttackPointSprite; //farAttackPoint
    private Sprite faceSprite; //face position
    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    private BlendedSteering<Vector2> steeringCombination;

    private Location<Vector2> positionTarget;
    private Location<Vector2> faceTarget;
    private float targetDirection;

    private final Vector2 lastVelocity = new Vector2(0f, 0f);

    private EngineEffect engineEffect;

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
        return 80f;
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

        final Texture targetTexture = new Texture(Gdx.files.internal("target.png"));
        targetSprite = new Sprite(targetTexture);
        targetSprite.setScale(0.03f);
        farAttackPointSprite = new Sprite(targetTexture);
        farAttackPointSprite.setScale(0.02f);
        faceSprite = new Sprite(new Texture(Gdx.files.internal("face.png")));
        faceSprite.setScale(0.02f);

        positionTarget = new Box2DLocation();
        faceTarget = new Box2DLocation();

        engineEffect = new EngineEffect(this);

        steeringCombination = new BlendedSteering<>(this);
        getSteeringBehaviours().forEach(steeringBehaviour ->
                steeringCombination.add(steeringBehaviour, 0.5f));

        targetDirection = MathUtils.random(MathUtils.PI2);
    }
    @Override
    public void render(SpriteBatch batch) {
        final float maxDistance = 100f;
        final float minDistance = 70f;
        final Vector2 targetDisplacement = new Vector2(maxDistance, 0f).rotateRad(targetDirection);
        final Vector2 farAttackPoint = new Vector2(
                Context.getInstance().getPlayerShip().getBody().getPosition().x + targetDisplacement.x,
                Context.getInstance().getPlayerShip().getBody().getPosition().y + targetDisplacement.y);
        final float distanceToPlayer = new Vector2(
                getBody().getPosition().x - Context.getInstance().getPlayerShip().getBody().getPosition().x,
                getBody().getPosition().y - Context.getInstance().getPlayerShip().getBody().getPosition().y)
                .len();
        if (distanceToPlayer > maxDistance + 10f) {
            // Rotate target rotation point only of player is far away
            targetDirection += Gdx.graphics.getDeltaTime() / 2f;
        }
        final Vector2 displacement = targetDisplacement.limit(Math.max(0, distanceToPlayer - minDistance));
        final Vector2 targetPos = new Vector2(
                Context.getInstance().getPlayerShip().getBody().getPosition().x + displacement.x,
                Context.getInstance().getPlayerShip().getBody().getPosition().y + displacement.y);

        positionTarget.getPosition().set(targetPos);
        //positionTarget.setOrientation(Context.getInstance().getPlayerShip().getBody().getAngle());
        faceTarget.getPosition().set(positionTarget.getPosition());

        steeringCombination.calculateSteering(steeringOutput);
        if (distanceToPlayer <= minDistance) { // do not move, if close to player, only rotate
            steeringOutput.linear.setZero();
        }
        applySteering();

        sprite.setCenter(getBody().getPosition().x, getBody().getPosition().y);
        sprite.setRotation(MathUtils.radDeg * getBody().getAngle());
        sprite.draw(batch);

        farAttackPointSprite.setCenter(
                farAttackPoint.x,
                farAttackPoint.y);
        farAttackPointSprite.draw(batch);
        targetSprite.setCenter(
                positionTarget.getPosition().x,
                positionTarget.getPosition().y);
        targetSprite.draw(batch);
        faceSprite.setCenter(
                faceTarget.getPosition().x,
                faceTarget.getPosition().y);
        faceSprite.draw(batch);

        engineEffect.render(batch);

        // TODO
        limitVelocity();
    }

    @Override
    public void dispose() {
    }

    private List<SteeringBehavior<Vector2>> getSteeringBehaviours() {
        return List.of(
                new Face<>(this, faceTarget),
                new Arrive<>(this, positionTarget)
                        .setTimeToTarget(0.1f)
                        .setArrivalTolerance(10f)
                        .setDecelerationRadius(100f)
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
            if (steeringOutput.linear.len() >= lastVelocity.len() - 10) {
                speedUp(steeringOutput.linear);
                engineEffect.start();
            } else {
                engineEffect.stop();
            }
            lastVelocity.x = steeringOutput.linear.x;
            lastVelocity.y = steeringOutput.linear.y;
        } else {
            engineEffect.stop();
        }
    }

    private void speedUp(final Vector2 force) {
        /*getBody().applyForceToCenter(
                force.x * 10000,
                force.y * 10000,
                true);*/

        final float forceToApply = getBody().getMass() * 200; // force 200x times more than self mass
        final Vector2 impulse = new Vector2(forceToApply, 0).rotateRad(getBody().getAngle());
        getBody().applyForceToCenter(impulse, true);
    }

    // TODO common
    private float angleDifference(float angle1, float angle2) {
        float diff = (angle1 - angle2) % (MathUtils.PI * 2);
        return diff < -1 * MathUtils.PI ? diff + 2 * MathUtils.PI : diff;
    }

    @Override
    public float getMaxSpeed() {
        return 100f;
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

package org.snoopdesigns.endless.world.steering;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.snoopdesigns.endless.context.Context;
import org.snoopdesigns.endless.physics.Box2DLocation;
import org.snoopdesigns.endless.physics.SteerablePhysicalBody;

public class ChaseSteering implements Steering {

    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    private final BlendedSteering<Vector2> steeringCombination;
    private final Location<Vector2> positionTarget;
    private final Location<Vector2> faceTarget;
    private final SteeringApplicator steeringApplicator;
    private final SteerablePhysicalBody steerablePhysicalBody;
    private final Vector2 lastVelocity = new Vector2(0f, 0f);
    private final float maxDistance;
    private final float minDistance;
    private float targetDirection;

    public ChaseSteering(final SteerablePhysicalBody steerablePhysicalBody,
                         final float maxDistance,
                         final float minDistance,
                         final SteeringApplicator steeringApplicator) {
        this.steerablePhysicalBody = steerablePhysicalBody;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
        this.steeringApplicator = steeringApplicator;

        positionTarget = new Box2DLocation();
        faceTarget = new Box2DLocation();

        steeringCombination = new BlendedSteering<>(steerablePhysicalBody);
        getSteeringBehaviours().forEach(steeringBehaviour ->
                steeringCombination.add(steeringBehaviour, 0.5f));

        targetDirection = MathUtils.random(MathUtils.PI2);
    }

    @Override
    public void calculate() {
        final Vector2 targetDisplacement = new Vector2(maxDistance, 0f).rotateRad(targetDirection);
        final float distanceToPlayer = new Vector2(
                steerablePhysicalBody.getBody().getPosition().x -
                        Context.getInstance().getPlayerShip().getBody().getPosition().x,
                steerablePhysicalBody.getBody().getPosition().y -
                        Context.getInstance().getPlayerShip().getBody().getPosition().y)
                .len();
        if (distanceToPlayer > maxDistance + 10f) {
            // Rotate target rotation point only of player is far away
            targetDirection += Gdx.graphics.getDeltaTime() / 1f;
        }
        final Vector2 displacement = targetDisplacement.limit(Math.max(0, (distanceToPlayer - minDistance)));
        final Vector2 targetPos = new Vector2(
                Context.getInstance().getPlayerShip().getBody().getPosition().x + displacement.x,
                Context.getInstance().getPlayerShip().getBody().getPosition().y + displacement.y);

        positionTarget.getPosition().set(targetPos);
        faceTarget.getPosition().set(positionTarget.getPosition());

        steeringCombination.calculateSteering(steeringOutput);
        if (distanceToPlayer <= minDistance) { // do not move, if close to player, only rotate
            steeringOutput.linear.setZero();
        }

        applySteering();
    }

    private void applySteering() {
        // Update position and linear velocity.
        if (steeringOutput.angular != 0) {
            // this method internally scales the torque by deltaTime
            steeringApplicator.applyAngularVelocity(steeringOutput.angular);
        }

        if (!steeringOutput.linear.isZero()) {
            if (steeringOutput.linear.len() >= lastVelocity.len() - 10) {
                steeringApplicator.applyLinearVelocity(steeringOutput.linear);
            } else {
                steeringApplicator.stopLinearVelocity();
            }
            lastVelocity.x = steeringOutput.linear.x;
            lastVelocity.y = steeringOutput.linear.y;
        } else {
            steeringApplicator.stopLinearVelocity();
        }
    }

    private List<SteeringBehavior<Vector2>> getSteeringBehaviours() {
        return List.of(
                new Face<>(steerablePhysicalBody, faceTarget),
                new Arrive<>(steerablePhysicalBody, positionTarget)
                        .setTimeToTarget(0.1f)
                        .setArrivalTolerance(10f)
                        .setDecelerationRadius(100f)
        );
    }
}

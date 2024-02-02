package org.snoopdesigns.endless.physics;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public abstract class SteerablePhysicalBody extends PhysicalBody implements Steerable<Vector2> {

    public abstract float getMaxSpeed();
    public abstract float getMaxAcceleration();
    public abstract float getMaxRotationSpeed();
    public abstract float getMaxRotationAcceleration();

    @Override
    public Vector2 getLinearVelocity() {
        return getBody().getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return getBody().getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return 0.001f;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.1f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
    }

    @Override
    public float getMaxLinearSpeed() {
        return getMaxSpeed();
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
    }

    @Override
    public float getMaxLinearAcceleration() {
        return getMaxAcceleration();
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
    }

    @Override
    public float getMaxAngularSpeed() {
        return getMaxRotationSpeed();
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
    }

    @Override
    public float getMaxAngularAcceleration() {
        return getMaxRotationAcceleration();
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    }

    @Override
    public Vector2 getPosition() {
        return getBody().getPosition();
    }

    @Override
    public float getOrientation() {
        return getBody().getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        getBody().setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        final var tmpVector = new Vector2(1, 0).rotateRad(angle);
        outVector.x = tmpVector.x;
        outVector.y = tmpVector.y;
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2DLocation();
    }
}

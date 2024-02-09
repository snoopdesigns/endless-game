package org.snoopdesigns.endless.world.steering;

import com.badlogic.gdx.math.Vector2;

public interface SteeringApplicator {
    void applyAngularVelocity(float angular);
    void applyLinearVelocity(Vector2 linear);
    void stopLinearVelocity();
}

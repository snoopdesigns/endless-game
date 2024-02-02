package org.snoopdesigns.endless.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.snoopdesigns.endless.context.Context;

public abstract class PhysicalBody {

    Body body;

    public abstract BodyType getBodyType();
    public abstract FixtureDef getFixture();

    public Vector2 getInitialPosition() {
        return new Vector2(0, 0);
    }

    public void initBody() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = getBodyType();
        bodyDef.position.set(getInitialPosition());
        body = Context.getInstance().getWorldContext().getWorld().createBody(bodyDef);
        body.createFixture(getFixture());
    }

    public Body getBody() {
        return body;
    }
}

package org.snoopdesigns.endless.world.effects;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.snoopdesigns.endless.physics.PhysicalBody;
import org.snoopdesigns.endless.world.Renderable;

public class EngineEffect implements Renderable {
    private final Queue<ParticleEffect> effects = new LinkedList<>();
    private final PhysicalBody body;

    public EngineEffect(PhysicalBody body) {
        this.body = body;
    }

    public void start() {
        final ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles.p"), Gdx.files.internal(""));
        effect.setPosition(
                body.getBody().getPosition().x,
                body.getBody().getPosition().y);
        effect.start();
        effects.add(effect);
    }

    public void stop() {
        effects.forEach(ParticleEffect::allowCompletion);
    }

    @Override
    public void create() {
    }

    @Override
    public void render(SpriteBatch batch) {
        final float x = body.getBody().getPosition().x;
        final float y = body.getBody().getPosition().y;

        effects.removeIf(ParticleEffect::isComplete);
        effects.forEach(effect -> {
            final Vector2 effectOffset = new Vector2(-5f, 0f).rotateRad(body.getBody().getAngle());
            effect.setPosition(x + effectOffset.x, y + effectOffset.y);
            effect.getEmitters().forEach(emitter -> {
                final float effectRotation = MathUtils.radDeg * body.getBody().getAngle();
                emitter.getAngle().setLow(effectRotation);
                emitter.getAngle().setHigh(effectRotation);
            });
        });

        effects.forEach(effect ->
                effect.draw(batch, Gdx.graphics.getDeltaTime()));
    }

    @Override
    public void dispose() {

    }
}

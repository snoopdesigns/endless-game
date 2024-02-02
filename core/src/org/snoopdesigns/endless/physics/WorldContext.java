package org.snoopdesigns.endless.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldContext {

    private final World world;

    public WorldContext() {
        this.world = new World(new Vector2(0, 0), true);
    }

    public World getWorld() {
        return world;
    }
}

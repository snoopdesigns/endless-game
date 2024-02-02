package org.snoopdesigns.endless.context;

import org.snoopdesigns.endless.physics.WorldContext;
import org.snoopdesigns.endless.physics.camera.CameraContext;
import org.snoopdesigns.endless.world.player.PlayerShip;

public final class Context implements OnInit {
    private static Context INSTANCE;

    private CameraContext cameraContext;
    private WorldContext worldContext;
    private PlayerShip playerShip;

    @Override
    public void create() {
        INSTANCE = new Context();
        INSTANCE.cameraContext = new CameraContext();
        INSTANCE.worldContext = new WorldContext();
        INSTANCE.playerShip = new PlayerShip();
    }

    public static Context getInstance() {
        return INSTANCE;
    }

    public CameraContext getCameraContext() {
        return cameraContext;
    }

    public WorldContext getWorldContext() {
        return worldContext;
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }
}

package org.snoopdesigns.endless.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import org.snoopdesigns.endless.world.Controllable;

public class ExitControllable implements Controllable {

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE) {
            Gdx.app.exit();
            System.exit(1);
        }
        return true;
    }
}

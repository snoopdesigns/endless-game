package org.snoopdesigns.endless;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.snoopdesigns.endless.config.Config;

public final class DesktopLauncher {

	private final static String TITLE = "Endless";

	private DesktopLauncher() {
	}

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(TITLE);
		config.useVsync(true);
		config.setWindowedMode(Config.getScreenWidth(), Config.getScreenHeight());
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		new Lwjgl3Application(new EndlessGame(), config);
	}
}

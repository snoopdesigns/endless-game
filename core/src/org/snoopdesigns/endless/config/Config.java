package org.snoopdesigns.endless.config;

public final class Config {
    private static int screenWidth = 1280;
    private static int screenHeight = 720;

    private Config() {
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}

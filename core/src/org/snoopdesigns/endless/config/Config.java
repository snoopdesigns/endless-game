package org.snoopdesigns.endless.config;

public final class Config {
    private static int screenWidth = 1900;
    private static int screenHeight = 1200;

    private Config() {
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}

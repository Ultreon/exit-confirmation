package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.world.disconnect();
        mc.connect(null);
        mc.setScreen(new TitleScreen());
    }

    public static void saveWorldThen(Runnable runnable) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.world.disconnect();
        mc.connect(null);
        mc.setScreen(new TitleScreen());
        runnable.run();
    }

    public static void saveWorldThenOpen(Screen screen) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.world.disconnect();
        mc.connect(null);
        mc.setScreen(screen);
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> MinecraftClient.getInstance().scheduleStop());
    }
}

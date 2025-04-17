package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.world.disconnect();
        mc.connect(null);
        mc.openScreen(new TitleScreen());
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.world.disconnect();
        mc.connect(null);
        mc.openScreen(new TitleScreen());
        runnable.run();
    }

    public static void saveWorldThenOpen(Screen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.world.disconnect();
        mc.connect(null);
        mc.openScreen(screen);
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getMinecraft().scheduleStop());
    }
}

package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.mixin.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.stat.Stats;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = MinecraftAccessor.getInstance();
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        mc.openScreen(new TitleScreen());
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = MinecraftAccessor.getInstance();
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        runnable.run();
    }

    public static void saveWorldThenOpen(Screen screen) {
        Minecraft mc = MinecraftAccessor.getInstance();
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        mc.openScreen(screen);
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> MinecraftAccessor.getInstance().stop());
    }
}

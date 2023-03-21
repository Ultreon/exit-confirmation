package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.stat.Stats;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = ExitConfirmation.minecraft;
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        mc.openScreen(new TitleScreen());
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = ExitConfirmation.minecraft;
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        runnable.run();
    }

    public static void saveWorldThenOpen(Screen screen) {
        Minecraft mc = ExitConfirmation.minecraft;
        mc.statManager.incrementStat(Stats.leaveGame, 1);
        if (mc.isConnectedToServer()) {
            mc.level.disconnect();
        }

        mc.setLevel(null);
        mc.openScreen(screen);
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> ExitConfirmation.minecraft.stop());
    }
}

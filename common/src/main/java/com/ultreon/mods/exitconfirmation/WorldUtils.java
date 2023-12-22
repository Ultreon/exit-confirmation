package com.ultreon.mods.exitconfirmation;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

public final class WorldUtils {
    private static final Component SAVING_LEVEL = Component.translatable("menu.savingLevel");

    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean bl = mc.isLocalServer();
            ServerData serverData = mc.getCurrentServer();
            mc.level.disconnect();
            if (bl) {
                mc.disconnect(new GenericDirtMessageScreen(SAVING_LEVEL));
            } else {
                mc.disconnect();
            }

            TitleScreen titleScreen = new TitleScreen();
            if (bl) {
                mc.setScreen(titleScreen);
            } else if (serverData != null && serverData.isRealm()) {
                mc.setScreen(new RealmsMainScreen(titleScreen));
            } else {
                mc.setScreen(new JoinMultiplayerScreen(titleScreen));
            }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean bl = mc.isLocalServer();
            mc.level.disconnect();
            if (bl) {
                mc.disconnect(new GenericDirtMessageScreen(SAVING_LEVEL));
            } else {
                mc.disconnect();
            }

            runnable.run();
        }
    }

    public static void saveWorldThenOpen(Screen screen) {
        saveWorldThen(() -> Minecraft.getInstance().setScreen(screen));
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getInstance().stop());
    }
}

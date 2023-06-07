package com.ultreon.mods.exitconfirmation;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean flag = mc.isLocalServer();
            boolean flag1 = mc.isConnectedToRealms();
            mc.level.disconnect();
            if (flag) {
                mc.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
            } else {
                mc.clearLevel();
            }

            TitleScreen titleScreen = new TitleScreen();
            if (flag) {
                mc.setScreen(new TitleScreen());
            } else if (flag1) {
                mc.setScreen(new RealmsMainScreen(titleScreen));
            } else {
                mc.setScreen(new JoinMultiplayerScreen(titleScreen));
            }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean flag = mc.isLocalServer();
            mc.level.disconnect();
            if (flag) {
                mc.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
            } else {
                mc.clearLevel();
            }

            runnable.run();
        }
    }

    public static void saveWorldThenOpen(Screen screen) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean flag = mc.isLocalServer();
            mc.level.disconnect();
            if (flag) {
                mc.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
            } else {
                mc.clearLevel();
            }

            mc.setScreen(screen);
        }
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getInstance().stop());
    }
}

package com.ultreon.mods.exitconfirmation;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean flag = mc.isLocalServer();
            boolean flag1 = mc.isConnectedToRealms();
            mc.level.disconnect();
            if (flag) {
                mc.clearLevel(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
            } else {
                mc.clearLevel();
            }

            MainMenuScreen titleScreen = new MainMenuScreen();
            if (flag) {
                mc.setScreen(new MainMenuScreen());
            } else if (flag1) {
                mc.setScreen(new RealmsMainScreen(titleScreen));
            } else {
                mc.setScreen(new MultiplayerScreen(titleScreen));
            }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean flag = mc.isLocalServer();
            mc.level.disconnect();
            if (flag) {
                mc.clearLevel(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
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
                mc.clearLevel(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
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

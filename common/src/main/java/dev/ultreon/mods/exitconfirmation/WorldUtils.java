package dev.ultreon.mods.exitconfirmation;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static net.minecraft.client.gui.screens.PauseScreen.disconnectFromWorld;

public final class WorldUtils {
    private static final Component SAVING_LEVEL = Component.translatable("menu.savingLevel");

    public static void saveWorldThenOpenTitle() {
        Minecraft minecraft = Minecraft.getInstance();

        boolean bl = minecraft.isLocalServer();
        ServerData serverData = minecraft.getCurrentServer();
        if (minecraft.level != null) {
            minecraft.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
        }

        if (bl) {
            minecraft.disconnectWithSavingScreen();
        } else {
            minecraft.disconnectWithProgressScreen();
        }

        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            minecraft.setScreen(titleScreen);
        } else if (serverData != null && serverData.isRealm()) {
            minecraft.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            minecraft.setScreen(new JoinMultiplayerScreen(titleScreen));
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft minecraft = Minecraft.getInstance();

        boolean bl = minecraft.isLocalServer();
        if (minecraft.level != null) {
            minecraft.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
        }

        if (bl) {
            minecraft.disconnectWithSavingScreen();
        } else {
            minecraft.disconnectWithProgressScreen();
        }

        runnable.run();
    }

    public static void saveWorldThenOpen(Screen screen) {
        saveWorldThen(() -> Minecraft.getInstance().setScreen(screen));
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getInstance().stop());
    }
}

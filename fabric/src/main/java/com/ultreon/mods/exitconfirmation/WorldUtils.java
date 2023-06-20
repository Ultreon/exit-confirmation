package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.realms.RealmsBridge;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            boolean bl = client.isIntegratedServerRunning();
            boolean bl2 = client.isConnectedToRealms();
            client.world.disconnect();
            client.connect(null);
            if (bl) {
                client.setScreen(new TitleScreen());
            } else if (bl2) {
                RealmsBridge realmsBridge = new RealmsBridge();
                realmsBridge.switchToRealms(new TitleScreen());
            } else {
                client.setScreen(new MultiplayerScreen(new TitleScreen()));
            }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            boolean bl = client.isIntegratedServerRunning();
            boolean bl2 = client.isConnectedToRealms();
            client.world.disconnect();
            client.connect(null);

            runnable.run();
        }
    }

    public static void saveWorldThenOpen(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            boolean bl = client.isIntegratedServerRunning();
            boolean bl2 = client.isConnectedToRealms();
            client.world.disconnect();
            client.connect(null);

            client.setScreen(screen);
        }
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> MinecraftClient.getInstance().scheduleStop());
    }
}

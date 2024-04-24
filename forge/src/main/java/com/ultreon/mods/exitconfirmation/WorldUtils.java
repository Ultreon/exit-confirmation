package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.realms.RealmsBridge;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null) {
            boolean serverRunning = mc.isIntegratedServerRunning();
            boolean connectedToRealms = mc.isConnectedToRealms();
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
            if (serverRunning) {
                mc.displayGuiScreen(new GuiMainMenu());
            } else if (connectedToRealms) {
                RealmsBridge realmsbridge = new RealmsBridge();
                realmsbridge.switchToRealms(new GuiMainMenu());
            } else {
                mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);

            runnable.run();
        }
    }

    public static void saveWorldThenOpen(GuiScreen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null) {
            boolean flag = mc.isIntegratedServerRunning();
            boolean flag1 = mc.isConnectedToRealms();
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);

            mc.displayGuiScreen(screen);
        }
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getMinecraft().shutdown());
    }
}

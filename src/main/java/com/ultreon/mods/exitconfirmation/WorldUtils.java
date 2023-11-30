package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public final class WorldUtils {
    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
        runnable.run();
    }

    public static void saveWorldThenOpen(GuiScreen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(screen);
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getMinecraft().shutdown());
    }
}

package com.ultreon.mods.exitconfirmation.forge;

import com.ultreon.mods.exitconfirmation.CloseSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.common.MinecraftForge;

public class ExitConfirmationImpl {
    public static void onQuitButtonClick(Minecraft mc, TitleScreen titleScreen, Button widget) {
        boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(CloseSource.QUIT_BUTTON));
        if (!flag) {
            mc.stop();
        }
    }

    public static void onCloseCallback(Minecraft mc, long handle, long window) {
        boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(CloseSource.GENERIC));
        if (!flag) {
            mc.stop();
        }
    }
}

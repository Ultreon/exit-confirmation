package com.ultreon.mods.exitconfirmation.neoforge;

import com.ultreon.mods.exitconfirmation.CloseSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

public class ExitConfirmationImpl {
    public static void onQuitButtonClick(Minecraft mc, TitleScreen titleScreen, Button widget) {
        WindowCloseEvent flag = NeoForge.EVENT_BUS.post(new WindowCloseEvent(CloseSource.QUIT_BUTTON));
        if (!flag.isCanceled()) {
            mc.stop();
        }
    }

    public static void onCloseCallback(Minecraft mc, long handle, long window) {
        WindowCloseEvent flag = NeoForge.EVENT_BUS.post(new WindowCloseEvent(CloseSource.GENERIC));
        if (flag.isCanceled()) {
            GLFW.glfwSetWindowShouldClose(window, false);
        }
    }
}

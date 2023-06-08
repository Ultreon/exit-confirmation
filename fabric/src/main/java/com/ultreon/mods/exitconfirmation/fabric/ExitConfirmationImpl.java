package com.ultreon.mods.exitconfirmation.fabric;

import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.CloseSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import org.lwjgl.glfw.GLFW;

public class ExitConfirmationImpl {
    public static void onQuitButtonClick(Minecraft client, TitleScreen titleScreen, Button widget) {
        ActionResult result = WindowCloseEvent.EVENT.invoker().closing(client.getWindow(), CloseSource.GENERIC);
        if (result != ActionResult.CANCEL) {
            client.stop();
        }
    }

    public static void onCloseCallback(Minecraft client, long handle, long window) {
        ActionResult result = WindowCloseEvent.EVENT.invoker().closing(client.getWindow(), CloseSource.GENERIC);
        if (result == ActionResult.CANCEL) {
            GLFW.glfwSetWindowShouldClose(window, false);
        }
    }
}

package com.ultreon.mods.exitconfirmation;

import com.mojang.blaze3d.platform.Window;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import java.util.List;
import java.util.Optional;

public class ExitConfirmation {
    private boolean callbackSetUp;

    public ExitConfirmation() {

    }

    @ApiStatus.Internal
    public ActionResult onWindowClose(Window window, CloseSource source) {
        Minecraft mc = Minecraft.getInstance();

        // Check close source.
        if (source == CloseSource.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.level == null && mc.screen == null) {
                return ActionResult.CANCEL;
            }

            // Always cancel when loading the world.
            if (mc.screen instanceof LevelLoadingScreen) {
                return ActionResult.CANCEL;
            }

            // Otherwise only cancel when the close prompt is enabled. TODO Add config support back again.
            if (Config.closePrompt) {
                // Allow closing ingame if enabled in config. TODO Add config support back again.
                if (mc.level != null && !Config.closePromptInGame) {
                    return ActionResult.PASS;
                }

                // Only show screen, when the screen isn't the confirmation screen already.
                if (!(mc.screen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.setScreen(new ConfirmExitScreen(mc.screen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source == CloseSource.QUIT_BUTTON) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already. TODO Add config support back again.
            if (Config.closePrompt && Config.closePromptQuitButton && !(mc.screen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(mc.screen));
                return ActionResult.CANCEL;
            }
        }

        // Pass, it's not a valid close source.
        return ActionResult.PASS;
    }

    /**
     * Sets everything up when the title screen is shown.
     *
     * @param client the minecraft client.
     * @param screen the initialized screen. (Only used if it's the title screen)
     */
    @ApiStatus.Internal
    public void onTitleScreenInit(Minecraft client, Screen screen) {
        // Only if it's the title screen.
        if (screen instanceof TitleScreen titleScreen) {
            // Set everything up.
            setupGLFWCallback(client);
            overrideQuitButton(client, titleScreen);
        }
    }

    /**
     * Overrides the quit button action.
     *
     * @param client the minecraft client.
     * @param titleScreen the title screen.
     */
    private void overrideQuitButton(Minecraft client, TitleScreen titleScreen) {
        // Get all gui objects from the title screen.
        List<? extends GuiEventListener> buttons = titleScreen.children();

        // Intercepting close button.
        Optional<? extends Button> quitButton = buttons.stream().filter(listener -> listener instanceof Button button && button.getMessage().equals(Component.translatable("menu.quit"))).map(listener -> (Button) listener).findFirst();

        // Only override if the quit button is found.
        quitButton.ifPresent(widget -> {
            // Override on press field. (Requires access widener)
            widget.onPress = (button) -> {
                ExitConfirmation.onQuitButtonClick(client, titleScreen, widget);
            };
        });
    }

    @ExpectPlatform
    public static void onQuitButtonClick(Minecraft client, TitleScreen titleScreen, Button widget) {
        throw new AssertionError("Not implemented");
    }

    /**
     * Sets up the {@link GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI) window close callback using GLFW}.
     * @param client the minecraft client.
     * @see GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI)
     */
    @SuppressWarnings("resource")
    private void setupGLFWCallback(Minecraft client) {
        if (!callbackSetUp) {
            // Intercepting close button / ALT+F4 (on Windows and Ubuntu)
            long handle = client.getWindow().getWindow();

            // Set the callback.
            GLFW.glfwSetWindowCloseCallback(handle, window -> {
                ExitConfirmation.onCloseCallback(client, handle, window);
            });
            callbackSetUp = true;
        }
    }

    @ExpectPlatform
    public static void onCloseCallback(Minecraft client, long handle, long window) {
        throw new AssertionError("Not implemented");
    }
}

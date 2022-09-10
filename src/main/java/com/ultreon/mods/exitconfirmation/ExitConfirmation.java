package com.ultreon.mods.exitconfirmation;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import java.util.List;
import java.util.Optional;


public class ExitConfirmation implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    private boolean callbackSetUp = false;

    public ExitConfirmation() {
    }

    public ActionResult onWindowClose(Window window, WindowCloseEvent.Source source) {
        Minecraft mc = Minecraft.getInstance();

        // Check close source.
        if (source == WindowCloseEvent.Source.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.level == null && mc.screen == null) {
                return ActionResult.CANCEL;
            }

            // Always cancel when loading the world.
            if (mc.screen instanceof LevelLoadingScreen) {
                return ActionResult.CANCEL;
            }

            // Otherwise only cancel when the close prompt is enabled.
            if (Config.closePrompt.get()) {
                // Allow closing ingame if enabled in config.
                if (mc.level != null && !Config.closePromptInGame.get()) {
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
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already.
            if (Config.closePrompt.get() && Config.closePromptQuitButton.get() && !(mc.screen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(mc.screen));
                return ActionResult.CANCEL;
            }
        }

        // Pass, it's not a valid close source.
        return ActionResult.PASS;
    }

    @Override
    public void onInitializeClient() {
        // Initialize config.
        Config.initialize();

        // Register ourselves for server and other game events we are interested in
        WindowCloseEvent.EVENT.register(this::onWindowClose);

        // Intercept things when the title screen opened.
        ScreenEvents.AFTER_INIT.register(this::onTitleScreenInit);
    }

    /**
     * Sets everything up when the title screen is shown.
     *
     * @param client the minecraft client.
     * @param screen the initialized screen. (Only used if it's the title screen)
     * @param scaledWidth scaled width of the screen.
     * @param scaledHeight scaled height of the screen.
     */
    private void onTitleScreenInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
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
                ActionResult result = WindowCloseEvent.EVENT.invoker().closing(client.getWindow(), WindowCloseEvent.Source.GENERIC);
                if (result != ActionResult.CANCEL) {
                    client.stop();
                }
            };
        });
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
                ActionResult result = WindowCloseEvent.EVENT.invoker().closing(client.getWindow(), WindowCloseEvent.Source.GENERIC);
                if (result == ActionResult.CANCEL) {
                    GLFW.glfwSetWindowShouldClose(window, false);
                }
            });
            callbackSetUp = true;
        }
    }
}

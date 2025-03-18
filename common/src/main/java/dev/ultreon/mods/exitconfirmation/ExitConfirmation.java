package dev.ultreon.mods.exitconfirmation;

import dev.ultreon.mods.exitconfirmation.config.Config;
import dev.ultreon.mods.exitconfirmation.mixin.accessor.ButtonAccessor;
import dev.ultreon.mods.xinexlib.event.system.EventSystem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ExitConfirmation {
    public static final Config CONFIG = new Config();
    public static final Logger LOGGER = LoggerFactory.getLogger("ExitConfirmation");
    private boolean callbackSetUp;

    @ApiStatus.Internal
    public ExitConfirmation() {
        // Register ourselves for server and other game events we are interested in
        EventSystem.MAIN.on(GameExitEvent.class, (event) -> {
            ActionResult actionResult = this.onGameExit(event);
            if (actionResult == ActionResult.CANCEL) {
                event.cancel();
            }
        });
    }

    @ApiStatus.Internal
    public ActionResult onGameExit(GameExitEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final ExitSource source = event.getSource();

        final Screen screen = mc.screen;
        if (screen instanceof ConfirmExitScreen) {
            return ActionResult.CANCEL;
        }

        // Check the close source.
        if (source instanceof ExitSource.WindowExitSource) {
            // Always cancel if the world isn't loaded but also being in-game. (Fixes bug)
            if (mc.level == null && screen == null) {
                return ActionResult.CANCEL;
            }

            // Always cancel when loading the world.
            if (screen instanceof LevelLoadingScreen) {
                return ActionResult.CANCEL;
            }

            // Otherwise only cancel when the close prompt is enabled. TODO Add config support back again.
            if (CONFIG.closePrompt.get()) {
                // Allow closing in-game if enabled in config. TODO Add config support back again.
                if (mc.level != null && !CONFIG.closePromptInGame.get()) {
                    return ActionResult.PASS;
                }

                // Only show screen, when the screen isn't the confirmation screen already.
                if (!(screen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.setScreen(new ConfirmExitScreen(screen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source instanceof ExitSource.ButtonWidgetExitSource) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already. TODO Add config support back again.
            if (CONFIG.closePrompt.get() && CONFIG.closePromptQuitButton.get() && !(screen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(screen));
                return ActionResult.CANCEL;
            }
        } else if (source instanceof ExitSource.KeyboardInScreenExitSource) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already. TODO Add config support back again.
            if (CONFIG.closePrompt.get() && CONFIG.closePromptQuitButton.get() && !(screen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(screen));
                return ActionResult.CANCEL;
            }
        } else if (source instanceof ExitSource.KeyboardExitSource) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already. TODO Add config support back again.
            if (CONFIG.closePrompt.get() && CONFIG.closePromptQuitButton.get() && !(screen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(screen));
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
            this.setupGLFWCallback(client);
            this.overrideQuitButton(client, titleScreen);
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
            ((ButtonAccessor) widget).setOnPress((button) -> this.onQuitButtonClick(client, titleScreen, widget));
        });
    }

    public final void onQuitButtonClick(Minecraft client, TitleScreen titleScreen, Button quitButton) {
        GameExitEvent publish = EventSystem.MAIN.publish(new GameExitEvent(ExitSource.buttonWidget(titleScreen, quitButton), client));
        if (!publish.isCanceled()) {
            client.stop();
        }
    }

    /**
     * Sets up the {@link GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI) window close callback using GLFW}.
     * @param client the minecraft client.
     * @see GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI)
     */
    @SuppressWarnings("resource")
    private void setupGLFWCallback(Minecraft client) {
        if (!this.callbackSetUp) {
            // Intercepting close button / ALT+F4 (on Windows and Ubuntu)
            long handle = client.getWindow().getWindow();

            // Set the callback.
            GLFW.glfwSetWindowCloseCallback(handle, window -> this.onCloseCallback(client, window));
            this.callbackSetUp = true;
        }
    }

    public void onCloseCallback(Minecraft client, long window) {
        GameExitEvent publish = EventSystem.MAIN.publish(new GameExitEvent(ExitSource.window(client.getWindow()), client));
        if (publish.isCanceled()) {
            GLFW.glfwSetWindowShouldClose(window, false);
        }
    }
}

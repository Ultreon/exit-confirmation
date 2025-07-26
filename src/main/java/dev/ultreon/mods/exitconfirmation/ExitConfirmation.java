package dev.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.GameWindow;
import dev.ultreon.quantum.client.QuantumClient;
import dev.ultreon.quantum.client.api.events.ClientLifecycleEvents;
import dev.ultreon.quantum.client.api.events.WindowEvents;
import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.screens.world.WorldLoadScreen;
import dev.ultreon.quantum.desktop.ClientModInitializer;
import dev.ultreon.quantum.events.api.EventResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

public class ExitConfirmation implements ClientModInitializer {
    public static final ExitConfig CONFIG = new ExitConfig();

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogManager.getLogger();

    @ApiStatus.Internal
    public ExitConfirmation() {
        ClientLifecycleEvents.WINDOW_CLOSED.subscribe(() -> this.onWindowClose(null));
    }

    @Override
    @ApiStatus.Internal
    public void onInitializeClient() {
        // Initialize config.
        CONFIG.load();
        CONFIG.save();

        // Register ourselves for server and other game events we are interested in
        WindowEvents.WINDOW_CLOSE_REQUESTED.subscribe(this::onWindowClose);
    }

    private dev.ultreon.quantum.events.api.EventResult onWindowClose(GameWindow gameWindow) {
        final QuantumClient mc = QuantumClient.get();

        final Screen screen = mc.screen;
        if (screen instanceof ConfirmExitScreen) {
            return EventResult.interruptCancel();
        }

        // Always cancel if the world isn't loaded but also being in-game. (Fixes bug)
        if (mc.world == null && screen == null) {
            return EventResult.interruptCancel();
        }

        // Always cancel when loading the world.
        if (screen instanceof WorldLoadScreen) {
            return EventResult.interruptCancel();
        }

        // Otherwise only cancel when the close prompt is enabled. TODO Add config support back again.
        if (ExitConfig.closePrompt) {
            // Allow closing in-game if enabled in config. TODO Add config support back again.
            if (mc.world != null && !ExitConfig.closePromptInGame) {
                return EventResult.interruptCancel();
            }

            // Only show screen, when the screen isn't the confirmation screen already.
            // Set the screen.
            mc.showScreen(new ConfirmExitScreen(screen));

            // Cancel the event.
            return EventResult.interruptCancel();
        }

        // Pass, it's not a valid close source.
        return EventResult.pass();
    }
}

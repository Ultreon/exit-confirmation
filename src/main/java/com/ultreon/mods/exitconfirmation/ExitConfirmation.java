package com.ultreon.mods.exitconfirmation;

import com.badlogic.gdx.Gdx;
import com.ultreon.craft.client.ClientModInit;
import com.ultreon.craft.client.UltracraftClient;
import com.ultreon.craft.client.events.ClientLifecycleEvents;
import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.screens.WorldLoadScreen;
import com.ultreon.libs.events.v1.EventResult;
import com.ultreon.mods.exitconfirmation.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

public class ExitConfirmation implements ClientModInit {
    public static final Config CONFIG = new Config();

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogManager.getLogger();

    @ApiStatus.Internal
    public ExitConfirmation() {
        ClientLifecycleEvents.WINDOW_CLOSED.listen(() -> AttemptExitEvent.EVENT.factory().closing(CloseSource.GENERIC));
    }

    @Override
    @ApiStatus.Internal
    public void onInitializeClient() {
        // Initialize config.
        Config.load();
        Config.save();

        // Register ourselves for server and other game events we are interested in
        AttemptExitEvent.EVENT.listen(this::onWindowClose);
    }

    @ApiStatus.Internal
    public EventResult onWindowClose(CloseSource source) {
        final UltracraftClient mc = UltracraftClient.get();

        final Screen screen = mc.screen;
        if (screen instanceof ConfirmExitScreen) {
            return EventResult.interruptCancel();
        }

        // Check close source.
        if (source == CloseSource.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.world == null && screen == null) {
                return EventResult.interruptCancel();
            }

            // Always cancel when loading the world.
            if (screen instanceof WorldLoadScreen) {
                return EventResult.interruptCancel();
            }

            // Otherwise only cancel when the close prompt is enabled. TODO Add config support back again.
            if (CONFIG.closePrompt.get()) {
                // Allow closing ingame if enabled in config. TODO Add config support back again.
                if (mc.world != null && !CONFIG.closePromptInGame.get()) {
                    return EventResult.interruptCancel();
                }

                // Only show screen, when the screen isn't the confirmation screen already.
                if (!(screen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.showScreen(new ConfirmExitScreen(screen));
                }

                // Cancel the event.
                return EventResult.interruptCancel();
            }
        } else if (source == CloseSource.QUIT_BUTTON) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already. TODO Add config support back again.
            if (CONFIG.closePrompt.get() && CONFIG.closePromptQuitButton.get() && !(screen instanceof ConfirmExitScreen)) {
                mc.showScreen(new ConfirmExitScreen(screen));
                return EventResult.interruptCancel();
            }
        }

        // Pass, it's not a valid close source.
        return EventResult.pass();
    }

    public static void onQuitButtonClick() {
        EventResult result = AttemptExitEvent.EVENT.factory().closing(CloseSource.QUIT_BUTTON);
        if (!result.isCanceled()) {
            Gdx.app.exit();
        }
    }
}

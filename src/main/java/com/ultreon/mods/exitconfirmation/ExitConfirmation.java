package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.minecraft.client.MinecraftClient;

public class ExitConfirmation implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";
    public static boolean allowExit;
    public static final Config CONFIG = new Config();

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = Logger.get("ExitConfirmation");

    @Override
    public void onInitializeClient() {
        Config.load();
        Config.save();

        WindowCloseEvent.EVENT.register(this::onWindowClose);
    }

    public ActionResult onWindowClose(WindowCloseEvent.Source source) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Check close source.
        if (source == WindowCloseEvent.Source.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.world == null && mc.currentScreen == null) {
                return ActionResult.CANCEL;
            }

            // Otherwise only cancel when the close prompt is enabled.
            if (ExitConfirmation.CONFIG.closePrompt.get()) {
                // Allow closing ingame if enabled in config.
                if (mc.world != null && !ExitConfirmation.CONFIG.closePromptInGame.get()) {
                    return ActionResult.PASS;
                }

                // Only show screen, when the screen isn't the confirmation screen yet.
                if (!(mc.currentScreen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.setScreen(new ConfirmExitScreen(mc.currentScreen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and the screen isn't currently the confirmation screen already.
            if (ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.closePromptQuitButton.get() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(mc.currentScreen));
                return ActionResult.CANCEL;
            }
        }
        return ActionResult.PASS;
    }
}

package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExitConfirmation implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    public static final Config CONFIG = new Config();
    public static boolean allowExit;
    private boolean callbackSetUp = false;

    public ExitConfirmation() {
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

                // Only show screen, when the screen isn't the confirmation screen already.
                if (!(mc.currentScreen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.setScreen(new ConfirmExitScreen(mc.currentScreen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already.
            if (ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.closePromptQuitButton.get() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.setScreen(new ConfirmExitScreen(mc.currentScreen));
                return ActionResult.CANCEL;
            }
        }

        // Pass, it's not a valid close source.
        return ActionResult.PASS;
    }

    @Override
    public void onInitializeClient() {
        // Initialize config.
        Config.load();
        Config.save();

        // Register ourselves for server and other game events we are interested in
        WindowCloseEvent.EVENT.register(this::onWindowClose);
    }
}

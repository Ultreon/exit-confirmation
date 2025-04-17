package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.client.Minecraft;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ExitConfirmation implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";
    public static boolean allowExit;
    public static final Config CONFIG = new Config();

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogManager().getLogger("ExitConfirmation");
    private static ExitConfirmation instance;

    public static ExitConfirmation getInstance() {
        return ExitConfirmation.instance;
    }

    @Override
    public void onInitializeClient() {
        ExitConfirmation.instance = this;

        Config.load();
        Config.save();
    }

    public ActionResult onWindowClose(WindowCloseEvent.Source source) {
        Minecraft mc = Minecraft.getMinecraft();

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
                    mc.openScreen(new ConfirmExitScreen(mc.currentScreen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and the screen isn't currently the confirmation screen already.
            if (ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.closePromptQuitButton.get() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.openScreen(new ConfirmExitScreen(mc.currentScreen));
                return ActionResult.CANCEL;
            }
        }
        return ActionResult.PASS;
    }
}

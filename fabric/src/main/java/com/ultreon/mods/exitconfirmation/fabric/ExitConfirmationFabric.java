package com.ultreon.mods.exitconfirmation.fabric;

import com.ultreon.mods.exitconfirmation.Config;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExitConfirmationFabric implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    private final ExitConfirmation common = new ExitConfirmation();

    public ExitConfirmationFabric() {
    }

    @Override
    public void onInitializeClient() {
        // Initialize config.
        Config.load();
        Config.save();

        // Register ourselves for server and other game events we are interested in
        WindowCloseEvent.EVENT.register(common::onWindowClose);

        // Intercept things when the title screen opened.
        ScreenEvents.AFTER_INIT.register((client, screen, $, $1) -> common.onTitleScreenInit(client, screen));
    }
}

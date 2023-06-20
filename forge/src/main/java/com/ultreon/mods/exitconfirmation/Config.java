package com.ultreon.mods.exitconfirmation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Objects;

@ParametersAreNonnullByDefault
public final class Config {
    public static final Property CLOSE_PROMPT;
    public static final Property CLOSE_PROMPT_IN_GAME;
    public static final Property CLOSE_PROMPT_QUIT_BUTTON;
    public static final Property QUIT_ON_ESC_IN_TITLE;

    private static final Configuration CONFIGURATION;

    static {
        CONFIGURATION = new Configuration(new File("config/exitconfirmation.cfg"), "1.0");
        CLOSE_PROMPT = CONFIGURATION.get("exitConfirmation", "closePrompt", true, "Show close prompt.");
        CLOSE_PROMPT_IN_GAME = CONFIGURATION.get("exitConfirmation", "closePromptInGame", true, "Show close prompt in-game.");
        CLOSE_PROMPT_QUIT_BUTTON = CONFIGURATION.get("exitConfirmation", "closePromptQuitButton", true, "Show close prompt when pressing quit button.");
        QUIT_ON_ESC_IN_TITLE = CONFIGURATION.get("exitConfirmation", "quitOnEscInTite", true, "Show close prompt when pressing escape in title screen.");

        CONFIGURATION.save();

        new Config();
    }

    private Config() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void reload() {
        CONFIGURATION.load();
    }

    public static void save() {
        CONFIGURATION.save();
    }

    public static void init() {

    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event) {
        if (Objects.equals(event.modID, ExitConfirmation.MOD_ID)) {
            reload();
        }
    }
}

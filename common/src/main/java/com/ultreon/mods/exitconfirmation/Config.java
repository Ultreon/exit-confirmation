package com.ultreon.mods.exitconfirmation;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final File FILE = new File(getConfigDir().toFile(), "exit-confirmation.properties");

    public static boolean closePrompt = true;

    public static boolean closePromptInGame = true;
    public static boolean closePromptQuitButton = true;
    public static boolean quitOnEscInTitle = true;
    public static boolean dirtBackground = false;
    public static boolean enableNarrator = false;
    public static boolean forceTransparentBackground;

    public static void load() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(FILE));
            closePrompt = Boolean.getBoolean(properties.getProperty("close_prompt"));
            closePromptInGame = Boolean.getBoolean(properties.getProperty("close_prompt.in_game"));
            closePromptQuitButton = Boolean.getBoolean(properties.getProperty("close_prompt.quit_button"));
            quitOnEscInTitle = Boolean.getBoolean(properties.getProperty("quit_on_esc_in_title"));
            dirtBackground = Boolean.getBoolean(properties.getProperty("screen.dirt_background"));
            forceTransparentBackground = Boolean.getBoolean(properties.getProperty("screen.force_transparent_background"));
            enableNarrator = Boolean.getBoolean(properties.getProperty("screen.enable_narrator"));
        } catch (FileNotFoundException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Properties properties = new Properties();
            properties.setProperty("close_prompt", Boolean.toString(closePrompt));
            properties.setProperty("close_prompt.in_game", Boolean.toString(closePromptInGame));
            properties.setProperty("close_prompt.quit_button", Boolean.toString(closePromptQuitButton));
            properties.setProperty("quit_on_esc_in_title", Boolean.toString(quitOnEscInTitle));
            properties.setProperty("screen.dirt_background", Boolean.toString(dirtBackground));
            properties.setProperty("screen.force_transparent_background", Boolean.toString(forceTransparentBackground));
            properties.setProperty("screen.enable_narrator", Boolean.toString(enableNarrator));
            properties.store(new FileOutputStream(FILE), "Exit Confirmation configuration.");
        } catch (FileNotFoundException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ExpectPlatform
    private static Path getConfigDir() {
        throw new AssertionError("Not implemented");
    }
}

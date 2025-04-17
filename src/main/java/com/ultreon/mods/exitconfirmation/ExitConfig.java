package com.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.config.crafty.ConfigEntry;
import dev.ultreon.quantum.config.crafty.ConfigInfo;
import dev.ultreon.quantum.config.crafty.CraftyConfig;
import dev.ultreon.quantum.config.crafty.Ranged;

import java.nio.ByteBuffer;

@ConfigInfo(fileName = "exit_confirm.json5")
public class ExitConfig extends CraftyConfig {
    @ConfigEntry(path = "enabled")
    public static boolean enabled = true;

    @ConfigEntry(path = "closePrompt")
    public static boolean closePrompt = true;

    @ConfigEntry(path = "closePromptInGame")
    public static boolean closePromptInGame = true;

    @ConfigEntry(path = "quitOnEscInTitle")
    public static boolean quitOnEscInTitle = true;

    @ConfigEntry(path = "disconnectPrompt")
    public static boolean disconnectPrompt = true;

    @ConfigEntry(path = "disconnectPromptInGame")
    public static boolean disconnectPromptInGame = true;

    @ConfigEntry(path = "confirmDelay")
    @Ranged(min = 0, max = 80)
    public static int confirmDelay = 20;

    @ConfigEntry(path = "solidBackground")
    public static boolean solidBackground = false;

    @ConfigEntry(path = "forceTransparentBackground")
    public static boolean forceTransparentBackground = false;
}

package dev.ultreon.mods.exitconfirmation.fabric.config;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ConfigImpl {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

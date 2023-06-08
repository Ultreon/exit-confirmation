package com.ultreon.mods.exitconfirmation.config.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}

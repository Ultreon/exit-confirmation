package com.ultreon.mods.exitconfirmation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExitConfirmation.MOD_ID, version = "0.1.0", clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class ExitConfirmation {

    public static final String MOD_ID = "exit_confirm";
    private static boolean callbackSetup;
    private boolean escPress = false;

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();

    public ExitConfirmation() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(new ModEvents());

        Config.init();
    }
}

package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExitConfirmation.MOD_ID, version = "0.1.0", clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class ExitConfirmation {

    public static final String MOD_ID = "exit_confirm";
    public static boolean allowExit;
    public static final Config CONFIG = new Config();

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();

    public ExitConfirmation() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.load();
        Config.save();
    }

    @SubscribeEvent
    public void onWindowClose(WindowCloseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        WindowCloseEvent.Source source = event.getSource();

        // Check close source.
        if (source == WindowCloseEvent.Source.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.theWorld == null && mc.currentScreen == null) {
                event.setCanceled(true);
                return;
            }

            // Otherwise only cancel when the close prompt is enabled.
            if (ExitConfirmation.CONFIG.closePrompt.get()) {
                // Allow closing ingame if enabled in config.
                if (mc.theWorld != null && !ExitConfirmation.CONFIG.closePromptInGame.get()) {
                    return;
                }

                // Only show screen, when the screen isn't the confirmation screen yet.
                if (!(mc.currentScreen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.displayGuiScreen(new ConfirmExitScreen(mc.currentScreen));
                }

                // Cancel the event.
                event.setCanceled(true);
            }
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and the screen isn't currently the confirmation screen already.
            if (ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.closePromptQuitButton.get() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.displayGuiScreen(new ConfirmExitScreen(mc.currentScreen));
                event.setCanceled(true);
            }
        }
    }
}

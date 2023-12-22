package com.ultreon.mods.exitconfirmation.neoforge;

import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.CloseSource;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

@Mod(ExitConfirmationNeoForge.MOD_ID)
public class ExitConfirmationNeoForge {
    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    private final ExitConfirmation common = new ExitConfirmation();

    @ApiStatus.Internal
    public ExitConfirmationNeoForge() {
        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        Config.load();
        Config.save();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public void onWindowClose(WindowCloseEvent event) {
        CloseSource source = event.getSource();

        event.setCanceled(this.common.onWindowClose(source) == ActionResult.CANCEL);
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public void onTitleScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();

        this.common.onTitleScreenInit(mc, screen);
    }
}

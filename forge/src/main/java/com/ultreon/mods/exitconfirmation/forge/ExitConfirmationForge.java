package com.ultreon.mods.exitconfirmation.forge;

import com.ultreon.mods.exitconfirmation.CloseSource;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

@Mod(ExitConfirmationForge.MOD_ID)
public class ExitConfirmationForge {
    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    private final ExitConfirmation common = new ExitConfirmation();

    @ApiStatus.Internal
    public ExitConfirmationForge() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.load();
        Config.save();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public void onWindowClose(WindowCloseEvent event) {
        CloseSource source = event.getSource();

        this.common.onWindowClose(source);
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public void onTitleScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();

        this.common.onTitleScreenInit(mc, screen);
    }
}

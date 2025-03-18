package dev.ultreon.mods.exitconfirmation.forge;

import dev.ultreon.mods.exitconfirmation.config.Config;
import dev.ultreon.mods.exitconfirmation.ExitConfirmation;
import dev.ultreon.mods.exitconfirmation.config.gui.ConfigScreen;
import dev.ultreon.mods.xinexlib.client.event.WindowCloseEvent;
import dev.ultreon.mods.xinexlib.event.system.EventSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;

@Mod(ExitConfirmationForge.MOD_ID)
public class ExitConfirmationForge {
    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    private final ExitConfirmation common = new ExitConfirmation();

    @ApiStatus.Internal
    public ExitConfirmationForge(FMLJavaModLoadingContext modContext) {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.load();
        Config.save();

        modContext.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public void onTitleScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();

        this.common.onTitleScreenInit(mc, screen);
    }
}

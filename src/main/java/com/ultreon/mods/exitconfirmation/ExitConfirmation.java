package com.ultreon.mods.exitconfirmation;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.core.Hooks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.relauncher.ModListHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.IOException;

@Mod(modid = ExitConfirmation.MOD_ID, version = "0.1.0-mc.1.7.10", acceptedMinecraftVersions = "1.7.10")
public class ExitConfirmation {

    public static final String MOD_ID = "exit_confirm";
    public static boolean allowExit;
    public static final Config CONFIG = new Config();

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();

    private ExitConfirmation() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Exit Confirmation initialized.");

        Config.load();
        Config.save();
   }

   public static void init() {
       new ExitConfirmation();
   }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event) {
        GuiScreen gui = event.gui;
        GuiButton button = event.button;

        if (button.id == 4 && gui instanceof GuiMainMenu) {
            if (MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON))) {
                event.setCanceled(true);
                return;
            }
            ExitConfirmation.allowExit = true;
            gui.mc.shutdown();
        }
    }

    @SubscribeEvent
    public void onWindowClose(WindowCloseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        WindowCloseEvent.Source source = event.getSource();

        Thread.dumpStack();

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

package com.ultreon.mods.exitconfirmation;

import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Translations;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.entrypoint.applet.AppletFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ServerConnectingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;


public class ExitConfirmation implements ClientModInitializer {

    public static final String MOD_ID = "exit_confirm";

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();
    public static Minecraft minecraft;
    private static ExitConfirmation instance;
    private boolean windowListenerSetup = false;
    private AppletFrame window;

    public ExitConfirmation() {
        instance = this;
        Translations.addTranslation("screen.exit_confirm.title", "Exit Confirmation");
        Translations.addTranslation("screen.exit_confirm.description", "Are you sure you want to exit Minecraft?");
    }

    public static Id id(String path) {
        return new Id(MOD_ID, path);
    }

    public static void handleTitleScreenInit(Minecraft client, TitleScreen titleScreen) {
        instance.onTitleScreenInit(ExitConfirmation.minecraft, titleScreen);
    }

    public static Frame getGameWindow() {
        return instance.window;
    }

    public ActionResult onWindowClose(Window window, WindowCloseEvent.Source source) {
        Minecraft mc = ExitConfirmation.minecraft;

        // Check close source.
        if (source == WindowCloseEvent.Source.GENERIC) {
            // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
            if (mc.level == null && mc.currentScreen == null) {
                return ActionResult.CANCEL;
            }

            // Always cancel when loading the world.
            if (mc.currentScreen instanceof DownloadingTerrainScreen) {
                return ActionResult.CANCEL;
            }

            // Always cancel when loading the world.
            if (mc.currentScreen instanceof ServerConnectingScreen) {
                return ActionResult.CANCEL;
            }

            // Otherwise only cancel when the close prompt is enabled.
            if (Config.getClosePrompt()) {
                // Allow closing ingame if enabled in config.
                if (mc.level != null && !Config.getClosePromptInGame()) {
                    return ActionResult.PASS;
                }

                // Only show screen, when the screen isn't the confirmation screen already.
                if (!(mc.currentScreen instanceof ConfirmExitScreen)) {
                    // Set the screen.
                    mc.openScreen(new ConfirmExitScreen(mc.currentScreen));
                }

                // Cancel the event.
                return ActionResult.CANCEL;
            }
        } else if (source == WindowCloseEvent.Source.QUIT_BUTTON) {
            // Cancel quit button when set in config, and screen isn't currently the confirmation screen already.
            if (Config.getClosePrompt() && Config.getClosePromptQuitButton() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.openScreen(new ConfirmExitScreen(mc.currentScreen));
                return ActionResult.CANCEL;
            }
        }

        // Pass, it's not a valid close source.
        return ActionResult.PASS;
    }

    @Override
    public void onInitializeClient() {
    }

    /**
     * Sets everything up when the title screen is shown.
     *
     * @param client the minecraft client.
     * @param screen the initialized screen. (Only used if it's the title screen)
     */
    private void onTitleScreenInit(Minecraft client, Screen screen) {
        // Initialize config.
        try {
            Config.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register ourselves for server and other game events we are interested in
        WindowCloseEvent.EVENT.register(this::onWindowClose);

        window = (AppletFrame) (ExitConfirmation.minecraft.canvas.getParent().getParent().getParent());

        // Only if it's the title screen.
        if (screen instanceof TitleScreen) {
            // Set everything up.
            setupGLFWCallback();
        }
    }

    /**
     * Sets up the {@link WindowListener window listener using AWT}.
     * @see JFrame#addWindowListener(WindowListener)
     */
    private void setupGLFWCallback() {
        if (!windowListenerSetup) {
            // Intercepting close button / ALT+F4 (on Windows and Ubuntu)
//            window.windowClosing(WindowConstants.DO_NOTHING_ON_CLOSE);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    ActionResult result = WindowCloseEvent.EVENT.invoker().closing(window, WindowCloseEvent.Source.GENERIC);
                    if (result == ActionResult.PASS) {
                        e.getWindow().dispose();
                    }
                    System.out.println("e.getOldState() = " + e.getOldState());
                    System.out.println("e.getNewState() = " + e.getNewState());
                }
            });
            windowListenerSetup = true;
        }
    }

    public AppletFrame getWindow() {
        return window;
    }
}

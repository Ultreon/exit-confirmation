package com.ultreon.mods.exitconfirmation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.render.gui.screen.MessengerScreen;
import com.ultreon.bubbles.render.gui.screen.Screen;
import com.ultreon.libs.events.v1.EventResult;
import com.ultreon.mods.exitconfirmation.config.Config;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

public class ExitConfirmation implements ModInitializer {
    public static final Config CONFIG = new Config();
    public static boolean allowSaveAndQuit = false;
    public static boolean allowExitGame = false;

    @ApiStatus.Internal
    public ExitConfirmation() {

    }

    @Override
    public void onInitialize() {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public boolean closeRequested() {
                if (allowExitGame) return true;

                EventResult eventResult = ExitConfirmation.onWindowClose();
                if (eventResult.isCanceled()) return false;
                return super.closeRequested();
            }
        });
    }

    @ApiStatus.Internal
    private static EventResult onWindowClose() {
        final BubbleBlaster game = BubbleBlaster.getInstance();

        final Screen screen = game.getCurrentScreen();
        if (screen instanceof ConfirmExitScreen) {
            return EventResult.interruptCancel();
        }

        // Always cancel if the world isn't loaded but also being ingame. (Fixes bug)
        if (game.world == null && screen == null) {
            return EventResult.interruptCancel();
        }

        // Always cancel when loading the world.
        if (screen instanceof MessengerScreen) {
            return EventResult.interruptCancel();
        }

        // Otherwise only cancel when the close prompt is enabled. TODO Add config support back again.
        if (CONFIG.closePrompt.get()) {
            // Allow closing ingame if enabled in config. TODO Add config support back again.
            if (game.world != null && !CONFIG.closePromptInGame.get()) {
                return EventResult.pass();
            }

            // Set the screen.
            game.showScreen(new ConfirmExitScreen(screen));

            // Cancel the event.
            return EventResult.interruptCancel();
        }

        return EventResult.pass();
    }

    @ApiStatus.Internal
    public static boolean onGameShutdown() {
        EventResult eventResult = ExitConfirmation.onWindowClose();
        return eventResult.isCanceled();
    }
}

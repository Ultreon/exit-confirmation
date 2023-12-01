package com.ultreon.mods.exitconfirmation;

import com.badlogic.gdx.Gdx;
import com.ultreon.craft.client.UltracraftClient;
import com.ultreon.craft.client.events.ClientLifecycleEvents;
import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.widget.TextButton;
import com.ultreon.craft.text.TextObject;

public class ConfirmExitScreen extends ConfirmScreen {
    private static final TextObject DESCRIPTION = TextObject.translation("exit_confirm.screen.exit.description");
    private static final TextObject TITLE = TextObject.translation("exit_confirm.screen.exit.title");

    public ConfirmExitScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(TextButton btn) {
        if (this.client != null) {
            btn.enabled = false;
            var eventResult = ClientLifecycleEvents.WINDOW_CLOSED.factory().onWindowClose();
            if (!eventResult.isCanceled() && (this.client.world != null)) {
                this.client.exitWorldAndThen(() -> {
                    try {
                        var close = this.client.connection.close();
                        if (close != null) close.sync();
                        var future = this.client.connection.closeGroup();
                        if (future != null) future.sync();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    } catch (Exception e) {
                        UltracraftClient.LOGGER.warn("Error occurred while closing connection:", e);
                    }
                    Gdx.app.postRunnable(() -> {
                        Gdx.app.exit();
                        System.exit(0);
                    });
                });
                return;
            }
            Gdx.app.exit();
            System.exit(0);
        }
    }
}

package com.ultreon.mods.exitconfirmation;

import com.badlogic.gdx.Gdx;
import dev.ultreon.quantum.client.QuantumClient;
import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.widget.TextButton;
import dev.ultreon.quantum.text.TextObject;

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

            this.client.exitWorldAndThen(() -> {
                try {
                    this.client.connection.close();
                } catch (Exception e) {
                    QuantumClient.LOGGER.warn("Error occurred while closing connection:", e);
                }
                Gdx.app.postRunnable(() -> {
                    Gdx.app.exit();
                    System.exit(0);
                });
            });
        }
    }
}

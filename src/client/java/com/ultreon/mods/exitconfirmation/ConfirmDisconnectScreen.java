package com.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.client.gui.screens.Screen;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmDisconnectScreen extends ConfirmScreen {
    public ConfirmDisconnectScreen(Screen background) {
        super(background, "Confirm Disconnect", "Are you sure you want to disconnect?");
    }

    @Override
    public void yesButtonCallback(TextButton btn) {
        if (this.client != null) {
            btn.enabled = false;
            this.client.exitWorldToTitle();
        }
    }

    @Override
    public boolean canCloseWithEsc() {
        return true;
    }
}

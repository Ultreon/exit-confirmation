package com.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.widget.TextButton;
import dev.ultreon.quantum.text.TextObject;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmDisconnectScreen extends ConfirmScreen {
    private static final TextObject DESCRIPTION = TextObject.translation("exit_confirm.screen.disconnect.description");
    private static final TextObject TITLE = TextObject.translation("exit_confirm.screen.disconnect.title");

    public ConfirmDisconnectScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
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

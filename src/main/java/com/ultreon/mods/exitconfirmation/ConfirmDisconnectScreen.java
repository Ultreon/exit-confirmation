package com.ultreon.mods.exitconfirmation;

import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.widget.TextButton;
import com.ultreon.craft.text.TextObject;

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

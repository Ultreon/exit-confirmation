package com.ultreon.mods.exitconfirmation;

import com.ultreon.bubbles.render.gui.screen.Screen;
import com.ultreon.libs.text.v1.TextObject;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmDisconnectScreen extends ConfirmScreen {
    private static final TextObject DESCRIPTION = TextObject.translation("screen.disconnect_confirm.description");
    private static final TextObject TITLE = TextObject.translation("screen.disconnect_confirm.title");

    public ConfirmDisconnectScreen(@Nullable Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback() {
        this.yesButton.enabled = false;
        ExitConfirmation.allowSaveAndQuit = true;
        this.game.saveAndQuit();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

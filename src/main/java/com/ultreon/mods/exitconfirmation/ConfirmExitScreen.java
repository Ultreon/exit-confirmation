package com.ultreon.mods.exitconfirmation;


import com.ultreon.bubbles.render.gui.screen.Screen;
import com.ultreon.libs.text.v1.TextObject;

public class ConfirmExitScreen extends ConfirmScreen {
    private static final TextObject DESCRIPTION = TextObject.translation("screen.exit_confirm.description");
    private static final TextObject TITLE = TextObject.translation("screen.exit_confirm.title");

    public ConfirmExitScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback() {
        if (this.game != null) {
            this.yesButton.enabled = false;
            if (this.game.world != null) {
                this.game.saveAndQuit();
                return;
            }

            ExitConfirmation.allowExitGame = true;
            this.game.shutdown();
        }
    }
}

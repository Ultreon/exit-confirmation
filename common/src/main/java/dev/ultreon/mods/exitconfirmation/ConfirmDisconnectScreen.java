package dev.ultreon.mods.exitconfirmation;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfirmDisconnectScreen extends ConfirmScreen {
    private static final Component DESCRIPTION = Component.translatable("screen.disconnect_confirm.description");
    private static final Component TITLE = Component.translatable("screen.disconnect_confirm.title");

    public ConfirmDisconnectScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(Button btn) {
        if (this.minecraft != null) {
            btn.active = false;
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, WorldUtils::saveWorldThenOpenTitle, true);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

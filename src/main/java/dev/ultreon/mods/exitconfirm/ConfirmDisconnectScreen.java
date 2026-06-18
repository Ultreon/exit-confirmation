package dev.ultreon.mods.exitconfirm;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

//? >= 26.1 {
/*import net.minecraft.client.multiplayer.ClientLevel;
*///? }
//? < 1.19.2 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///? }

public class ConfirmDisconnectScreen extends ConfirmScreen {
	//? < 1.19.2 {
    /*private static final Component DESCRIPTION = new TranslatableComponent("screen.disconnect_confirm.description");
    private static final Component TITLE = new TranslatableComponent("screen.disconnect_confirm.title");
	*///? } else {
    private static final Component DESCRIPTION = Component.translatable("screen.disconnect_confirm.description");
    private static final Component TITLE = Component.translatable("screen.disconnect_confirm.title");
	//? }

    public ConfirmDisconnectScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(Button btn) {
        if (this.minecraft != null) {
            btn.active = false;

			//? >= 1.20.1 {
			this.minecraft
					.getReportingContext()
					.draftReportHandled(this.minecraft, this, WorldUtils::saveWorldThenOpenTitle, true);
			//? } else {
			/*WorldUtils.saveWorldThenOpenTitle();
			*///? }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

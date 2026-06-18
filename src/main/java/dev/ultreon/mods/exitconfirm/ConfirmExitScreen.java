package dev.ultreon.mods.exitconfirm;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

//? < 1.19.2 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///? }

public class ConfirmExitScreen extends ConfirmScreen {
	//? < 1.19.2 {
    /*private static final Component DESCRIPTION = new TranslatableComponent("screen.exit_confirm.description");
    private static final Component TITLE = new TranslatableComponent("screen.exit_confirm.title");
	*///? } else {
	private static final Component DESCRIPTION = Component.translatable("screen.exit_confirm.description");
	private static final Component TITLE = Component.translatable("screen.exit_confirm.title");
	//? }

    public ConfirmExitScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(Button btn) {
		ExitConfirmation.markShouldClose();
		if (this.minecraft != null) {
            btn.active = false;
            if (this.minecraft.level != null && this.minecraft.isLocalServer()) {
                WorldUtils.saveWorldThenQuitGame();
                return;
            }

            this.minecraft.stop();
        }
    }
}

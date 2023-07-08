package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class ConfirmExitScreen extends ConfirmScreen {
    private static final Component DESCRIPTION = Component.translatable("screen.exit_confirm.description");
    private static final Component TITLE = Component.translatable("screen.exit_confirm.title");

    public ConfirmExitScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(Button btn) {
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

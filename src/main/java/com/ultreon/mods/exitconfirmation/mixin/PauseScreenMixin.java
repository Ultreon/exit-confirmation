package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.craft.client.UltracraftClient;
import com.ultreon.craft.client.gui.screens.PauseScreen;
import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.widget.TextButton;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.ConfirmDisconnectScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(TextObject component) {
        super(component);
    }

    @Inject(method = "exitWorld", at = @At("HEAD"), cancellable = true)
    private void exitConfirmation$createPauseMenu(TextButton caller, CallbackInfo ci) {
        caller.enabled = false;
        var client = UltracraftClient.get();
        if (client.screen == this) {
            if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
                ci.cancel();
                client.showScreen(new ConfirmDisconnectScreen(client.screen));
            }
        }
    }
}


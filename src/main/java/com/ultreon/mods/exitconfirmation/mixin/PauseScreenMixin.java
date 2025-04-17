package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ConfirmDisconnectScreen;
import com.ultreon.mods.exitconfirmation.ExitConfig;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import dev.ultreon.quantum.client.QuantumClient;
import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.screens.PauseScreen;
import dev.ultreon.quantum.client.gui.widget.TextButton;
import dev.ultreon.quantum.text.TextObject;
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
        var client = QuantumClient.get();
        if (client.screen == this) {
            if (ExitConfig.disconnectPrompt) {
                ci.cancel();
                client.showScreen(new ConfirmDisconnectScreen(client.screen));
            }
        }
    }
}


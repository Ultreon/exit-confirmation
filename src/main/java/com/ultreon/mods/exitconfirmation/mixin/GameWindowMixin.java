package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.bubbles.GameWindow;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameWindow.class)
public class GameWindowMixin {
    @Inject(at = @At("HEAD"), method = "dispose", cancellable = true)
    public void shutdown$exit_confirmation(CallbackInfo ci) {
        if (ExitConfirmation.allowExitGame) {
            return;
        }
        if (ExitConfirmation.onGameShutdown() && ExitConfirmation.CONFIG.closePrompt.get()) {
            ci.cancel();
        }
    }
}

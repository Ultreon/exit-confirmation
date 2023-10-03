package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.render.gui.screen.PauseScreen;
import com.ultreon.mods.exitconfirmation.ConfirmDisconnectScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleBlaster.class)
public class BubbleBlasterMixin {
    @Inject(at = @At("HEAD"), method = "shutdown", cancellable = true)
    public void shutdown$exit_confirmation(CallbackInfo ci) {
        if (ExitConfirmation.allowExitGame) {
            return;
        }
        if (ExitConfirmation.onGameShutdown() && ExitConfirmation.CONFIG.closePrompt.get()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "saveAndQuit", cancellable = true)
    public void saveAndQuit$exit_confirmation(CallbackInfo ci) {
        var minecraft = BubbleBlaster.getInstance();
        if (ExitConfirmation.allowSaveAndQuit) {
            ExitConfirmation.allowSaveAndQuit = false;
            return;
        }

        if (minecraft.getCurrentScreen() instanceof PauseScreen) {
            if(ExitConfirmation.CONFIG.saveAndExitPrompt.get()) {
                ci.cancel();
                minecraft.showScreen(new ConfirmDisconnectScreen(minecraft.getCurrentScreen()));
            }
        }
    }
}

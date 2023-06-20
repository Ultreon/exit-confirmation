package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
//    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
//    public void exitConfirm$injectButtonClick(ButtonWidget button, CallbackInfo ci) {
//        if (button.id == 4) {
//            ActionResult result = WindowCloseEvent.EVENT.invoker().closing(WindowCloseEvent.Source.GENERIC);
//            if (result != ActionResult.CANCEL) {
//                ExitConfirmation.allowExit = true;
//                client.scheduleStop();
//            }
//            ci.cancel();
//        }
//    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectEscapePrompt(char id, int code, CallbackInfo ci) {
        if (code == Keyboard.KEY_ESCAPE && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (this.client.currentScreen == this) {
                this.client.setScreen(new ConfirmExitScreen(this.client.currentScreen));
                ci.cancel();
            }
        }
    }
}

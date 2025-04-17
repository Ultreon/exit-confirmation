package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectButtonClick(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 4) {
            if (ExitConfirmation.getInstance().onWindowClose(WindowCloseEvent.Source.QUIT_BUTTON) == ActionResult.CANCEL) {
                ci.cancel();
                return;
            }
            ExitConfirmation.allowExit = true;
            mc.scheduleStop();
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectEscapePrompt(char id, int code, CallbackInfo ci) {
        if (code == Keyboard.KEY_ESCAPE && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (this.mc.currentScreen == this) {
                this.mc.openScreen(new ConfirmExitScreen(this.mc.currentScreen));
                ci.cancel();
            }
        }
    }
}

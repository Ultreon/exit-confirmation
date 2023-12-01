package com.ultreon.mods.exitconfirmation.mixin;

import com.badlogic.gdx.Input;
import com.ultreon.craft.client.UltracraftClient;
import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.screens.TitleScreen;
import com.ultreon.craft.client.gui.widget.TextButton;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private boolean exitConfirmation$escPress;

    protected TitleScreenMixin(TextObject component) {
        super(component);
    }

    @Inject(at = @At("HEAD"), method = "build")
    public void exitConfirmation$build(CallbackInfo ci) {
        this.exitConfirmation$escPress = false;
    }

    @Inject(at = @At("HEAD"), method = "quitGame", cancellable = true)
    public void exitConfirmation$quitGame(TextButton caller, CallbackInfo ci) {
        ci.cancel();
        ExitConfirmation.onQuitButtonClick();
    }

    @Override
    public boolean keyPress(int keyCode) {
        return super.keyPress(keyCode);
    }

    @Override
    public boolean keyRelease(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (!this.exitConfirmation$escPress) {
                this.exitConfirmation$escPress = true;
                var client = UltracraftClient.get();
                if (client.screen == this) {
                    System.out.println("keyCode = " + keyCode);
                    client.showScreen(new ConfirmExitScreen(client.screen));
                    return true;
                }
            }
        }

        return super.keyRelease(keyCode);
    }
}

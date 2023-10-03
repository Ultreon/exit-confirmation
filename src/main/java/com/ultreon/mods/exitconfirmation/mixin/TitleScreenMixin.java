package com.ultreon.mods.exitconfirmation.mixin;

import com.badlogic.gdx.Input;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.render.gui.screen.Screen;
import com.ultreon.bubbles.render.gui.screen.TitleScreen;
import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    private boolean exitConfirmation$escPress;

    @Inject(at = @At("HEAD"), method = "init")
    public void exitConfirmation$init(CallbackInfo ci) {
        this.exitConfirmation$escPress = false;
    }

    @Override
    public boolean keyPress(int keycode) {
        if (keycode == Input.Keys.ESCAPE && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (!this.exitConfirmation$escPress) {
                this.exitConfirmation$escPress = true;
                var game = BubbleBlaster.getInstance();
                if (game.getCurrentScreen() == this) {
                    game.showScreen(new ConfirmExitScreen(game.getCurrentScreen()));
                    return true;
                }
            }
        }
        return super.keyPress(keycode);
    }

    @Override
    public boolean keyRelease(int keyCode) {
        // TODO Add config support back again.
        if (keyCode == 256 && this.exitConfirmation$escPress && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get() && this.game.getCurrentScreen() == this) {
            this.exitConfirmation$escPress = false;
            return true;
        }
        return super.keyRelease(keyCode);
    }
}

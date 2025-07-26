package dev.ultreon.mods.exitconfirmation.mixin;

import com.badlogic.gdx.Input;
import dev.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import dev.ultreon.mods.exitconfirmation.ExitConfig;
import dev.ultreon.quantum.client.QuantumClient;
import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.screens.TitleScreen;
import dev.ultreon.quantum.client.gui.widget.TitleButton;
import dev.ultreon.quantum.text.TextObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    private boolean exitConfirmation$escPress;

    protected TitleScreenMixin(TextObject component) {
        super(component);
    }

    @Inject(at = @At("HEAD"), method = "build")
    public void exitConfirmation$build(CallbackInfo ci) {
        this.exitConfirmation$escPress = false;
    }

    @Inject(at = @At("HEAD"), method = "quitGame", cancellable = true)
    public void exitConfirmation$quitGame(TitleButton caller, CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public boolean keyPress(int keyCode) {
        return super.keyPress(keyCode);
    }

    @Override
    public boolean keyRelease(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE && ExitConfig.closePrompt && ExitConfig.quitOnEscInTitle) {
            if (!this.exitConfirmation$escPress) {
                this.exitConfirmation$escPress = true;
                var client = QuantumClient.get();
                if (client.screen == this) {
                    client.showScreen(new ConfirmExitScreen(client.screen));
                    return true;
                }
            }
        }

        return super.keyRelease(keyCode);
    }
}

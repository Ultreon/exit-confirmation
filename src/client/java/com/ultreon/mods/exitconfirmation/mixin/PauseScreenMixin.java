package com.ultreon.mods.exitconfirmation.mixin;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ultreon.mods.exitconfirmation.ConfirmDisconnectScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import dev.ultreon.quantum.client.QuantumClientKt;
import dev.ultreon.quantum.client.QuantumVoxel;
import dev.ultreon.quantum.client.gui.screens.PauseScreen;
import dev.ultreon.quantum.client.gui.screens.Screen;
import kotlin.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    public PauseScreenMixin() {
    }

    @Inject(method = "setup$lambda$7$lambda$6", at = @At(value = "HEAD"), cancellable = true)
    private static void exitConfirmation$createPauseMenu(int it, CallbackInfoReturnable<Unit> cir) {
        var client = QuantumVoxel.instance;
        if (client.getScreen() instanceof PauseScreen) {
            if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
                cir.setReturnValue(Unit.INSTANCE);
                client.showScreen(new ConfirmDisconnectScreen(client.getScreen()));
            }
        }
    }
}


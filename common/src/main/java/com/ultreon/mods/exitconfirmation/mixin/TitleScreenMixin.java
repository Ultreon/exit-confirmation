package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private boolean exitConfirmation$escPress;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("HEAD"), method = "init")
    public void exitConfirmation$init(CallbackInfo ci) {
        this.exitConfirmation$escPress = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (!this.exitConfirmation$escPress) {
                this.exitConfirmation$escPress = true;
                var minecraft = Minecraft.getInstance();
                if (minecraft.screen == this) {
                    minecraft.setScreen(new ConfirmExitScreen(minecraft.screen));
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        var minecraft = Minecraft.getInstance();

        // TODO Add config support back again.
        if (keyCode == 256 && this.exitConfirmation$escPress && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get() && minecraft.screen == this) {
            this.exitConfirmation$escPress = false;
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}

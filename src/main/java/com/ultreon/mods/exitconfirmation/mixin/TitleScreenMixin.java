package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class TitleScreenMixin extends GuiScreen {
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectButtonClick(GuiButton button, CallbackInfo ci) {
        if (button.id == 4) {
            if (MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON))) {
                ci.cancel();
                return;
            }
            ExitConfirmation.allowExit = true;
            mc.shutdown();
        }
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectEscapePrompt(char id, int code, CallbackInfo ci) {
        if (code == Keyboard.KEY_ESCAPE && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            if (this.mc.currentScreen == this) {
                this.mc.displayGuiScreen(new ConfirmExitScreen(this.mc.currentScreen));
                ci.cancel();
            }
        }
    }
}

package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widgets.Button;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private boolean exitConfirmation$escPress;

    @Inject(at = @At("RETURN"), method = "init()V")
    private void exitConfirmation$init$return(CallbackInfo info) {
        ExitConfirmation.handleTitleScreenInit(minecraft, (TitleScreen)(Object)this);
    }

    @Inject(at = @At("HEAD"), method = "init")
    public void exitConfirmation$init$head(CallbackInfo ci) {
        exitConfirmation$escPress = false;
    }

    @Inject(at = @At("HEAD"), method = "buttonClicked", cancellable = true)
    public void exitConfirmation$buttonClicked(Button par1, CallbackInfo ci) {
        if (par1.id == 4) {
            ActionResult closing = WindowCloseEvent.EVENT.invoker().closing(ExitConfirmation.getGameWindow(), WindowCloseEvent.Source.QUIT_BUTTON);
            if (closing.equals(ActionResult.PASS)) {
                this.minecraft.scheduleStop();
            }
            ci.cancel();
        }
    }

    @Override
    public void keyPressed(char c, int i) {
        if (i == 1 && Config.getClosePrompt() && Config.getQuitOnEscInTitle()) {
            if (!exitConfirmation$escPress) {
                exitConfirmation$escPress = true;
                Minecraft minecraft = MinecraftAccessor.getInstance();
                if (minecraft.currentScreen == this) {
                    minecraft.openScreen(new ConfirmExitScreen(minecraft.currentScreen));
                    return;
                }
            }
        }
        super.keyPressed(c, i);
    }

    @Override
    public void onKeyboardEvent() {
        if (!Keyboard.getEventKeyState()) {
            this.keyReleased(Keyboard.getEventKey());
        }

        super.onKeyboardEvent();
    }

    public void keyReleased(int keyCode) {
        Minecraft minecraft = MinecraftAccessor.getInstance();
        if (keyCode == 1 && exitConfirmation$escPress && Config.getClosePrompt() && Config.getQuitOnEscInTitle() && minecraft.currentScreen == this) {
            exitConfirmation$escPress = false;
        }
    }
}

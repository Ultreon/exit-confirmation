package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.gui.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftApplet.class)
public class MinecraftAppletMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;scheduleStop()V"), method = "onRemoveNotify", cancellable = true)
    private void exitConfirm$injectScheduleStop(CallbackInfo ci) {
        Minecraft minecraft = ExitConfirmation.minecraft;
        if (minecraft == null) return;
        Screen currentScreen = minecraft.currentScreen;
        if (currentScreen instanceof ConfirmExitScreen) {
            ActionResult closing = WindowCloseEvent.EVENT.invoker().closing(ExitConfirmation.getGameWindow(), WindowCloseEvent.Source.QUIT_BUTTON);
            if (closing.equals(ActionResult.PASS)) {
                minecraft.scheduleStop();
            } else {
                ci.cancel();
            }
        } else {
            ci.cancel();
        }
    }
}

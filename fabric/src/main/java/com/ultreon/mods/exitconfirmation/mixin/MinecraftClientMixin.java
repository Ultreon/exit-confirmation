package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectExitConfirm(CallbackInfo ci) {
        if (ExitConfirmation.allowExit) return;
        ActionResult result = WindowCloseEvent.EVENT.invoker().closing(WindowCloseEvent.Source.GENERIC);
        if (result == ActionResult.CANCEL) {
            ci.cancel();
        }
    }
}

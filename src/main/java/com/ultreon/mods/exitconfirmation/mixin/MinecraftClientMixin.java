package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "shutdown", at = @At("HEAD"), cancellable = true)
    public void exitConfirm$injectExitConfirm(CallbackInfo ci) {
        if (ExitConfirmation.allowExit) return;
        boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON));
        if (flag) {
            ci.cancel();
        }
    }
}

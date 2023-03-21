package com.ultreon.mods.exitconfirmation.mixin;

import net.fabricmc.loader.entrypoint.applet.AppletFrame;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AppletFrame.class)
public class AppletFrameMixin {
    @Inject(at = @At(value = "HEAD"), method = "launch", cancellable = true, remap = false)
    private void start(String[] args, CallbackInfo ci) {
        Minecraft.main(args);
        ci.cancel();
    }
}

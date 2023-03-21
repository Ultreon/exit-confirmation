package com.ultreon.mods.exitconfirmation.mixin;

import net.fabricmc.loader.entrypoint.applet.AppletMain;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AppletMain.class)
public class AppletMainMixin {
    @Inject(at = @At(value = "HEAD"), method = "main", cancellable = true, remap = false)
    private static void start(String[] args, CallbackInfo ci) {
        Minecraft.main(args);
        ci.cancel();
    }
}

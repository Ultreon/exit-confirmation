package com.ultreon.mods.exitconfirmation.mixin;

import com.mojang.blaze3d.platform.Window;
import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public abstract Window getWindow();

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    private void stop$exitConfirmation(CallbackInfo ci) {
        if (ExitConfirmation.didAccept()) {
            return;
        }

        if (WindowCloseEvent.EVENT.invoker().closing(this.getWindow(), WindowCloseEvent.Source.GENERIC) == ActionResult.CANCEL) {
            GLFW.glfwSetWindowShouldClose(this.getWindow().getWindow(), false);
            ci.cancel();
        }
    }
}

package com.ultreon.mods.exitconfirmation.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.Window;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public abstract Window getWindow();

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    private void stop$exitConfirmation(CallbackInfo ci) {
        if (ExitConfirmation.didAccept()) {
            return;
        }


        if (MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(this.getWindow(), WindowCloseEvent.Source.GENERIC))) {
            GLFW.glfwSetWindowShouldClose(this.getWindow().getWindow(), false);
            ci.cancel();
        }
    }
}

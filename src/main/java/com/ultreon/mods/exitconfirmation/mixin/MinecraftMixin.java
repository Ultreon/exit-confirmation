package com.ultreon.mods.exitconfirmation.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.swing.*;
import java.awt.*;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(at = @At(value = "NEW", target = "Ljava/awt/Frame;"), method = "start(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V")
    private static Frame start(String title) {
        return new JFrame(title);
    }
}
